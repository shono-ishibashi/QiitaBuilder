package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleMapper {

    List<Integer> getPostedArticleCountRank();
    Integer getCountByUserId(Integer userId);
    Integer insertArticle(Article article);
    void updateArticle(Article article);
}
