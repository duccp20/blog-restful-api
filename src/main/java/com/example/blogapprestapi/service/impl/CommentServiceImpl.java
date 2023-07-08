package com.example.blogapprestapi.service.impl;

import com.example.blogapprestapi.exception.BlogApiException;
import com.example.blogapprestapi.exception.ResourceNotFoundException;
import com.example.blogapprestapi.model.dto.request.CommentDTO;
import com.example.blogapprestapi.model.entity.Comment;
import com.example.blogapprestapi.model.entity.Post;
import com.example.blogapprestapi.repository.CommentRepository;
import com.example.blogapprestapi.repository.PostRepository;
import com.example.blogapprestapi.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;


    @Autowired
    private ModelMapper mapper;


    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        //get id post
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId));

        //entity
        Comment comment = mapToEntity(commentDTO);
        //set post cho comment
        comment.setPost(post);
        //save
        commentRepository.save(comment);

        //return dto
        return mapToDTO(comment);
    }

    @Override
    public List<CommentDTO> findByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("comment", "id", commentId));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "This comment doesn't belong to a post with an ID" + postId);
        }
        return mapToDTO(comment);
    }

    @Override
    public CommentDTO updateCommentById(Long postId, Long commentId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("comment", "id", commentId));
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "This comment doesn't belong to a post with an ID" + postId);
        }

        // update
        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());

        commentRepository.save(comment);
        return mapToDTO(comment);
    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("comment", "id", commentId));
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "This comment doesn't belong to a post with an ID" + postId);
        }
        commentRepository.delete(comment);
    }

    //map to DTO
    public CommentDTO mapToDTO(Comment comment) {
//        return CommentDTO
//                .builder()
//                .id(comment.getId())
//                .name(comment.getName())
//                .email(comment.getEmail())
//                .body(comment.getBody())
//                .build();
        return mapper.map(comment, CommentDTO.class);
    }

    //map to entity

    public Comment mapToEntity(CommentDTO commentDTO) {
//        return Comment
//                .builder()
//                .name(commentDTO.getName())
//                .email(commentDTO.getEmail())
//                .body(commentDTO.getBody())
//                .build();
        return mapper.map(commentDTO, Comment.class);
    }

}
