package com.example.blogapprestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalHandlerException {

    //handle specific exception e.g. EntityNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handlerResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        ErrorMessage  errorMessage = new ErrorMessage(exception.getStatus(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BlogApiException.class)
    public ResponseEntity<ErrorMessage> handlerBlogApiException(BlogApiException exception, WebRequest webRequest) {
        ErrorMessage  errorMessage = new ErrorMessage(exception.getStatus(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    //handle global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handlerBlogApiException(Exception exception, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorMessage.builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message(exception.getMessage())
                        .details(webRequest.getDescription(false))
                        .build());
    }

    //handle validate response
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerBlogApiException(MethodArgumentNotValidException exception, WebRequest webRequest) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) ->{
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    //Lưu ý chọn đúng class security.access thay vì .java
//    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
//    public ResponseEntity<ErrorMessage> handlerAccessDeniedException(AccessDeniedException exception, WebRequest webRequest) {
//        ErrorMessage  errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED, exception.getMessage(), webRequest.getDescription(false));
//        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
//    }

}
