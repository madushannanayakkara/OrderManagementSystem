package com.assignment.ordermanagementapi.controller;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.ordermanagementapi.dto.OrderCreateRequest;
import com.assignment.ordermanagementapi.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateRequest orderCreateRequest,
                                    @RequestHeader("Authorization") String token){
        try {
            String extractedToken = token.substring(7);
            return ResponseEntity.ok(orderService.createOrder(orderCreateRequest, extractedToken));
        } catch (IllegalArgumentException e) {
            if ("Client not found!".equals(e.getMessage())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("msg", "Client not found!"));
            } else if ("Invalid request body!".equals(e.getMessage())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("msg", "Invalid request body!"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("msg", e.getMessage()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg", "An unexpected error occurred!"));
        }
    }

    @DeleteMapping("/cancel/{orderReference}")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderReference,
                                @RequestHeader("Authorization") String token) {
        try {
            String extractedToken = token.substring(7);

            return ResponseEntity.ok(orderService.cancelOrder(orderReference, extractedToken));
        } catch (IllegalArgumentException e) {
            if ("Invalid order reference!".equals(e.getMessage())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("msg", "Invalid order reference!"));
            } else if ("Only NEW orders can be cancelled!".equals(e.getMessage())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("msg", "Only NEW orders can be cancelled!"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("msg", e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg", "An unexpected error occurred!"));
        }
    }

    @GetMapping("/history/{page}/{size}")
    public ResponseEntity<?> fetchOrderHistory(
            @RequestHeader("Authorization") String token,
            @PathVariable int page, 
            @PathVariable int size) {

        try {
            String extractedToken = token.substring(7);
            
            return ResponseEntity.ok(orderService.fetchOrderHistory(extractedToken, PageRequest.of(page, size)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg", "An unexpected error occurred!"));
        }
    }
    
}
