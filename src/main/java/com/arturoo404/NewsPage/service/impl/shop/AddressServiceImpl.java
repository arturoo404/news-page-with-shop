package com.arturoo404.NewsPage.service.impl.shop;

import com.arturoo404.NewsPage.entity.shop.address.Address;
import com.arturoo404.NewsPage.entity.shop.address.dto.AddressDto;
import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import com.arturoo404.NewsPage.repository.shop.AddressRepository;
import com.arturoo404.NewsPage.service.shop.AddressService;
import com.arturoo404.NewsPage.validation.PhoneNumberValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private final AddressRepository addressRepository;

    @Autowired
    private final PhoneNumberValid phoneNumberValid;

    @Autowired
    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository, PhoneNumberValid phoneNumberValid, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.phoneNumberValid = phoneNumberValid;
        this.userRepository = userRepository;
    }

    @Override
    public Object updateAddress(AddressDto addressDto) throws ValidException, ExistInDatabaseException {
        if (!phoneNumberValid.valid(addressDto.getPhoneNumber())){
            throw new ValidException("Bad phone number.");
        }

        final Optional<User> byEmail = userRepository.findByEmail(addressDto.getEmail());
        if (byEmail.isEmpty()){
            throw new ExistInDatabaseException("User not found.");
        }

        User user = byEmail.get();

        if (user.getAddress() != null){
            Optional<Address> a = addressRepository.findAddressByUserEmail(addressDto.getEmail());
            Address address = a.get();
            address.setCity(addressDto.getCity());
            address.setFirstName(addressDto.getFirstName());
            address.setLastName(addressDto.getLastName());
            address.setHomeNumber(String.valueOf(addressDto.getHomeNumber()));
            address.setPostcode(addressDto.getPostcode());
            address.setPhoneNumber(String.valueOf(addressDto.getPhoneNumber()));
            address.setStreet(addressDto.getStreet());
            return addressRepository.save(address);
        }

        user.setAddress(Address.builder()
                .city(addressDto.getCity())
                .firstName(addressDto.getFirstName())
                .lastName(addressDto.getLastName())
                .homeNumber(String.valueOf(addressDto.getHomeNumber()))
                .postcode(addressDto.getPostcode())
                .phoneNumber(String.valueOf(addressDto.getPhoneNumber()))
                .street(addressDto.getStreet())
                .build());

        return userRepository.save(user);

    }
}
