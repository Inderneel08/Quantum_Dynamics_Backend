package com.example.Quantum_Dynamics_Backend.Security;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.Quantum_Dynamics_Backend.Jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAutheticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Get JWT header.
        // Bearer.
        // Validate the header.

        String requestTokenHeader = request.getHeader("Authorization");

        String jwtToken = null;

        System.out.println("RequestToken: -> " + requestTokenHeader);

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);

            if (!jwtTokenProvider.isTokenExpired(jwtToken)) {

                if (jwtTokenProvider.validateToken(jwtToken)) {

                    filterChain.doFilter(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        } else {
            String servletPath = request.getServletPath();

            if ((servletPath.equals("/login")) || (servletPath.equals("/register"))) {
                filterChain.doFilter(request, response);
            }
        }
    }

}
