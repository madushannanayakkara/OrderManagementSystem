package com.assignment.ordermanagementapi.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.assignment.ordermanagementapi.exception.JWTException;
import com.assignment.ordermanagementapi.service.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
@PropertySource("classpath:secrets.properties")
public class JWTServiceImpl implements JWTService {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public String generateToken(UserDetails userDetails) {
        try {
            return Jwts.builder().setSubject(userDetails.getUsername()) // Set the username as subject
                        .setIssuedAt(new Date(System.currentTimeMillis())) // Set the issue date
                        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Set expiration for one day
                        .signWith(getSignKey(), SignatureAlgorithm.HS256) // Sign using HS256 algorithm
                        .compact(); // Generate token
        } catch (JWTException e) {
            throw new JWTException("Error generating token: " + e.getMessage());
        }
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails){
        try {
            return Jwts.builder().setClaims(extraClaims)
                        .setSubject(userDetails.getUsername()) // Set the username as subject
                        .setIssuedAt(new Date(System.currentTimeMillis())) // Set the issue date
                        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Set expiration for 7 days
                        .signWith(getSignKey(), SignatureAlgorithm.HS256) // Sign using HS256 algorithm
                        .compact(); // Generate token
        } catch (JWTException e) {
            throw new JWTException("Error generating refresh token: " + e.getMessage());
        }
    }

    public String extractUserName(String token){
        try {
            return extractClaim(token, Claims::getSubject); // Extract the subject (username) from claims
        } catch (JWTException e) {
            throw new JWTException("Error extracting username: " + e.getMessage());
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        try {
            final String username = extractUserName(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException e) {
            throw new JWTException("Token validation failed: " + e.getMessage());
        }
    }

    private boolean isTokenExpired(String token){
        try {
            return extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (ExpiredJwtException e) {
            throw new JWTException("Token is expired: " + e.getMessage());
        } catch (JwtException e) {
            throw new JWTException("Error checking token expiration: " + e.getMessage());
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolvers){
        try {
            final Claims claims = extractAllClaims(token); // Extract all claims
            return claimResolvers.apply(claims); // Apply the provided claim resolver function
        } catch (JwtException e) {
            throw new JWTException("Error extracting claim: " + e.getMessage());
        }
    }

    private SecretKey getSignKey(){
        try {
            byte[] key = Decoders.BASE64.decode(JWT_SECRET);
            return Keys.hmacShaKeyFor(key); // Return the secret key for signing
        } catch (Exception e) {
            throw new JWTException("Error generating secret key: " + e.getMessage());
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                        .setSigningKey(getSignKey()) // Set the signing key
                        .build()
                        .parseClaimsJws(token) // Parse the token to extract claims
                        .getBody(); // Return the body (claims)
        } catch (JwtException e) {
            throw new JWTException("Error extracting claims: " + e.getMessage());
        }
    }
    
}
