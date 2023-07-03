package com.example.blogapprestapi.repository;

import com.example.blogapprestapi.model.entity.Category;
import com.example.blogapprestapi.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCategoryId(Long categoryId);

    @Query("select p FROM  Post p where p.title like CONCAT('%', :query, '%')")
    List<Post> searchPost(String query);

    @Query(value = "select * FROM  posts p where p.title like  CONCAT('%', :query, '%')", nativeQuery = true)
    List<Post> searchPostByNative(String query);

}
