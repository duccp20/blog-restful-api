package com.example.blogapprestapi.service.impl;

import com.example.blogapprestapi.exception.BlogApiException;
import com.example.blogapprestapi.model.dto.request.LoginDTO;

import com.example.blogapprestapi.model.dto.response.JwtTokenResponse;
import com.example.blogapprestapi.model.entity.User;
import com.example.blogapprestapi.repository.UserRepository;
import com.example.blogapprestapi.security.UserPrincipal;
import com.example.blogapprestapi.security.jwt.JwtAuthProvider;
import com.example.blogapprestapi.service.LoginService;
import com.example.blogapprestapi.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAuthProvider jwtAuthProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Object doLogin(LoginDTO loginDTO) {
        User user = userRepository.findByUsernameOrEmail(loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail())
                .orElseThrow(() -> new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid Username Or Email"));
        boolean password = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
        if (!password) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Password not valid!");
        }

        //tao access token.
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserPrincipal) authentication.getPrincipal();
        String username = userDetails.getUsername();
        String token = jwtAuthProvider.generateToken(username);

        List<String> roles = userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();


        //tao refresh token at the same time.
        String refreshToken = jwtAuthProvider.generateRefreshToken(userDetails);

        return JwtTokenResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .refreshToken(refreshToken)
                .roles(roles)
                .build();

    }
}
