package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.entity.shop.cart.dto.CartDetailDto;
import com.arturoo404.NewsPage.entity.shop.cart.dto.CartNavInfoDto;
import com.arturoo404.NewsPage.entity.shop.cart.dto.CartProductDetailDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.shop.CartService;
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

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartApiController.class)
class CartApiControllerTest {


    @MockBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    private String url = "/api/shop/cart";

    @Test
    @WithMockUser
    void itShouldReturnBadRequestAndMassageWhenQuantityIsEqualsZeroOrLess() throws Exception {
        //Given
        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/add-product")
                        .param("quantity", "0")
                        .param("email", "email@gmail.com")
                        .param("productId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Select number of product.");
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void itShouldReturnBadRequestWhenAddProductFindProblem() throws Exception {
        //Given
        //When
        doThrow(new ExistInDatabaseException(""))
                .when(cartService).addProductToCart(anyString(), anyLong(), anyInt());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/add-product")
                .param("quantity", "1")
                .param("email", "email@gmail.com")
                .param("productId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void itShouldReturnStatusOkWhenSuccessfullyAddedProductToCart() throws Exception {
        //Given
        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/add-product")
                .param("quantity", "1")
                .param("email", "email@gmail.com")
                .param("productId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    @WithMockUser
    void itShouldReturnCartNavInfo() throws Exception {
        //Given
        CartNavInfoDto cartNavInfoDto = new CartNavInfoDto(100D, 100L);

        //When
        when(cartService.findCartNavInfo(anyString()))
                .thenReturn(cartNavInfoDto);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/nav-info")
                        .param("email", "email@gmail.com")
                        .with(csrf()))
                .andExpect(jsonPath("$.amount", is(100D)))
                .andExpect(jsonPath("$.quantity", is(100)))
                .andExpect(status().isOk())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldReturnBadRequestWhenServiceFoundProblemWithCartNavInfo() throws Exception {
        //Given
        //When
        doThrow(new ExistInDatabaseException(""))
                .when(cartService).findCartNavInfo(anyString());

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/nav-info")
                        .param("email", "email@gmail.com")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldReturnCartDetailAndStatusOk() throws Exception {
        //Given
        CartDetailDto cartDetailDto = new CartDetailDto(100D, 100L, List.of(
                new CartProductDetailDto(10, 10L, "product", 100D),
                new CartProductDetailDto(11, 13L, "productTwo", 200D)
        ));

        //When
        when(cartService.findCartDetail(anyString()))
                .thenReturn(cartDetailDto);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/detail")
                        .param("email", "email@gmail.com")
                        .with(csrf()))
                .andExpect(jsonPath("$.amount", is(cartDetailDto.getAmount())))
                .andExpect(jsonPath("$.quantity", is(Math.toIntExact(cartDetailDto.getQuantity()))))
                .andExpect(jsonPath("$.product.length()", is(cartDetailDto.getProduct().size())))
                .andExpect(status().isOk())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldReturnBadRequestWhenServiceFoundProblemWithCartDetail() throws Exception {
        //Given
        //When
        doThrow(new ExistInDatabaseException(""))
                .when(cartService).findCartDetail(anyString());

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/detail")
                        .param("email", "email@gmail.com")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldReturnStatusOkWhenSuccessfullyDeletedProductFromCart() throws Exception {
        //Given
        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(url + "/delete-product")
                .param("quantity", "1")
                .param("email", "email@gmail.com")
                .param("productId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    @WithMockUser
    void itShouldReturnBadRequestWhenServiceFoundProblemWithDeleteProduct() throws Exception {
        //Given
        //When
        doThrow(new ExistInDatabaseException(""))
                .when(cartService).deleteProductFromCart(anyString(), anyLong(), anyInt());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(url + "/delete-product")
                .param("quantity", "1")
                .param("email", "email@gmail.com")
                .param("productId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }
}