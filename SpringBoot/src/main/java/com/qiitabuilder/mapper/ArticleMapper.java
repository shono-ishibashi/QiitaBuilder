package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper {
    Integer insertArticle(Article article);
    void updateArticle(Article article);
}
