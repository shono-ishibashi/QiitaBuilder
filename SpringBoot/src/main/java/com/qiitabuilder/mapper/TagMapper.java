package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagMapper {
    //Tagをすべて取得
    List<Tag> findAll();

    //記事に投稿に関連しているTagのIDを取得
    List<Integer> findAllArticleTag(Integer userId, Integer articleId);

    //記事とTagの関係を追加
    void insertArticleTag(Integer userId, Integer tagId, Integer articleId);

    //記事とTagの関係を削除
    void deleteArticleTag(Integer userId, Integer tagId, Integer articleId);


    Integer insertTag(Tag tag);
}
