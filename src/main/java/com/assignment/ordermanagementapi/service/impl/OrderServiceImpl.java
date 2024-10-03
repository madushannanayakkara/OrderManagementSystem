package com.assignment.ordermanagementapi.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.assignment.ordermanagementapi.dto.OrderCreateRequest;
import com.assignment.ordermanagementapi.dto.OrderCreateResponse;
import com.assignment.ordermanagementapi.dto.OrderHistoryBase;
import com.assignment.ordermanagementapi.dto.OrderHistoryResponse;
import com.assignment.ordermanagementapi.entity.Client;
import com.assignment.ordermanagementapi.entity.Order;
import com.assignment.ordermanagementapi.entity.OrderStatus;
import com.assignment.ordermanagementapi.repository.ClientRepository;
import com.assignment.ordermanagementapi.repository.OrderRepository;
import com.assignment.ordermanagementapi.service.JWTService;
import com.assignment.ordermanagementapi.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ClientRepository clientRepository;

    private final JWTService jwtService;

    public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest, String token) {
        String userEmail = jwtService.extractUserName(token);
        Client client = clientRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Client not found!"));
        
        if (orderCreateRequest.getItemName() == null || orderCreateRequest.getItemName().isEmpty() ||
            orderCreateRequest.getAddressLine1() == null || orderCreateRequest.getAddressLine1().isEmpty() ||
            orderCreateRequest.getAddressLine2() == null || orderCreateRequest.getAddressLine2().isEmpty() ||
            orderCreateRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid request body!");
        } 

        Order order = new Order();
        order.setItemName(orderCreateRequest.getItemName());
        order.setQuantity(orderCreateRequest.getQuantity());
        order.setAddress_line_1(orderCreateRequest.getAddressLine1());
        order.setAddress_line_2(orderCreateRequest.getAddressLine2());
        order.setAddress_line_3(orderCreateRequest.getAddressLine3());
        order.setTimestamp(new Timestamp(System.currentTimeMillis()));
        order.setClient(client);
        order.setOrderReference();

        orderRepository.save(order);

        OrderCreateResponse response = new OrderCreateResponse();
        response.setOrderReference(order.getOrderReference());

        return response;
    }

    public Map<String, String> cancelOrder(String orderReference, String token) {
        Map<String, String> response = new HashMap<>();

        String userEmail = jwtService.extractUserName(token);
        Client client = clientRepository.findByEmail(userEmail)
                .orElseThrow();

        Optional<Order> optionalOrder = orderRepository.findByOrderReference(orderReference);

        if(!optionalOrder.isPresent()){
            throw new IllegalArgumentException("Invalid order reference!");
        }

        Order order = optionalOrder.get();

        if (client.getId() == order.getClient().getId()){
            if (order.getStatus() == OrderStatus.NEW) {
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
                response.put("msg", "Order successfully cancelled!");
            } else {
                throw new IllegalArgumentException("Only NEW orders can be cancelled!");
            }
        } else {
            response.put("msg", "You are not authorize to cancelled this order!");
        }

        return response;
    }

    public OrderHistoryResponse fetchOrderHistory(String token, Pageable pageable) {
        String userEmail = jwtService.extractUserName(token);
        Client client = clientRepository.findByEmail(userEmail).orElseThrow();
        OrderHistoryResponse orderHistoryResponse = new OrderHistoryResponse();

        orderHistoryResponse.setEmail(userEmail);

        List<Order> orderList = orderRepository.findByClient(client, pageable);
        List<OrderHistoryBase> orderHistoryBaseList = new ArrayList<>();

        orderList.forEach(order -> {
            OrderHistoryBase orderHistoryBase = new OrderHistoryBase();
            orderHistoryBase.setOrderReference(order.getOrderReference());
            orderHistoryBase.setItemName(order.getItemName());
            orderHistoryBase.setQuantity(order.getQuantity());
            orderHistoryBase.setAddress_line_1(order.getAddress_line_1());
            orderHistoryBase.setAddress_line_2(order.getAddress_line_2());
            orderHistoryBase.setAddress_line_3(order.getAddress_line_3());
            orderHistoryBase.setStatus(order.getStatus());

            orderHistoryBaseList.add(orderHistoryBase);
        });

        orderHistoryResponse.setOrderHistory(orderHistoryBaseList);

        return orderHistoryResponse;
    }

    public List<Order> getOrdersByState(String state){
        return orderRepository.findByStatus(state);
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
    
}
