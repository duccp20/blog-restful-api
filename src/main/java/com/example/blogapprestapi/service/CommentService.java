package com.example.blogapprestapi.service;

import com.example.blogapprestapi.model.dto.request.CommentDTO;

import java.util.List;

public interface CommentService {

    CommentDTO createComment(Long postId, CommentDTO commentDTO);
    List<CommentDTO> findByPostId(Long postId);

    CommentDTO getCommentById(Long postId, Long commentId);

    CommentDTO updateCommentById(Long postId, Long commentId, CommentDTO commentDTO);

    void deleteCommentById(Long postId, Long commentId);
}
