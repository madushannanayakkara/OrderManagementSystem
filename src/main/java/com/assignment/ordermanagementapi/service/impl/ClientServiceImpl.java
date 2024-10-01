package com.assignment.ordermanagementapi.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// import com.assignment.ordermanagementapi.entity.Client;
import com.assignment.ordermanagementapi.repository.ClientRepository;
import com.assignment.ordermanagementapi.service.ClientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return clientRepository.findByEmail(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
            }
            
        };
    }

    // public Client createClient(Client client) {
    //     return clientRepository.save(client);
    // }
    
}
