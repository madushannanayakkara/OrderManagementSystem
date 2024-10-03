package com.assignment.ordermanagementapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderHistoryResponse {

    private String email;
    
    private List<OrderHistoryBase> orderHistory;
    
}
