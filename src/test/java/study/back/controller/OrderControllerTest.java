package study.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import study.back.dto.request.CreateOrderRequestDto;
import study.back.entity.OrderEntity;
import study.back.exception.NotValidTotalPriceException;
import study.back.exception.PointAndCouponConflictException;
import study.back.service.implement.OrderProcessServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private OrderProcessServiceImpl orderProcessService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void givenOrder_whenCreateOrder_thenOrderSuccess() throws Exception {
        // given
        OrderEntity order = OrderEntity.builder()
                .totalPrice(10000)
                .build();

        given(orderProcessService.createOrder(any(), any(CreateOrderRequestDto.class))).willReturn(order);

        // when & then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/order")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CreateOrderRequestDto.builder().build()))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("주문 성공"))
                .andExpect(jsonPath("$.code").value("SU"))
                .andExpect(jsonPath("$.price").value(10000));

        verify(orderProcessService, times(1)).createOrder(any(), any(CreateOrderRequestDto.class));
    }

    @Test
    @WithMockUser
    void givenZeroOrderPrice_whenCreateOrder_thenReturnErrorResponseDto() throws Exception {
        // given
        given(orderProcessService.createOrder(any(), any(CreateOrderRequestDto.class))).willThrow(new NotValidTotalPriceException("총 금액이 0 입니다"));
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/order")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(CreateOrderRequestDto.builder().build()))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("NT"))
                .andExpect(jsonPath("$.message").value("총 금액이 0 입니다"));

        verify(orderProcessService, times(1)).createOrder(any(), any(CreateOrderRequestDto.class));
    }

    @Test
    @WithMockUser
    void givenCouponAndPoint_whenCreateOrder_thenReturnErrorResponseDto() throws Exception {
        // given
        given(orderProcessService.createOrder(any(), any(CreateOrderRequestDto.class))).willThrow(new PointAndCouponConflictException("포인트와 쿠폰 동시 사용 불가능"));
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/order")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(CreateOrderRequestDto.builder().build()))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("PCC"))
                .andExpect(jsonPath("$.message").value("포인트와 쿠폰 동시 사용 불가능"));

        verify(orderProcessService, times(1)).createOrder(any(), any(CreateOrderRequestDto.class));
    }
}