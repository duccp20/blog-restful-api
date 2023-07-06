package com.example.blogapprestapi.service.impl;

import com.example.blogapprestapi.exception.BlogApiException;
import com.example.blogapprestapi.exception.ResourceNotFoundException;
import com.example.blogapprestapi.model.dto.LoginDTO;

import com.example.blogapprestapi.model.entity.User;
import com.example.blogapprestapi.repository.UserRepository;
import com.example.blogapprestapi.security.jwt.JwtAuthProvider;
import com.example.blogapprestapi.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public String doLogin(LoginDTO loginDTO) {
        User user = userRepository.findByUsernameOrEmail(loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail())
                .orElseThrow(() -> new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid Username Or Email"));
        boolean password = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
        if (!password) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Password not valid!");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtAuthProvider.generateToken(authentication);

        return token;
    }
}
