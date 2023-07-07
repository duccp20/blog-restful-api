package com.example.blogapprestapi.controller;

import com.example.blogapprestapi.model.dto.LoginDTO;
import com.example.blogapprestapi.model.dto.RegisterDTO;
import com.example.blogapprestapi.model.dto.response.JwtAuthResponse;
import com.example.blogapprestapi.model.entity.Token;
import com.example.blogapprestapi.model.entity.User;
import com.example.blogapprestapi.service.LoginService;
import com.example.blogapprestapi.service.RegisterService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private RegisterService registerService;


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> doLogin(@RequestBody LoginDTO loginDTO) {
        String token = loginService.doLogin(loginDTO); //handle trong service
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(); //tạo 1 instance của response
        jwtAuthResponse.setToken(token); //set token cho response
        return ResponseEntity.ok(jwtAuthResponse);
    }


    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> doRegister(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) {
        return ResponseEntity.ok(registerService.doRegister(registerDTO, request));
    }

    @GetMapping("/register/verifyEmail")
    public ResponseEntity<?> verifyToken(@RequestParam String token) {
        String theToken = registerService.verifyToken(token);
        return ResponseEntity.ok(theToken);
    }


    @GetMapping("/register/resend-verification-token")
    public ResponseEntity<?> resendVerificationToken(@RequestParam String token, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        registerService.resendVerificationTokenEmail(token, request);
        return ResponseEntity.ok("verification token has been sent. Please check mail to verify again!");
    }
}
