package com.arturoo404.NewsPage.service.impl.shop;

import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.shop.ProductRepository;
import com.arturoo404.NewsPage.service.shop.AvailableProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AvailableProductServiceImpl implements AvailableProductService {

    @Autowired
    private final ProductRepository productRepository;

    public AvailableProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product availableStatus(Long productId, Integer quantity) throws ExistInDatabaseException {
        Optional<Product> byIdAndStatus = productRepository.findByIdAndStatus(productId);
        if (byIdAndStatus.isEmpty()){
            throw new ExistInDatabaseException("We did not find this product.");
        }

        Product product = byIdAndStatus.get();
        if (product.getAvailableProduct().getProductQuantity() < quantity){
            throw new ExistInDatabaseException("We do not have that many product, please select less number of it.");
        }

        if (product.getAvailableProduct().getProductQuantity().equals(quantity)){
            product.getAvailableProduct().setAvailableStatus(false);
            product.getAvailableProduct().setProductQuantity(0);
            return productRepository.save(product);
        }

        product.getAvailableProduct().setProductQuantity(product.getAvailableProduct().getProductQuantity() - quantity);
        return productRepository.save(product);
    }
}
