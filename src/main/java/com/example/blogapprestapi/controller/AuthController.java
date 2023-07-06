package com.example.blogapprestapi.controller;
import com.example.blogapprestapi.model.dto.LoginDTO;
import com.example.blogapprestapi.model.dto.RegisterDTO;
import com.example.blogapprestapi.model.dto.response.JwtAuthResponse;
import com.example.blogapprestapi.service.LoginService;
import com.example.blogapprestapi.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> doRegister(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) {
        return ResponseEntity.ok(registerService.doRegister(registerDTO, request));
    }

    @GetMapping("/register/verifyEmail")
       public ResponseEntity<?> verifyToken(@RequestParam String token) {
        boolean theToken = registerService.verifyToken(token);
        if (theToken) {
            return ResponseEntity.ok("Tài khoản đã được xác thực, vui lòng login!");
        }

        return ResponseEntity.ok("Tài khoản xác thật thất bại!");
    }

}
