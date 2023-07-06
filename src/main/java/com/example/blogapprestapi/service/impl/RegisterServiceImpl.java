package com.example.blogapprestapi.service.impl;

import com.example.blogapprestapi.event.RegistrationCompleteEvent;
import com.example.blogapprestapi.exception.BlogApiException;
import com.example.blogapprestapi.exception.ResourceNotFoundException;
import com.example.blogapprestapi.model.dto.RegisterDTO;
import com.example.blogapprestapi.model.entity.Role;
import com.example.blogapprestapi.model.entity.Token;
import com.example.blogapprestapi.model.entity.User;
import com.example.blogapprestapi.repository.RoleRepository;
import com.example.blogapprestapi.repository.TokenRepository;
import com.example.blogapprestapi.repository.UserRepository;
import com.example.blogapprestapi.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public String doRegister(RegisterDTO registerDTO, HttpServletRequest request) {

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

        if (registerDTO.getRole() == null || registerDTO.getRole().isEmpty()) {
            Role role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "Role_User", null));
            roles.add(role);
        }
        user.setRoles(roles);
        userRepository.save(user);
        publisher.publishEvent(new RegistrationCompleteEvent(user, application(request)));
        return "User register Success";
    }

    @Override
    public void saveTokenAndUser(String token, User user) {
        Token theToken = new Token(token, user);
        tokenRepository.save(theToken);
    }

    @Override
    public boolean verifyToken(String token) {
        Token theToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new BlogApiException(HttpStatus.BAD_REQUEST, "Token not found!"));

        Calendar calendar = Calendar.getInstance();
        if (theToken.getExpirationTime().getTime() - calendar.getTime().getTime() < 0) {
            tokenRepository.delete(theToken);
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Token Expired!");
        }
        User user = theToken.getUser();
        user.setEnable(true);
        userRepository.save(user);
        return true;
    }

    private String application(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}

