package com.example.blogapprestapi.model.dto.response;

import com.example.blogapprestapi.model.dto.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPaginationResponse {

    private List<PostDTO> content;
    private Integer pageNo;
    private Integer pageSize;

    private Integer totalPages;

    private Long totalElements;

    private boolean last;
}
