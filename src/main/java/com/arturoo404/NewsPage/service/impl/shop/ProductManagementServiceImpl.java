package com.arturoo404.NewsPage.service.impl.shop;

import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.shop.AvailableProductRepository;
import com.arturoo404.NewsPage.repository.shop.ProductPriceRepository;
import com.arturoo404.NewsPage.service.shop.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductManagementServiceImpl implements ProductManagementService {

    @Autowired
    private final AvailableProductRepository availableProductRepository;

    @Autowired
    private final ProductPriceRepository productPriceRepository;

    public ProductManagementServiceImpl(AvailableProductRepository availableProductRepository, ProductPriceRepository productPriceRepository) {
        this.availableProductRepository = availableProductRepository;
        this.productPriceRepository = productPriceRepository;
    }

    @Override
    public Object updateProductStatus(Long id, Boolean status) throws ExistInDatabaseException {
        Optional<AvailableProduct> availableProductById = availableProductRepository.findAvailableProductById(id);
        objectDatabaseExist(availableProductById);

        AvailableProduct availableProduct = availableProductById.get();
        availableProduct.setAvailableStatus(status);

        return availableProductRepository.save(availableProduct);
    }

    @Override
    public Object updateProductQuantity(Long id, Integer quantity) throws ExistInDatabaseException {
        Optional<AvailableProduct> availableProductById = availableProductRepository.findAvailableProductById(id);
        objectDatabaseExist(availableProductById);
        AvailableProduct availableProduct = availableProductById.get();
        availableProduct.setProductQuantity(quantity);
        return availableProductRepository.save(availableProduct);
    }

    @Override
    public Object updateProductPrice(Long id, Double price) throws ExistInDatabaseException {
        Optional<ProductPrice> productPriceByProductId = productPriceRepository.findProductPriceByProductId(id);
        objectDatabaseExist(productPriceByProductId);

        ProductPrice productPrice = productPriceByProductId.get();
        productPrice.setPrice(price);
        return productPriceRepository.save(productPrice);
    }

    @Override
    public Object updateProductDiscountPrice(Long id, Double price) throws ExistInDatabaseException {
        Optional<ProductPrice> productPriceByProductId = productPriceRepository.findProductPriceByProductId(id);
        objectDatabaseExist(productPriceByProductId);

        ProductPrice productPrice = productPriceByProductId.get();
        productPrice.setDiscountPrice(price);
        return productPriceRepository.save(productPrice);
    }

    @Override
    public Object updatePromotionStatus(Long id, Boolean status) throws ExistInDatabaseException {
        Optional<ProductPrice> productPriceByProductId = productPriceRepository.findProductPriceByProductId(id);
        objectDatabaseExist(productPriceByProductId);

        ProductPrice productPrice = productPriceByProductId.get();
        productPrice.setDiscount(status);
        return productPriceRepository.save(productPrice);
    }

    private void objectDatabaseExist(Optional<?> byId) throws ExistInDatabaseException {
        if (byId.isEmpty()){
            throw new ExistInDatabaseException("This product is not exist in database.");
        }
    }
}
