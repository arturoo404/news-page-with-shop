package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.entity.shop.order.dto.OrderDetailDto;
import com.arturoo404.NewsPage.entity.shop.order.dto.OrderDto;
import com.arturoo404.NewsPage.entity.shop.order.dto.OrderUserListDto;
import com.arturoo404.NewsPage.entity.shop.order_detail.dto.OrderProductDto;
import com.arturoo404.NewsPage.enums.OrderStatus;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.shop.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderApiController.class)
class OrderApiControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private String url = "/api/shop/order";

    @Test
    @WithMockUser
    void itShouldReturnBadRequestAndMessageWhenClientInformationFieldIsEmpty() throws Exception {
        //Given
        OrderDto orderDto = OrderDto.builder()
                .street("street")
                .postcode("22222")
                .homeNumber(10)
                .city("city")
                .firstName("first")
                .lastName("last")
                .commentsToOrder("comments")
                .build();

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/make")
                        .content(objectToJson(orderDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("One or more client information field is empty.");
    }

    @Test
    @WithMockUser
    void itShouldReturnBadRequestAndMessageWhenAddressInformationFieldIsEmpty() throws Exception {
        //Given
        OrderDto orderDto = OrderDto.builder()
                .email("test@gmail.com")
                .street("street")
                .phoneNumber(1234567890L)
                .homeNumber(10)
                .city("")
                .firstName("first")
                .lastName("last")
                .commentsToOrder("comments")
                .build();

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/make")
                .content(objectToJson(orderDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("One or more address field is empty.");
    }

    @Test
    @WithMockUser
    void itShouldReturnBadRequestWithMessageMakeOrder() throws Exception {
        //Given
        OrderDto orderDto = OrderDto.builder()
                .email("test@gmail.com")
                .street("street")
                .phoneNumber(1234567890L)
                .homeNumber(10)
                .city("aa")
                .postcode("23234")
                .firstName("first")
                .lastName("last")
                .commentsToOrder("comments")
                .build();
        //When

        doThrow(new ExistInDatabaseException("Message"))
                .when(orderService).makeOrder(any(OrderDto.class));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/make")
                .content(objectToJson(orderDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(mvcResult.getResponse().getContentAsString()).isNotEmpty();
    }

    @Test
    @WithMockUser
    void itShouldReturnStatusOkAfterSuccessfullyCreatedOrder() throws Exception {
        //Given
        OrderDto orderDto = OrderDto.builder()
                .email("test@gmail.com")
                .street("street")
                .phoneNumber(1234567890L)
                .homeNumber(10)
                .city("aa")
                .postcode("23234")
                .firstName("first")
                .lastName("last")
                .commentsToOrder("comments")
                .build();
        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/make")
                .content(objectToJson(orderDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    @WithMockUser
    void itShouldReturnUserOrderListAndStatusOk() throws Exception {
        //Given
        List<OrderUserListDto> orderUserListDtos = List.of(
                orderUserListDtoOne(), orderUserListDtoTwo()
        );

        //When
        when(orderService.getUserOrderList(anyString()))
                .thenReturn(orderUserListDtos);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/user-list")
                        .param("email", "email@gmail.com")
                        .with(csrf()))
                .andExpect(jsonPath("$.length()", is(orderUserListDtos.size())))
                .andExpect(status().isOk())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldReturnOrderDetailAndStatusOk() throws Exception {
        //Given
        final OrderDetailDto orderDetailDto = OrderDetailDto.builder()
                .products(List.of(
                        new OrderProductDto(),
                        new OrderProductDto()
                )).date(new Date())
                .amount(100D)
                .orderStatus(OrderStatus.NEW)
                .build();

        //When
        when(orderService.getUserOrderDetail(anyString(), anyLong()))
                .thenReturn(orderDetailDto);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/user-detail")
                        .param("email", "email@gmail.com")
                        .param("orderId", "1")
                        .with(csrf()))
                .andExpect(jsonPath("$.products.length()", is(orderDetailDto.getProducts().size())))
                .andExpect(jsonPath("$.orderStatus", is(orderDetailDto.getOrderStatus().name())))
                .andExpect(status().isOk())
                .andReturn();
        //Then
    }

    @Test
    @WithMockUser
    void itShouldReturnBadRequestWhenUserOrderDetailFindProblem() throws Exception {
        //Given
        //When
        doThrow(new ExistInDatabaseException("message"))
                .when(orderService).getUserOrderDetail(anyString(), anyLong());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/user-detail")
                .param("email", "email@gmail.com")
                .param("orderId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();
        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(mvcResult.getResponse().getContentAsString()).isNotEmpty();
    }

    @Test
    @WithMockUser
    void itShouldReturnManagementListOfOrdersAndStatusOk() throws Exception {
        //Given
        Page<OrderUserListDto> listDto = new PageImpl<>(
                List.of(orderUserListDtoOne(), orderUserListDtoTwo())
        );

        //When
        when(orderService.getPageOfOrderList(anyInt(), any(OrderStatus.class)))
                .thenReturn(listDto);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/management/list")
                        .param("page", "0")
                        .param("status", "NEW")
                        .with(csrf()))
                .andExpect(jsonPath("$.totalElements", is((int) listDto.get().count())))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser
    void itShouldUpdateOrderStatusAndReturnStatusOk() throws Exception {
        //Given
        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url + "/management/change-status")
                        .param("orderId", "1")
                        .param("status", "NEW")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }

    private OrderUserListDto orderUserListDtoOne() {
        return OrderUserListDto.builder()
                .date(new Date())
                .orderStatus(OrderStatus.NEW)
                .id(1L)
                .amount(100D)
                .build();
    }

    private OrderUserListDto orderUserListDtoTwo() {
        return OrderUserListDto.builder()
                .date(new Date())
                .orderStatus(OrderStatus.NEW)
                .id(2L)
                .amount(200D)
                .build();
    }

    @Test
    void changeOrderStatus() {
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}