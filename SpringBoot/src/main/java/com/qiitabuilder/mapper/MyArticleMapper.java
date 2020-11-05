package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.qiitabuilder.domain.MyArticle;

@Mapper
@Repository
public interface MyArticleMapper {
    /**
     * MyArticleに紐づいたArticleのレコードを取得する
     *
     * @param userId
     * @return
     */
    List<Article> getMyArticlesByUserId(@Param("userId") Integer userId);

    /**
     * 検索条件に一致するレコードを取得する
     *
     * @param articleId      　記事ID
     * @param registerUserId 　My記事登録したユーザーID
     * @return 検索条件に一致したレコード
     */
    MyArticle findByArticleIdAndRegisterUserId(Integer articleId, Integer registerUserId);

    /**
     * MyArticleを新規作成する
     *
     * @param myArticle 　My記事情報
     */
    void insert(MyArticle myArticle);

    /**
     * 自動採番されたプライマリーキーの値を取得する
     *
     * @return 自動採番の値
     */
    Integer getAutoIncrementKey();

    /**
     * MyArticleを削除する
     *
     * @param myArticleId My記事ID
     * @return テーブルに１レコード以上の削除があった場合はtrue、１件も無かった場合はfalseを返す
     */
    boolean delete(Integer myArticleId);

}
