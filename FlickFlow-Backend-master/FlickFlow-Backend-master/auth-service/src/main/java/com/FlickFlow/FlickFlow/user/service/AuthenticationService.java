package com.FlickFlow.FlickFlow.user.service;

import com.FlickFlow.FlickFlow.config.JwtUtil;
import com.FlickFlow.FlickFlow.user.entity.Session;
import com.FlickFlow.FlickFlow.user.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final SessionRepository sessionRepository;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService,
                                 JwtUtil jwtUtil, SessionRepository sessionRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.sessionRepository = sessionRepository;
    }

    public String authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails);

        Session session = new Session();
        session.setToken(jwt);
        session.setUsername(username);
        session.setCreatedAt(new Date());
        session.setExpiresAt(new Date(System.currentTimeMillis() + jwtUtil.JWT_EXPIRATION_MS));
        sessionRepository.save(session);

        return jwt;
    }

    public void logout(String token) {
        Optional<Session> session = sessionRepository.findByToken(token);
        session.ifPresent(s -> {
            s.setRevoked(true);
            sessionRepository.save(s);
        });
    }
}
