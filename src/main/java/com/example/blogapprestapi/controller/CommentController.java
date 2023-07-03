package com.example.blogapprestapi.controller;

import com.example.blogapprestapi.model.dto.CommentDTO;
import com.example.blogapprestapi.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDTO), HttpStatus.CREATED);
    }


    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.findByPostId(postId));
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long postId, @PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.getCommentById(postId, commentId), HttpStatus.OK);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateCommentById(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid  @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.updateCommentById
                (postId, commentId, commentDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentById(
            @PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteCommentById(postId, commentId);
        return new ResponseEntity<>("This comment have already been deleted.", HttpStatus.OK);
    }

}
