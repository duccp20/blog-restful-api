package com.example.blogapprestapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;
    private String body;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn
            (name = "post_id", nullable = false)
    private Post post;
}
