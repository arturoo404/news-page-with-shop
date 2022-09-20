package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.repository.ProductRepository;
import com.arturoo404.NewsPage.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
