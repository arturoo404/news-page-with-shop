package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.shop.ProductManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductManagementApiController.class)
class ProductManagementApiControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductManagementService productManagementService;

    private String url = "/api/shop/product-management";

    @Test
    @WithMockUser
    void itShouldUpdateStatusAndReturnStatusOK() throws Exception {
        //Given
        //When
        when(productManagementService.updateProductStatus(anyLong(), anyBoolean()))
                .thenReturn(new AvailableProduct());

        mockMvc.perform(MockMvcRequestBuilders.patch(url + "/update-status/1")
                        .param("status", "TRUE")
                .with(csrf())).andExpect(status().isOk())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldThrowExceptionAndReturnStatusNotFoundWhenUpdateStatusCameAcrossProblem() throws Exception {
        //Given
        //When
        doThrow(new ExistInDatabaseException(""))
                .when(productManagementService).updateProductStatus(anyLong(), anyBoolean());

        mockMvc.perform(MockMvcRequestBuilders.patch(url + "/update-status/1")
                        .param("status", "TRUE")
                        .with(csrf())).andExpect(status().isNotFound())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldUpdateQuantityOfProductAndReturnStatusOK() throws Exception {
        //Given
        //When
        when(productManagementService.updateProductQuantity(anyLong(), anyInt()))
                .thenReturn(new AvailableProduct());

        mockMvc.perform(MockMvcRequestBuilders.patch(url + "/product-quantity/1")
                        .param("quantity", "10")
                        .with(csrf())).andExpect(status().isOk())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldThrowExceptionAndReturnStatusNotFoundWhenUpdateQuantityOfProductCameAcrossProblem() throws Exception {
        //Given
        //When
        doThrow(new ExistInDatabaseException(""))
                .when(productManagementService).updateProductQuantity(anyLong(), anyInt());

        mockMvc.perform(MockMvcRequestBuilders.patch(url + "/product-quantity/1")
                        .param("quantity", "10")
                        .with(csrf())).andExpect(status().isNotFound())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldChangeProductPriceAndReturnStatusOK() throws Exception {
        //Given
        //When
        when(productManagementService.updateProductPrice(anyLong(), anyDouble()))
                .thenReturn(new ProductPrice());

        mockMvc.perform(MockMvcRequestBuilders.patch(url + "/change-price/1")
                        .param("price", "100.0")
                        .with(csrf())).andExpect(status().isOk())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldThrowExceptionAndReturnStatusNotFoundWhenChangeProductPriceCameAcrossProblem() throws Exception {
        //Given
        //When
        doThrow(new ExistInDatabaseException(""))
                .when(productManagementService).updateProductPrice(anyLong(), anyDouble());

        mockMvc.perform(MockMvcRequestBuilders.patch(url + "/change-price/1")
                        .param("price", "100.0")
                        .with(csrf())).andExpect(status().isNotFound())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldChangeDiscountPriceOfProductAndReturnStatusOK() throws Exception {
        //Given
        //When
        when(productManagementService.updateProductDiscountPrice(anyLong(), anyDouble()))
                .thenReturn(new ProductPrice());

        mockMvc.perform(MockMvcRequestBuilders.patch(url + "/discount-price/1")
                        .param("price", "90.0")
                        .with(csrf())).andExpect(status().isOk())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldThrowExceptionAndReturnStatusNotFoundWhenChangeDiscountPriceCameAcrossProblem() throws Exception {
        //Given
        //When
        doThrow(new ExistInDatabaseException(""))
                .when(productManagementService).updateProductDiscountPrice(anyLong(), anyDouble());

        mockMvc.perform(MockMvcRequestBuilders.patch(url + "/discount-price/1")
                        .param("price", "90.0")
                        .with(csrf())).andExpect(status().isNotFound())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldChangePromotionStatusAndReturnStatusOK() throws Exception {
        //Given
        //When
        when(productManagementService.updateProductStatus(anyLong(), anyBoolean()))
                .thenReturn(new AvailableProduct());

        mockMvc.perform(MockMvcRequestBuilders.patch(url + "/promotion-status/1")
                        .param("status", "TRUE")
                        .with(csrf())).andExpect(status().isOk())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldThrowExceptionAndReturnStatusNotFoundWhenChangePromotionStatusCameAcrossProblem() throws Exception {
        //Given
        //When
        doThrow(new ExistInDatabaseException(""))
                .when(productManagementService).updatePromotionStatus(anyLong(), anyBoolean());

        mockMvc.perform(MockMvcRequestBuilders.patch(url + "/promotion-status/1")
                        .param("status", "TRUE")
                        .with(csrf())).andExpect(status().isNotFound())
                .andReturn();
        //Then
    }
}