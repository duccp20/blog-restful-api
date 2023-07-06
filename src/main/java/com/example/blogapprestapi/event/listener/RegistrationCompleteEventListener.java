package com.example.blogapprestapi.event.listener;

import com.example.blogapprestapi.event.RegistrationCompleteEvent;
import com.example.blogapprestapi.exception.BlogApiException;
import com.example.blogapprestapi.model.entity.User;
import com.example.blogapprestapi.service.RegisterService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {


    @Autowired
    private RegisterService registerService;

    @Autowired
    private JavaMailSender mailSender;

    private User theUser;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        theUser = event.getUser();
        //tạo token
        String token = UUID.randomUUID().toString();
        //lưu token vào db kèm user
        registerService.saveTokenAndUser(token, theUser);
        //gửi mail kèm theo link (link chứa token)
        String url = event.getUrl()+"/api/v1/auth/register/verifyEmail?token="+token;
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Vui lòng check email" + url);
    }


    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, "+ theUser.getName()+ "! </p>"+
                "<p>Thank you for registering with us. </p>" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p>Thank you! <br> Users Registration Portal Service";


        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("phuocduc2k3@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

}
