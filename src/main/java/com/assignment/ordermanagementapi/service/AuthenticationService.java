package com.assignment.ordermanagementapi.service;

import com.assignment.ordermanagementapi.dto.JWTAuthenticationResponse;
import com.assignment.ordermanagementapi.dto.RefreshTokenRequest;
import com.assignment.ordermanagementapi.dto.SignInRequest;
import com.assignment.ordermanagementapi.dto.SignUpRequest;
import com.assignment.ordermanagementapi.dto.SignUpResponse;

public interface AuthenticationService {
    SignUpResponse signup(SignUpRequest signUpRequest);

    JWTAuthenticationResponse signin(SignInRequest sighInRequest);

    JWTAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
