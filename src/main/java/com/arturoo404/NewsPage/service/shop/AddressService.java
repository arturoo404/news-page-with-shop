package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.address.dto.AddressDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.exception.ValidException;

public interface AddressService {
    Object updateAddress(AddressDto addressDto) throws ValidException, ExistInDatabaseException;
}
