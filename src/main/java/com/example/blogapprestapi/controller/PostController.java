package com.example.blogapprestapi.controller;

import com.example.blogapprestapi.model.dto.request.PostDTO;
import com.example.blogapprestapi.model.dto.request.PostDTOv2;
import com.example.blogapprestapi.model.dto.response.PostPaginationResponse;
import com.example.blogapprestapi.service.PostService;
import com.example.blogapprestapi.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    //create post
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO) {
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }

    @GetMapping("/v1/posts")
    public ResponseEntity<PostPaginationResponse> getAllPosts(
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) Integer pageNo,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = Constants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = Constants.DEFAULT_SORT_DIR) String sortDir
    ) {
        return new ResponseEntity<>(postService.getAllPosts(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/v1/posts/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/v2/posts/{id}")
    public ResponseEntity<PostDTOv2> getPostByIdV2(@PathVariable Long id) {
        PostDTO postDTO = postService.getPostById(id);

        PostDTOv2 postDTOv2 = new PostDTOv2();
        postDTOv2.setId(postDTO.getId());
        postDTOv2.setTitle(postDTO.getTitle());
        postDTOv2.setDescription(postDTO.getDescription());
        postDTOv2.setContent(postDTO.getContent());
        postDTOv2.setComments(postDTO.getComments());
        postDTOv2.setCategoryId(postDTO.getCategoryId());

        List<String> tags = new ArrayList<>();
        tags.add("Java");
        tags.add("C++");
        tags.add("Python");
        postDTOv2.setTags(tags);
        return ResponseEntity.ok(postDTOv2);
    }

    @GetMapping("/v1/posts/category/{id}")
    public ResponseEntity<List<PostDTO>> getPostByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostByCategory(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/v1/posts/{id}")
    public ResponseEntity<PostDTO> updatePost(@Valid @PathVariable Long id, @RequestBody PostDTO post) {
        return new ResponseEntity<>(postService.updatePost(post, id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/v1/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(String.format("This post with id = %s has been deleted.", id), HttpStatus.OK);
    }

    @GetMapping("/v1/posts/search")
    public ResponseEntity<List<PostDTO>> searchPosts(
            @RequestParam String query
    ) {
        return ResponseEntity.ok(postService.searchPosts(query));
    }
}
