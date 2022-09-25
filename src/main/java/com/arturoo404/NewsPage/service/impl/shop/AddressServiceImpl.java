package com.arturoo404.NewsPage.service.impl.shop;

import com.arturoo404.NewsPage.repository.shop.AddressRepository;
import com.arturoo404.NewsPage.service.shop.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
}
