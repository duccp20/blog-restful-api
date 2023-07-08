package com.example.blogapprestapi.service.impl;

import com.example.blogapprestapi.event.RegistrationCompleteEvent;
import com.example.blogapprestapi.exception.BlogApiException;
import com.example.blogapprestapi.exception.ResourceNotFoundException;
import com.example.blogapprestapi.mail.EmailDetails;
import com.example.blogapprestapi.model.dto.request.RegisterDTO;
import com.example.blogapprestapi.model.entity.Role;
import com.example.blogapprestapi.model.entity.Token;
import com.example.blogapprestapi.model.entity.User;
import com.example.blogapprestapi.repository.RoleRepository;
import com.example.blogapprestapi.repository.TokenRepository;
import com.example.blogapprestapi.repository.UserRepository;
import com.example.blogapprestapi.service.EmailDetailsService;
import com.example.blogapprestapi.service.RegisterService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private EmailDetailsService emailDetailsService;

    @Autowired
    private HttpServletRequest servletRequest;

    @Override
    public String doRegister(RegisterDTO registerDTO, HttpServletRequest request) {

        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "ALREADY REGISTERED USER NAME");
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "ALREADY REGISTERED EMAIL");
        }

        User user = new User();
        user.setName(registerDTO.getName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setUsername(registerDTO.getUsername());

        Set<Role> roles = new HashSet<>();

        if (registerDTO.getRole() == null || registerDTO.getRole().isEmpty()) {
            Role role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "Role_User", null));
            roles.add(role);
        }
        user.setRoles(roles);
        userRepository.save(user);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Tạo tài khoản thành công, vui lòng verify account thông qua email đã được gửi để login!";
    }

    @Override
    public String verifyToken(String token) {
        //servlet request là autowird HttpServler ở trên
        String url = applicationUrl(servletRequest) + "/api/v1/auth/register/resend-verification-token?token=" + token;
        Token theToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new BlogApiException(HttpStatus.BAD_REQUEST, "Token not found!"));

        if (theToken.getUser().isEnable()) {
            return "Account này đã được xác thực!";
        }
        Calendar calendar = Calendar.getInstance();
        if (theToken.getExpirationTime().getTime() - calendar.getTime().getTime() < 0) {
            log.info("Resend link: " + url);
            return "Token đã hết hạn, bạn có thể click vào link sau để yêu cầu gửi xác thực lại! " +
                    "<a href=\"" + url + "\">Verify your email to activate your account</a>";
        }
        User user = theToken.getUser();
        user.setEnable(true);
        userRepository.save(user);
        return "Xác thực thành công, bạn đã có thể login!";
    }


    @Override
    public Token generateNewToken(String token) {
        Token theToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new BlogApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy token")
        );
        theToken.setToken(UUID.randomUUID().toString());
        theToken.setExpirationTime(new Token().getExpirationTime());
        tokenRepository.save(theToken);
        return theToken;
    }

    @Override
    public void resendVerificationTokenEmail(String token, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Token newToken = generateNewToken(token);
        resendEmail(newToken.getToken(), applicationUrl(request));

    }

    private void resendEmail(String token, String applicationUrl) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl + "/api/v1/auth/register/verifyEmail?token=" + token;
        Token userFromToken = tokenRepository.findByToken(token).orElseThrow(() -> new BlogApiException(
                HttpStatus.BAD_REQUEST, "This token not found"));
        try {
            EmailDetails emailDetails = EmailDetails
                    .builder()
                    .subject("Email Verification")
                    .senderName("User Registration Portal Service")
                    .mailRecipient(userFromToken.getUser().getEmail())
                    .content("<p> Hi, " + userFromToken.getUser().getName() + "! </p>" +
                            "<p>Thank you for registering with us. </p>" +
                            "Please, follow the link below to complete your registration.</p>" +
                            "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                            "<p>Thank you! <br> Users Registration Portal Service")
                    .build();
            emailDetailsService.sendMailWithAttachment(emailDetails);
            log.info("Vui lòng check email: " + url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}

