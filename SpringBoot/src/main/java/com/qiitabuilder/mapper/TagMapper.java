package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagMapper {
    List<Tag> findAll();
    void postTag(Tag tag);
    List<Tag> findByUserId(Integer userId);

    void insertArticleTag(Article article);

    void updateArticleTag(Article article);

    void deleteArticleTag(Article article);


    Integer insertTag(Tag tag);
}
