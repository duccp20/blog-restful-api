package com.example.blogapprestapi.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {

    private String senderName;
    private String mailRecipient;
    private String content;
    private String subject;
    private String attachment;
}
