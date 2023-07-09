//package com.example.blogapprestapi.model.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.Instant;
//import java.util.Date;
//
//@Setter
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class RefreshToken {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String token;
//
//    @Column(nullable = false)
//    private Date expiryDate;
//    @OneToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user;
//
//
//
//
//}
