package com.example.blogapprestapi.service;

import com.example.blogapprestapi.mail.EmailDetails;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailDetailsService {
    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details) throws MessagingException, UnsupportedEncodingException;
}
