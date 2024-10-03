package com.assignment.ordermanagementapi.dto;

import com.assignment.ordermanagementapi.entity.OrderStatus;

import lombok.Data;

@Data
public class OrderHistoryBase {

    private String orderReference;

    private String itemName;

    private int quantity;

    private String address_line_1;

    private String address_line_2;

    private String address_line_3;

    private OrderStatus status;
    
}
