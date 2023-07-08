package com.example.blogapprestapi.model.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.net.http.HttpResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMailResponse {
    private HttpStatus httpStatus;
    private String message;
}
