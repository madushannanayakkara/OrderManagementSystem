package com.assignment.ordermanagementapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface ClientService {
    
    UserDetailsService userDetailsService();

}
