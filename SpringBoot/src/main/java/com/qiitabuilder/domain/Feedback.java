package com.qiitabuilder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    private Integer feedbackId;
    private Integer userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String content;
    private Integer deleteFlag;
}
