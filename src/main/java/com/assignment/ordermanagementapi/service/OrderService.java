package com.assignment.ordermanagementapi.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.assignment.ordermanagementapi.dto.OrderCreateRequest;
import com.assignment.ordermanagementapi.dto.OrderCreateResponse;
import com.assignment.ordermanagementapi.entity.Order;

public interface OrderService {

    OrderCreateResponse createOrder(OrderCreateRequest orderCreateReques, String token);

    Map<String, String> cancelOrder(Long orderId, String token);

    Page<Order> fetchOrderHistory(Long clientId, Pageable pageable);
    // Map<String, Pageable> fetchOrderHistory(Long clientId, Pageable pageable);

    List<Order> getOrdersByState(String state);

    Order updateOrder(Order order);
    
}
