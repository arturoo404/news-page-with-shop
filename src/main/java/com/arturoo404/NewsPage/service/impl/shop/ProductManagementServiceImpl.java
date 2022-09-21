package com.arturoo404.NewsPage.service.impl.shop;

import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.product.Product;
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

    private void objectDatabaseExist(Optional<?> byId) throws ExistInDatabaseException {
        if (byId.isEmpty()){
            throw new ExistInDatabaseException("This product is not exist in database.");
        }
    }
}
