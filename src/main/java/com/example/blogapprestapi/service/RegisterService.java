package com.example.blogapprestapi.service;

import com.example.blogapprestapi.model.dto.RegisterDTO;
import com.example.blogapprestapi.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface RegisterService {

    String doRegister(RegisterDTO registerDTO, HttpServletRequest httpServletRequest);

    void saveTokenAndUser(String token, User user);

    boolean verifyToken(String token);
}
