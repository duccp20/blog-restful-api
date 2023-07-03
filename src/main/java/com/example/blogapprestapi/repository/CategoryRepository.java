package com.example.blogapprestapi.repository;

import com.example.blogapprestapi.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Boolean existsByName (String name);
}
