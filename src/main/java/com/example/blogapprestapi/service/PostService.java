package com.example.blogapprestapi.service;

import com.example.blogapprestapi.model.dto.PostDTO;
import com.example.blogapprestapi.model.dto.response.PostPaginationResponse;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);

    PostPaginationResponse getAllPosts(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    PostDTO getPostById(Long id);

    PostDTO updatePost(PostDTO post, Long id);

    void deletePost(Long id);

    List<PostDTO> getPostByCategory(Long categoryId);

    List<PostDTO> searchPosts(String query);
}
