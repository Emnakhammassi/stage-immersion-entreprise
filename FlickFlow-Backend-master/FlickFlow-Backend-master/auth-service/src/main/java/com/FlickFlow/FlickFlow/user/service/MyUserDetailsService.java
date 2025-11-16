package com.FlickFlow.FlickFlow.user.service;
import com.FlickFlow.FlickFlow.user.entity.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.FlickFlow.FlickFlow.user.repository.userRepository;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private userRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        user foundUser = userRepository.findByEmail(email);
        if (foundUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new CustomUserDetails(
                foundUser.getUserId(), // Assuming getId() returns the user ID
                foundUser.getEmail(),
                foundUser.getPassword(),
                new ArrayList<>() // Empty list of authorities, customize as needed
        );
    }


}
