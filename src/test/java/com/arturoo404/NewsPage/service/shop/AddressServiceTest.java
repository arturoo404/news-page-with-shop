package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.address.dto.AddressDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import com.arturoo404.NewsPage.repository.shop.AddressRepository;
import com.arturoo404.NewsPage.service.impl.shop.AddressServiceImpl;
import com.arturoo404.NewsPage.validation.PhoneNumberValid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneNumberValid phoneNumberValid;

    @BeforeEach
    void setUp(){
        addressService = new AddressServiceImpl(addressRepository, phoneNumberValid, userRepository);
    }

    @Test
    void itShouldThrowBadPhoneNumber() {
        //Given
        AddressDto addressDto = AddressDto.builder()
                .phoneNumber(123L)
                .build();

        //When
        when(phoneNumberValid.valid(anyLong())).thenReturn(false);

        try {
            addressService.updateAddress(addressDto);
        } catch (ValidException e) {
            assertThat(e.getMessage()).isEqualTo("Bad phone number.");
        } catch (ExistInDatabaseException e) {
            throw new RuntimeException(e);
        }
        //Then
    }

    @Test
    void itShouldThrowExistInDataBaseExceptionForUser() {
        //Given
        final AddressDto addressDto = addressDto();
        //When
        when(phoneNumberValid.valid(anyLong()))
                .thenReturn(true);

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        try {
            addressService.updateAddress(addressDto);
        } catch (ValidException e) {
            throw new RuntimeException(e);
        } catch (ExistInDatabaseException e) {
            assertThat(e.getMessage()).isEqualTo("User not found.");
        }
        //Then
    }
    private AddressDto addressDto(){
        return AddressDto.builder()
                .email("test@gmail.com")
                .street("street")
                .phoneNumber(1234567890L)
                .postcode("22222")
                .homeNumber(10)
                .city("city")
                .firstName("first")
                .lastName("last")
                .build();
    }
}