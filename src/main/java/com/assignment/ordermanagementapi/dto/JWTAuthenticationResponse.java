package com.assignment.ordermanagementapi.dto;

import lombok.Data;

@Data
public class JWTAuthenticationResponse {

    private String token;

    private String refreshToken;
    
}
