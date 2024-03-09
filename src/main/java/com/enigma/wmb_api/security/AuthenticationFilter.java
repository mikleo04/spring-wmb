package com.enigma.wmb_api.security;

import com.enigma.wmb_api.dto.response.JwtClaims;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.service.JwtService;
import com.enigma.wmb_api.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String barearToken = request.getHeader("Authorization");

            if (barearToken != null && jwtService.verifyToken(barearToken)) {
                JwtClaims jwtClaims = jwtService.getClaimsByToken(barearToken);
                UserAccount userAccount = userService.getByuserId(jwtClaims.getUserAccountId());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userAccount.getUsername(),
                        null,
                        userAccount.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Cannot set User authentication : {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

}
