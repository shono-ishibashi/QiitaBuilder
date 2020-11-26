package com.qiitabuilder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article {
    private Integer articleId;

    private User postedUser;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String title;

    private String content;

    private Integer stateFlag;

    private Integer articleVersion;

    private String qiitaArticleId;

    private List<Tag> tags;

    private List<Feedback> feedbacks;

    private Integer qiitaRecommendPoint;

    private Integer registeredMyArticleCount;

    private Integer feedbackCount;
}
