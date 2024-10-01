package com.assignment.ordermanagementapi.cron;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.assignment.ordermanagementapi.entity.Order;
import com.assignment.ordermanagementapi.entity.OrderStatus;
import com.assignment.ordermanagementapi.service.OrderService;

@Configuration
@EnableScheduling
public class OrderDispatcherJob {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 0 * * * ?")
    public void dispatchOrders() {
        List<Order> newOrders = orderService.getOrdersByState(OrderStatus.NEW.name());
        newOrders.forEach(order -> {
            order.setStatus(OrderStatus.DISPATCHED);
            orderService.updateOrder(order);
        });

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        System.out.println("Time: " + currentTime.format(formatter) + ": Orders Updated!");
    }
    
}
