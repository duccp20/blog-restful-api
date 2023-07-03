package com.example.blogapprestapi.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorMessage {
    private HttpStatus httpStatus;
    private String message;

    private String details;
}
