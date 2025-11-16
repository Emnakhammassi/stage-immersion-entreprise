package com.FlickFlow.FlickFlow.user.controller;

import com.FlickFlow.FlickFlow.config.JwtUtil;
import com.FlickFlow.FlickFlow.user.api.Response.JwtResponse;
import com.FlickFlow.FlickFlow.user.api.Request.RefreshTokenRequest;
import com.FlickFlow.FlickFlow.user.dto.userDto;
import com.FlickFlow.FlickFlow.user.entity.Session;
import com.FlickFlow.FlickFlow.user.repository.SessionRepository;
import com.FlickFlow.FlickFlow.user.service.AuthenticationService;
import com.FlickFlow.FlickFlow.user.service.CustomUserDetails;
import com.FlickFlow.FlickFlow.user.service.MyUserDetailsService;
import com.FlickFlow.FlickFlow.user.service.userService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private userService userService;
    @Autowired
    private SessionRepository sessionRepository;
    private final AuthenticationService authenticationService;


    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody userDto userDto) {
        try {
            String jwt = authenticationService.authenticate(userDto.getEmail(), userDto.getPassword());
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userDto.getEmail());
            int userId = userDetails.getId();

            return ResponseEntity.ok(new JwtResponse(jwt, userDto.getEmail(), userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }


    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(userDetails);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody userDto userDto) {
        try {
            log.info("Registering user: " + userDto);
            userService.register(userDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthController.ApiResponse("User registered successfully"));
        } catch (Exception e) {
            log.error("Error registering user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String token = request.get("token");

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is missing");
        }

        try {
            authenticationService.logout(token);
            return ResponseEntity.ok("User logged out successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token or session already revoked.");
        }
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        Optional<Session> sessionOptional = sessionRepository.findByToken(refreshToken);

        if (sessionOptional.isPresent() && !sessionOptional.get().isRevoked() && !jwtUtil.isTokenExpired(refreshToken)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(sessionOptional.get().getUsername());
            String newJwt = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(newJwt, userDetails.getUsername()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token.");
        }
    }


    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            if (username != null && jwtUtil.isTokenValid(token, username)) {
                return ResponseEntity.ok(username);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }


    @Getter
    public static class ApiResponse {
        private String message;

        public ApiResponse(String message) {
            this.message = message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }



}
