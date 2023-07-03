package com.example.blogapprestapi.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    @NotEmpty(message = "Name should not be empty or null")
    private String name;
    @NotEmpty(message = "Email should not be empty or null")
    @Email(message = "email address is not valid")
    private String email;
    @NotEmpty(message = "Body should not be empty or null")
    @Size(min = 10, message = "Body should not be less than 10 characters")
    private String body;
}
