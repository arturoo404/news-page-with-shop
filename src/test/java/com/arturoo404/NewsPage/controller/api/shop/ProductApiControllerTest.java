package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.entity.shop.product.dto.ProductCreateDto;
import com.arturoo404.NewsPage.entity.shop.product.dto.ProductDetail;
import com.arturoo404.NewsPage.entity.shop.product.dto.ProductPageDto;
import com.arturoo404.NewsPage.enums.ProductCategory;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.shop.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductApiController.class)
class ProductApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private String url = "/api/shop/product";

    @Test
    @WithMockUser
    void itShouldReturnStatusNoContentAndMessageWhenOneOrMoreFieldWasEmpty() throws Exception {
        //Given
        final ProductCreateDto productCreateDto = productCreateDto("");

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/create")
                .content(objectToJson(productCreateDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(204);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("One or more filed is empty.");
    }

    @Test
    @WithMockUser
    void itShouldReturnStatusOkProductCreate() throws Exception {
        //Given
        final ProductCreateDto productCreateDto = productCreateDto("name");

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/create")
                .content(objectToJson(productCreateDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    @WithMockUser
    void itShouldReturnStatusBadRequestWithMessageWhenThrowExistInDatabaseExceptionProductCreate() throws Exception {
        //Given
        final ProductCreateDto productCreateDto = productCreateDto("name");

        //When
        doThrow(ExistInDatabaseException.class)
                .when(productService).createProduct(any(ProductCreateDto.class));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/create")
                .content(objectToJson(productCreateDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void itShouldReturnBadRequestWhenProductPhotoWasEmpty() throws Exception {
        //Given
        MockMultipartFile photo = new MockMultipartFile("file", "filename.png", "image/jpeg", "10".getBytes());

        //When
        doThrow(ExistInDatabaseException.class)
                .when(productService).setProductPhoto(any(MultipartFile.class), anyLong());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(url + "/photo/set/2", "patch")
                .file(photo)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(404);
    }

    @Test
    @WithMockUser
    void itShouldReturnOkWhenPhotoWasSaved() throws Exception {
        //Given
        MockMultipartFile photo = new MockMultipartFile("file", "filename.png", "image/jpeg", "10".getBytes());

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(url + "/photo/set/2", "patch")
                .file(photo)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    @WithMockUser
    void itShouldReturnPageOfProduct() throws Exception {
        //Given
        Page<ProductPageDto> page = new PageImpl<>(List.of(productPageDto(),
                productPageDto()));

        //When
        when(productService.getProductList(anyInt()))
                .thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/list")
                .param("page", "0")
                        .with(csrf()))
                .andExpect(jsonPath("$.totalElements", is((int) page.get().count())))
                .andExpect(status().isOk())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldReturnPageOfProductByCategory() throws Exception {
        //Given
        Page<ProductPageDto> page = new PageImpl<>(List.of(productPageDto(),
                productPageDto()));

        //When
        when(productService.getProductListByCategory(anyInt(), any(ProductCategory.class)))
                .thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/list/OTHER")
                        .param("page", "0")

                        .with(csrf()))
                .andExpect(jsonPath("$.totalElements", is((int) page.get().count())))
                .andExpect(status().isOk())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldReturnProductDetailAndStatusOk() throws Exception {
        //Given
        ProductDetail productDetail = ProductDetail.builder()
                .name("name")
                .price(100D)
                .discountPrice(100D)
                .productQuantity(100)
                .description("desc")
                .build();

        //When

        when(productService.productDetail(anyLong()))
                .thenReturn(productDetail);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/detail")
                        .param("id", "0")
                        .with(csrf()))
                .andExpect(jsonPath("$.price", is(productDetail.getPrice())))
                .andExpect(jsonPath("$.name", is(productDetail.getName())))
                .andExpect(jsonPath("$.discountPrice", is(productDetail.getDiscountPrice())))
                .andExpect(jsonPath("$.productQuantity", is(productDetail.getProductQuantity())))
                .andExpect(jsonPath("$.description", is(productDetail.getDescription())))
                .andExpect(status().isOk())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldReturnStatusNotFoundWhenThrowExistInDatabaseExceptionProductDetail() throws Exception {
        //Given
        //When
        doThrow(ExistInDatabaseException.class)
                .when(productService).productDetail(anyLong());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/detail")
                .param("id", "0")
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(404);
    }

    private ProductPageDto productPageDto() {
        return ProductPageDto.builder()
                .quantity(100)
                .price(100D)
                .id(1L)
                .name("product")
                .discountPrice(100D)
                .build();
    }

    private ProductCreateDto productCreateDto(String name){
        return ProductCreateDto.builder()
                .name(name)
                .productQuantity(100)
                .productCategory(ProductCategory.OTHER)
                .price(100D)
                .description("desc")
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