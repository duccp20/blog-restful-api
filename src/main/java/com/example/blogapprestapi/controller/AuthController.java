package com.example.blogapprestapi.controller;

import com.example.blogapprestapi.model.dto.LoginDTO;
import com.example.blogapprestapi.model.dto.RegisterDTO;

import com.example.blogapprestapi.model.dto.response.JwtAuthResponse;
import com.example.blogapprestapi.service.LoginService;
import com.example.blogapprestapi.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private RegisterService registerService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> doLogin(@RequestBody LoginDTO loginDTO) {
        String token = loginService.doLogin(loginDTO);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setToken(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }


    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> doRegister(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(registerService.doRegister(registerDTO));
    }
}
