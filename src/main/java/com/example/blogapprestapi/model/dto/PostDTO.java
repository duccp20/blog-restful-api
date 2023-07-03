package com.example.blogapprestapi.model.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Long id;
    //không đc phép null or empty
    //nhập ít nhất 3 ký tự
    @NotEmpty
    @NotNull
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;

    @NotEmpty
    @NotNull
    @Size(min = 10, message = "Post description should have at least 10 characters")
    private String description;
    @NotEmpty
    @NotNull
    private String content;
    private Set<CommentDTO> comments;

    private Long categoryId;
}
