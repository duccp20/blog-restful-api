package com.example.blogapprestapi.model.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenResponse {
    private Long id;
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private String username;
    private String email;
    private List<String> roles;
}
