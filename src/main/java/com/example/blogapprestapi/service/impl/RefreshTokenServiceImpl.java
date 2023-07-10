package com.example.blogapprestapi.service.impl;

import com.example.blogapprestapi.exception.BlogApiException;
import com.example.blogapprestapi.model.dto.response.JwtTokenRefreshResponse;
import com.example.blogapprestapi.model.entity.User;
import com.example.blogapprestapi.repository.UserRepository;
import com.example.blogapprestapi.security.UserPrincipal;
import com.example.blogapprestapi.security.jwt.JwtAuthFilter;
import com.example.blogapprestapi.security.jwt.JwtAuthProvider;
import com.example.blogapprestapi.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtAuthProvider jwtAuthProvider;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Override
    public Object doRefreshToken(HttpServletRequest request) {

        String refreshToken = jwtAuthFilter.getTokenFromRequest(request);
        String accessToken = null;
        if (StringUtils.hasText(refreshToken) && jwtAuthProvider.validateToken(refreshToken)) {
            String username = jwtAuthProvider.getUsername(refreshToken);
            User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                    () -> new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid Username Or Email"));
            accessToken = jwtAuthProvider.generateToken(user.getUsername());
        } else {
            throw new BlogApiException(HttpStatus.UNAUTHORIZED, "Khong AUthen duoc!");
        }
        return JwtTokenRefreshResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }


}
