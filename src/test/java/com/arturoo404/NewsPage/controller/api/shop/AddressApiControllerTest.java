package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.controller.api.news.ArticleApiController;
import com.arturoo404.NewsPage.entity.news.comments.dto.AddCommentsDto;
import com.arturoo404.NewsPage.entity.shop.address.dto.AddressDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.service.shop.AddressService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AddressApiController.class)
class AddressApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    private String url = "/api/shop/address/";


    @Test
    @WithMockUser
    void itShouldThrowExceptionAndReturnStatusBadRequest() throws Exception {
        //Given
        AddressDto addressDto = addressDto();

        //When
        doThrow(new ExistInDatabaseException(""))
                .when(addressService).updateAddress(any(AddressDto.class));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(addressDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void itShouldReturnStatusOkAndObject() throws Exception {
        //Given
        AddressDto addressDto = addressDto();

        //When

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(addressDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
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

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}