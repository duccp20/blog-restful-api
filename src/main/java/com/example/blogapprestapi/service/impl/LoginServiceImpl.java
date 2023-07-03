package com.example.blogapprestapi.service.impl;

import com.example.blogapprestapi.model.dto.LoginDTO;

import com.example.blogapprestapi.security.jwt.JwtAuthProvider;
import com.example.blogapprestapi.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAuthProvider jwtAuthProvider;

    @Override
    public String doLogin(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtAuthProvider.generateToken(authentication);

        return token;
    }
}
