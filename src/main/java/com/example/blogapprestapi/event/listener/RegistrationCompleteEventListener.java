package com.example.blogapprestapi.event.listener;

import com.example.blogapprestapi.event.RegistrationCompleteEvent;
import com.example.blogapprestapi.mail.EmailDetails;
import com.example.blogapprestapi.model.entity.Token;
import com.example.blogapprestapi.model.entity.User;
import com.example.blogapprestapi.repository.TokenRepository;
import com.example.blogapprestapi.service.EmailDetailsService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {


    @Autowired
    private TokenRepository tokenRepository;



    @Autowired
    private EmailDetailsService emailDetailsService;

    private User theUser;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        theUser = event.getUser();
        //tạo token
        String token = UUID.randomUUID().toString();
        //lưu token vào db kèm user
        Token tokenAndUser = new Token(token, theUser);
        tokenRepository.save(tokenAndUser);
        //gửi mail kèm theo link (link chứa token)
        String url = event.getUrl() + "/api/v1/auth/register/verifyEmail?token=" + token;
        try {
            EmailDetails emailDetails = EmailDetails
                    .builder()
                    .subject("Email Verification")
                    .senderName("User Registration Portal Service")
                    .mailRecipient(theUser.getEmail())
                    .content("<p> Hi, " + theUser.getName() + "! </p>" +
                            "<p>Thank you for registering with us. </p>" +
                            "<p>Please, follow the link below to complete your registration.</p>" +
                            "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                            "<p>Thank you! <br> Users Registration Portal Service </p>")
                    .build();
            emailDetailsService.sendMailWithAttachment(emailDetails);
            log.info("Vui lòng check email: " + url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }
}


