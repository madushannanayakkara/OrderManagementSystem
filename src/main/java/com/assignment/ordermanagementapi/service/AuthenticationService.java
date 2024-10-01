package com.assignment.ordermanagementapi.service;

import com.assignment.ordermanagementapi.dto.JWTAuthenticationResponse;
import com.assignment.ordermanagementapi.dto.RefreshTokenRequest;
import com.assignment.ordermanagementapi.dto.SignInRequest;
import com.assignment.ordermanagementapi.dto.SignUpRequest;
import com.assignment.ordermanagementapi.entity.Client;

public interface AuthenticationService {
    Client signup(SignUpRequest signUpRequest);

    JWTAuthenticationResponse signin(SignInRequest sighInRequest);

    JWTAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
