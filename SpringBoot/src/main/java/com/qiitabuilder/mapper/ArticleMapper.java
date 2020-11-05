package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.RankingUser;
import com.qiitabuilder.form.SearchArticleForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleMapper {

    /**
     * 検索条件に一致する記事一覧を取得.
     *
     * @param searchArticleForm フォーム
     * @return 記事情報一覧
     */
    List<Article> searchArticles(@Param("search") SearchArticleForm searchArticleForm);

    /**
     * 総ページ数を取得する.
     *
     * @param searchArticleForm フォーム
     * @return 総ページ数
     */
    Integer getTotalPage(@Param("search") SearchArticleForm searchArticleForm);

    /**
     * 投稿数ランキング順でユーザー情報を取得する.
     *
     * @return ランキングユーザー一覧
     */
    List<RankingUser> getPostedArticleCountRank();

    /**
     * 記事情報を取得する.
     *
     * @param articleId 記事ID
     * @return 記事情報
     */
    List<Article> findArticleById(Integer articleId);

    /**
     * 指定ユーザーの投稿記事ID一覧を取得する.
     *
     * @param userId ユーザーID
     * @return 記事ID一覧
     */
    List<Integer> getArticleIdListByUserId(Integer userId);

//    /**
//     * 投稿数を取得する.
//     *
//     * @param userId ユーザーID
//     * @return 投稿数
//     */
//    Integer getCountByUserId(Integer userId);

    /**
     * 記事を投稿する.
     *
     * @param article 記事情報
     * @return 記事ID（自動採番）
     */
    Integer insertArticle(Article article);

    /**
     * 記事を更新する.
     *
     * @param article 記事情報
     */
    void updateArticle(Article article);

    /**
     * 記事IDを基に記事情報を取得する
     *
     * @param articleId 記事ID
     * @return 記事情報
     */
    Article load(Integer articleId);
}
