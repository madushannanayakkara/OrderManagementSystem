package com.assignment.ordermanagementapi.service.impl;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.assignment.ordermanagementapi.dto.JWTAuthenticationResponse;
import com.assignment.ordermanagementapi.dto.RefreshTokenRequest;
import com.assignment.ordermanagementapi.dto.SignInRequest;
import com.assignment.ordermanagementapi.dto.SignUpRequest;
import com.assignment.ordermanagementapi.dto.SignUpResponse;
import com.assignment.ordermanagementapi.entity.Client;
import com.assignment.ordermanagementapi.repository.ClientRepository;
import com.assignment.ordermanagementapi.service.AuthenticationService;
import com.assignment.ordermanagementapi.service.JWTService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public SignUpResponse signup(SignUpRequest signUpRequest){
        if(clientRepository.findByEmail(signUpRequest.getEmail()).isPresent()){
            throw new IllegalArgumentException("User already exist!");
        }
        
        Client client = new Client();

        client.setEmail(signUpRequest.getEmail());
        client.setFirstname(signUpRequest.getFirstname());
        client.setLastname(signUpRequest.getLastname());
        client.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        clientRepository.save(client);

        SignUpResponse response = new SignUpResponse();
        response.setEmail(signUpRequest.getEmail());
        response.setFirstname(signUpRequest.getFirstname());
        response.setLastname(signUpRequest.getLastname());

        return response;
    }

    public JWTAuthenticationResponse signin(SignInRequest sighInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(sighInRequest.getEmail(), 
                sighInRequest.getPassword()));

        Client client = clientRepository.findByEmail(sighInRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password!"));
        String jwt = jwtService.generateToken(client);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), client);

        JWTAuthenticationResponse jwtAuthenticationResponse = new JWTAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;

    }

    public JWTAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        Client client = clientRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        if(!jwtService.isTokenValid(refreshTokenRequest.getToken(), client)){
            throw new IllegalArgumentException("Provided token is invalid! Please login again");
        }

        String jwt = jwtService.generateToken(client);

        JWTAuthenticationResponse jwtAuthenticationResponse = new JWTAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

        return jwtAuthenticationResponse;
    }
    
}
