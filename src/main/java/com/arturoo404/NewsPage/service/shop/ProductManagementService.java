package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.exception.ExistInDatabaseException;

public interface ProductManagementService {
    Object updateProductStatus(Long id, Boolean status) throws ExistInDatabaseException;

    Object updateProductQuantity(Long id, Integer quantity) throws ExistInDatabaseException;

    Object updateProductPrice(Long id, Double price) throws ExistInDatabaseException;
}
