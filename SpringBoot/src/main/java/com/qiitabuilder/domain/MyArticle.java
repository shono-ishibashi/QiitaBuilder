package com.qiitabuilder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyArticle {
    private Integer myArticleId;
    private Integer articleId;
    private Integer postedUserId;
    private Integer registerUserId;
}
