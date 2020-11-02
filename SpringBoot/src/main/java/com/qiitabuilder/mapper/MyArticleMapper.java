package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.MyArticle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyArticleMapper {

    /**
     * 検索条件に一致するレコードを取得する
     * @param articleId
     * @param registerUserId
     * @return 検索条件に一致したレコード
     */
    MyArticle findByArticleIdAndRegisterUserId(Integer articleId, Integer registerUserId);

    /**
     * MyArticleを新規作成する
     * @param myArticle
     */
    void insert(MyArticle myArticle);

    /**
     * 自動採番されたプライマリーキーの値を取得する
     * @return 自動採番の値
     */
    Integer getAutoIncrementKey();

    /**
     * MyArticleを削除する
     * @param myArticleId
     */
    void delete(Integer myArticleId);

}
