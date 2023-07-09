package com.example.blogapprestapi.service;

import com.example.blogapprestapi.model.dto.request.PasswordChangeRequest;
import com.example.blogapprestapi.model.dto.request.PasswordResetRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    void sendMailWithToken(PasswordResetRequest mail, HttpServletRequest request);

    String createNewPassword(String token, PasswordResetRequest passwordResetRequest);


    String handleResendTokenForResetPassword(String token);

    String changePasswordByToken(PasswordChangeRequest passwordChangeRequest, HttpServletRequest request);
}
