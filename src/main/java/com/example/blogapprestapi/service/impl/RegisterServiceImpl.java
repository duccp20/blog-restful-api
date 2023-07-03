package com.example.blogapprestapi.service.impl;

import com.example.blogapprestapi.exception.BlogApiException;
import com.example.blogapprestapi.model.dto.RegisterDTO;
import com.example.blogapprestapi.model.entity.Role;
import com.example.blogapprestapi.model.entity.User;
import com.example.blogapprestapi.repository.RoleRepository;
import com.example.blogapprestapi.repository.UserRepository;
import com.example.blogapprestapi.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String doRegister(RegisterDTO registerDTO) {

       if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "ALREADY REGISTERED USER NAME");
       }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "ALREADY REGISTERED EMAIL");
        }

        User user = new User();
        user.setName(registerDTO.getName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setUsername(registerDTO.getUsername());

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_USER").get();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return "User register Success";
    }
}
