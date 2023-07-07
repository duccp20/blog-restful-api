package com.example.blogapprestapi.repository;

import com.example.blogapprestapi.model.entity.Token;
import com.example.blogapprestapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

}
