package com.assignment.ordermanagementapi.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.assignment.ordermanagementapi.service.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@PropertySource("classpath:secrets.properties")
public class JWTServiceImpl implements JWTService {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public String generateToken(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername()) // Set the username as subject
                    .setIssuedAt(new Date(System.currentTimeMillis())) // Set the issue date
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Set expiration for one day
                    .signWith(getSignKey(), SignatureAlgorithm.HS256) // Sign using HS256 algorithm
                    .compact(); // Generate token
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder().setClaims(extraClaims)
                    .setSubject(userDetails.getUsername()) // Set the username as subject
                    .setIssuedAt(new Date(System.currentTimeMillis())) // Set the issue date
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Set expiration for 7 days
                    .signWith(getSignKey(), SignatureAlgorithm.HS256) // Sign using HS256 algorithm
                    .compact(); // Generate token
    }

    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject); // Extract the subject (username) from claims
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolvers){
        final Claims claims = extractAllClaims(token); // Extract all claims
        return claimResolvers.apply(claims); // Apply the provided claim resolver function
    }

    private SecretKey getSignKey(){
        System.out.println("This is secret key: " + JWT_SECRET);
        byte[] key = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(key); // Return the secret key for signing
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                    .setSigningKey(getSignKey()) // Set the signing key
                    .build()
                    .parseClaimsJws(token) // Parse the token to extract claims
                    .getBody(); // Return the body (claims)
    }
    
}
