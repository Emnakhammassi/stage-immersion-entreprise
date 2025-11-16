package com.FlickFlow.FlickFlow.config;

import com.FlickFlow.FlickFlow.user.repository.SessionRepository;
import com.FlickFlow.FlickFlow.user.service.MyUserDetailsService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;
import com.FlickFlow.FlickFlow.user.entity.Session;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtRequestFilter extends GenericFilterBean {

    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final SessionRepository sessionRepository;


    @Autowired
    public JwtRequestFilter(MyUserDetailsService userDetailsService, JwtUtil jwtUtil, SessionRepository sessionRepository) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwtToken);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                Optional<Session> optionalSession = sessionRepository.findByToken(jwtToken);

                if (optionalSession.isPresent()) {
                    Session session = optionalSession.get();

                    if (session.getExpiresAt().before(new Date()) || session.isRevoked()) {
                        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid or expired");
                        return;
                    }

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid or expired");
                    return;
                }
            }
        }

        chain.doFilter(httpRequest, httpResponse);
    }
}