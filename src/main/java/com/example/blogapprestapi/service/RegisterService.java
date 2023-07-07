package com.example.blogapprestapi.service;

import com.example.blogapprestapi.model.dto.RegisterDTO;
import com.example.blogapprestapi.model.entity.Token;
import com.example.blogapprestapi.model.entity.User;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface RegisterService {

    String doRegister(RegisterDTO registerDTO, HttpServletRequest httpServletRequest);


    String verifyToken(String token);


    Token generateNewToken(String token);

    void resendVerificationTokenEmail(String token, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException;
}
