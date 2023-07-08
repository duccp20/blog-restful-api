package com.example.blogapprestapi.service.impl;

import com.example.blogapprestapi.exception.BlogApiException;
import com.example.blogapprestapi.exception.ResourceNotFoundException;
import com.example.blogapprestapi.mail.EmailDetails;
import com.example.blogapprestapi.model.dto.request.PasswordResetRequest;
import com.example.blogapprestapi.model.dto.response.SendMailResponse;
import com.example.blogapprestapi.model.entity.PasswordResetToken;
import com.example.blogapprestapi.model.entity.User;
import com.example.blogapprestapi.repository.PasswordResetTokenRepository;
import com.example.blogapprestapi.repository.UserRepository;
import com.example.blogapprestapi.security.jwt.JwtAuthProvider;
import com.example.blogapprestapi.service.EmailDetailsService;
import com.example.blogapprestapi.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.UUID;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailDetailsService emailDetailsService;

    @Autowired
    private HttpServletRequest servletRequest;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void sendMailWithToken(PasswordResetRequest mail, HttpServletRequest request) {

        User user = userRepository.findUserByEmail(mail.getEmail())
                .orElseThrow(() -> new BlogApiException(HttpStatus.BAD_REQUEST, "User not found with username or email"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);

        String url = applicationUrl(request) + "/api/v1/auth/password-reset/create-new-password?token=" + token;
        try {
            EmailDetails emailDetails = EmailDetails
                    .builder()
                    .subject("Đổi mật khẩu")
                    .senderName("Blog For Dev")
                    .mailRecipient(mail.getEmail())
                    .content("<p> Hi, " + user.getName() + "! </p>" +
                            "<p>Please, follow the link below to continue set new password </p>" +
                            "<a href=\"" + url + "\">Click here</a>" +
                            "<p>Thank you! <br> Blog For Dev </p>")
                    .build();
            emailDetailsService.sendMailWithAttachment(emailDetails);
            log.info("url for reset password: " + url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    @Transactional
    public String createNewPassword(String token, PasswordResetRequest passwordResetRequest) {
        String url = applicationUrl(servletRequest) + "/api/v1/auth/password-reset";
        //compare token with token in db
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).
                orElseThrow(() -> new BlogApiException(HttpStatus.BAD_REQUEST, "TOKEN NOT FOUND IN SYSTEM!"));

        //validate expiration
        Calendar calendar = Calendar.getInstance();
        if (!(passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() >= 0)) {
            log.info("URL RESEND VERIFY PASSWORD!");
            return "Đã hết hạn thời gian đổi mật khẩu, vui lòng click vào link sau để làm lại " +
                    "<a href=\"" + url + "\">Verify your email to activate your account</a>";

        }
        //check password and confirm password
        if (!passwordResetRequest.getNewPassword().equals(passwordResetRequest.getConfirmPassword())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "NEW PASSWORD AND CONFIRM PASSWORD NOT VALID!");
        }

        //check new pass and old pass
        if(checkNewPassAndOldPass(passwordResetRequest.getNewPassword(), passwordResetToken.getUser())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "New Password equal to Old Password!");
        }
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.deleteByToken(token);
        return "Đã đổi mật khẩu thành công!";
        }


    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    private boolean checkNewPassAndOldPass(String newPass, User user) {
        return passwordEncoder.matches(newPass, user.getPassword());
    }
}
