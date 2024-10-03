package com.assignment.ordermanagementapi.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.assignment.ordermanagementapi.dto.OrderCreateRequest;
import com.assignment.ordermanagementapi.dto.OrderCreateResponse;
import com.assignment.ordermanagementapi.dto.OrderHistoryResponse;
import com.assignment.ordermanagementapi.entity.Order;
import com.assignment.ordermanagementapi.exception.JWTException;

public interface OrderService {

    OrderCreateResponse createOrder(OrderCreateRequest orderCreateReques, String token);

    Map<String, String> cancelOrder(String orderReference, String token);

    OrderHistoryResponse fetchOrderHistory(String token, Pageable pageable);

    List<Order> getOrdersByState(String state);

    Order updateOrder(Order order);
    
}
