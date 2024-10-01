package com.assignment.ordermanagementapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;

// import com.assignment.ordermanagementapi.entity.Client;

public interface ClientService {
    
    UserDetailsService userDetailsService();

    // Client createClient(Client client);

}
