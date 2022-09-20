package com.arturoo404.NewsPage.service.impl.shop;

import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.entity.shop.product.dto.ProductCreateDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.shop.ProductRepository;
import com.arturoo404.NewsPage.service.shop.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
