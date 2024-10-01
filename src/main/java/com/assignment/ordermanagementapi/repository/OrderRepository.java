package com.assignment.ordermanagementapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.ordermanagementapi.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByClient_Id(Long clientId, Pageable pageable);

    List<Order> findByStatus(String status);
    
    Order findByOrderReference(String orderReference);
    
}
