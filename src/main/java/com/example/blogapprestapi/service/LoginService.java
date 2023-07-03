package com.example.blogapprestapi.service;

import com.example.blogapprestapi.model.dto.LoginDTO;

public interface LoginService {
    String doLogin(LoginDTO loginDTO);
}
