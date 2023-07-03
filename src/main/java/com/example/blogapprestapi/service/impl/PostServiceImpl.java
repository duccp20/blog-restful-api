package com.example.blogapprestapi.service.impl;

import com.example.blogapprestapi.exception.ResourceNotFoundException;
import com.example.blogapprestapi.model.dto.PostDTO;
import com.example.blogapprestapi.model.dto.response.PostPaginationResponse;
import com.example.blogapprestapi.model.entity.Category;
import com.example.blogapprestapi.model.entity.Post;
import com.example.blogapprestapi.repository.CategoryRepository;
import com.example.blogapprestapi.repository.CommentRepository;
import com.example.blogapprestapi.repository.PostRepository;
import com.example.blogapprestapi.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;
    @Override
    public PostDTO createPost(PostDTO postDTO) {

        Category category = categoryRepository.findById(postDTO.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", postDTO.getCategoryId())
        );
        Post postEntity = mapToEntity(postDTO);
        postEntity.setCategory(category);
        postEntity = postRepository.save(postEntity);
        return mapToDTO(postEntity);
    }

    @Override
    public PostPaginationResponse getAllPosts(Integer pageNo, Integer pageSize, String softBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(softBy).ascending(): Sort.by(softBy).descending();
        //Pageable chứa các thông tin vị trí trang được lấy, số phần tử
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        //kiểu trả về là Object Page
        Page<Post> posts = postRepository.findAll(pageable);

        //lấy ra list post
        List<Post> listOfPosts = posts.getContent();

        //convert to list dto
        List<PostDTO> content = listOfPosts.stream().map(this::mapToDTO).collect(Collectors.toList());
        //post -> mapToDTO(post) = this::maptoDTO

        return PostPaginationResponse.builder()
                .content(content)
                .pageNo(posts.getNumber())
                .pageSize(posts.getSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .last(posts.isLast())
                .build();
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", "id", id));
        Category category = categoryRepository.findById(postDTO.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", postDTO.getCategoryId())
        );
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setDescription(postDTO.getDescription());
        post.setCategory(category);
        post = postRepository.save(post);
        return mapToDTO(post);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", "id", id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDTO> getPostByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        List<Post> posts = postRepository.findByCategoryId(categoryId);
        return posts.stream().map(post -> mapper.map(post, PostDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> searchPosts(String query) {
        List<Post> posts = postRepository.searchPostByNative(query);
        return posts.stream().map(post -> mapper.map(post, PostDTO.class)).collect(Collectors.toList());
    }


    //convert Entity to DTO
    public PostDTO mapToDTO(Post post) {
//        PostDTO postResponse = new PostDTO();
//        postResponse.setId(post.getId());
//        postResponse.setTitle(post.getTitle());
//        postResponse.setContent(post.getContent());
//        postResponse.setDescription(post.getDescription());
//        return postResponse;
        return mapper.map(post, PostDTO.class);
    }

    //convert Entity to DTO
    public Post mapToEntity(PostDTO postDTO) {
//        Post post = new Post();
//        post.setTitle(postDTO.getTitle());
//        post.setContent(postDTO.getContent());
//        post.setDescription(postDTO.getDescription());
        return mapper.map(postDTO, Post.class);
    }
}
