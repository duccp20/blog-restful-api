package com.example.blogapprestapi.service.impl;

import com.example.blogapprestapi.mail.EmailDetails;
import com.example.blogapprestapi.service.EmailDetailsService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailDetailsServiceImpl implements EmailDetailsService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendSimpleMail(EmailDetails details) {
        return null;
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(sender, details.getSenderName());
        messageHelper.setTo(details.getMailRecipient());
        messageHelper.setText(details.getContent(), true);
        messageHelper.setSubject(details.getSubject());

        // Sending the mail
        mailSender.send(message);
        return "Mail Sent Successfully...";
    }
}
