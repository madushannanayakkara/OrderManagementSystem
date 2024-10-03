package com.assignment.ordermanagementapi.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.ordermanagementapi.entity.Client;
import com.assignment.ordermanagementapi.entity.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClient(Client client,  Pageable pageable);

    List<Order> findByStatus(String status);

    Optional<Order> findByOrderReference(String orderReference);
    
}
