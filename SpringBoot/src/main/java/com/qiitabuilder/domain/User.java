package com.qiitabuilder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Integer userId;
    private String password;
    private List<Article> postArticles;
    private String displayName;
    private String photoUrl;
    private List<Feedback> feedbacks;
    private List<Article> myArticles;
    private String uid;
    private Integer feedbackCount;
    private Integer postedArticleCount;
    private Integer qiitaRecommendedAllCount;
    private List<Tag> usedTags;
    private Boolean isLoginUser;
}
