package com.arturoo404.NewsPage.service.impl.shop;

import com.arturoo404.NewsPage.entity.news.article.dto.TileArticleDto;
import com.arturoo404.NewsPage.entity.news.photo.dto.PhotoDto;
import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.entity.shop.product.dto.ProductCreateDto;
import com.arturoo404.NewsPage.entity.shop.product.dto.ProductPageDto;
import com.arturoo404.NewsPage.enums.Tag;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.shop.ProductRepository;
import com.arturoo404.NewsPage.service.shop.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Object createProduct(ProductCreateDto product) throws ExistInDatabaseException {
        Optional<Product> p = productRepository.findProductByName(product.getName());

        if (p.isPresent()){
            throw new ExistInDatabaseException("This product exist in database, change name and try again.");
        }

         Product productBuild = Product.builder()
                .description(product.getDescription())
                .productCategory(product.getProductCategory())
                .name(product.getName())
                .build();

        productBuild.setProductPrice(ProductPrice.builder()
                .product(productBuild)
                .discount(false)
                .discountPrice(product.getPrice())
                .price(product.getPrice())
                .build());
        productBuild.setAvailableProduct(AvailableProduct.builder()
                        .availableStatus(true)
                        .productQuantity(product.getProductQuantity())
                        .product(productBuild)
                .build());

        return productRepository.save(productBuild);
    }

    @Override
    public Object setProductPhoto(MultipartFile photo, Long id) throws ExistInDatabaseException, IOException {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()){
            throw new ExistInDatabaseException("This product is not exist in database.");
        }
        byId.get().setPhoto(photo.getBytes());

        return productRepository.save(byId.get());
    }

    @Override
    public Page<ProductPageDto> getProductList(Integer page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("id").descending());
        return productRepository.findALllAvailableProduct(pageable)
                .map(t -> ProductPageDto.builder()
                        .id(t.getId())
                        .discountPrice(t.getProductPrice().getDiscountPrice())
                        .name(t.getName())
                        .price(t.getProductPrice().getPrice())
                        .build());
    }

    @Override
    public PhotoDto getProductPhoto(Long id) {
        final Product product = productRepository.findById(id).get();
        return new PhotoDto(product.getPhoto());
    }
}
