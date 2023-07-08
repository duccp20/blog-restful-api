package com.example.blogapprestapi.service;

import com.example.blogapprestapi.model.dto.request.LoginDTO;

public interface LoginService {
    String doLogin(LoginDTO loginDTO);
}
