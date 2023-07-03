package com.example.blogapprestapi.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    private String resource;
    private String name;
    private Long value;
    private HttpStatus status = HttpStatus.NOT_FOUND;


    public ResourceNotFoundException(String resource, String name, Long value) {
        super(String.format("%s Not Found With Id :%s: %s", resource, name, value));
        //ví dụ: Post not found with id: 1
        this.resource = resource;
        this.name = name;
        this.value = value;
    }
}
