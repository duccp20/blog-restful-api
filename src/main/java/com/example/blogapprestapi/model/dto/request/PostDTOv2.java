package com.example.blogapprestapi.model.dto.request;

import com.example.blogapprestapi.model.dto.request.CommentDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTOv2 {
    private Long id;
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
    private List<String> tags;
}
