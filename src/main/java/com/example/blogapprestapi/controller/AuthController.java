package com.example.blogapprestapi.controller;

import com.example.blogapprestapi.model.dto.request.*;
import com.example.blogapprestapi.model.dto.response.JwtTokenResponse;
import com.example.blogapprestapi.model.dto.response.SendMailResponse;
import com.example.blogapprestapi.service.LoginService;
import com.example.blogapprestapi.service.RefreshTokenService;
import com.example.blogapprestapi.service.RegisterService;
import com.example.blogapprestapi.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(loginService.doLogin(loginDTO));
    }

    @PostMapping("/refresh-token")
    public  ResponseEntity<?> doRefreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(refreshTokenService.doRefreshToken(request));
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


    @PostMapping("/password-reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest, HttpServletRequest request) {
        userService.sendMailWithToken(passwordResetRequest, request);
        return ResponseEntity.status(HttpStatus.OK).body(

                SendMailResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Gửi mail thành công! Vui lòng check mail để xác thực")
                        .build()
        );
    }

    @Transactional
    @PostMapping("/password-reset/create-new-password")
    public ResponseEntity<String> createNewPassword(@RequestParam String token,
                                                 @RequestBody PasswordResetRequest passwordResetRequest) {
        return ResponseEntity.ok(userService.createNewPassword(token, passwordResetRequest));
    }

    @PostMapping("/password-reset/resend-token")
    public ResponseEntity<String> resendTokenForResetPassword(@RequestParam String token) {
        return ResponseEntity.ok(userService.handleResendTokenForResetPassword(token));
    }


    @PostMapping("/change-password")
    public ResponseEntity<String> changePasswordByToken(@RequestBody PasswordChangeRequest passwordChangeRequest,
                                                   HttpServletRequest request) {
        return ResponseEntity.ok(userService.changePasswordByToken(passwordChangeRequest, request));
    }
}
