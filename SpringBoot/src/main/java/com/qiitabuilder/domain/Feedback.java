package com.qiitabuilder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {
    private Integer feedbackId;
    private Integer articleId;
    private User postedUser;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String content;
    private Integer deleteFlag;
}
