package com.qiitabuilder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyArticle {
    private Integer myArticleId;
    private Integer articleId;
    private Integer postedUserId;
    private Integer registerUserId;
}
