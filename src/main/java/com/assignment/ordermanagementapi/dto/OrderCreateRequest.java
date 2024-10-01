package com.assignment.ordermanagementapi.dto;

import lombok.Data;

@Data
public class OrderCreateRequest {

    private String itemName;

    private int quantity;

    private String addressLine1;

    private String addressLine2;

    private String addressLine3;
    
}
