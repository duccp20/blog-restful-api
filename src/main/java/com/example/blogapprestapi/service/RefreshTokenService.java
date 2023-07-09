package com.example.blogapprestapi.service;


import jakarta.servlet.http.HttpServletRequest;


public interface RefreshTokenService {

    Object doRefreshToken(HttpServletRequest request);
}
