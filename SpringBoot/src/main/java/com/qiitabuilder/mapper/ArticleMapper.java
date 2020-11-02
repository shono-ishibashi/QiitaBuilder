package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.form.SearchArticleForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleMapper {

    /**
     * 検索条件に一致する記事一覧を取得
     * @param searchArticleForm
     * @return
     */
    List<Article> searchArticles(@Param("search") SearchArticleForm searchArticleForm);

    /**
     * 総ページ数を取得する
     *
     * @param searchArticleForm
     * @return
     */
    Integer getTotalPage(@Param("search") SearchArticleForm searchArticleForm);
}
