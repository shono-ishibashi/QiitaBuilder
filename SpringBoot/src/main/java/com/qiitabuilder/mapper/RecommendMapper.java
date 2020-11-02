package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.MyArticle;
import com.qiitabuilder.domain.Recommend;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RecommendMapper {

    List<Integer> getQiitaRecommendedRank();
    Integer getAllCountByPostedUserId(Integer postedUserId);
    List<Integer> getMostRecommendedArticleId(Integer userId, Integer sortId);


    /**
     * 検索条件に一致するレコードを取得する
     * @param articleId
     * @param recommendUserId
     * @return 検索条件に一致したレコード
     */
    Recommend findByArticleIdAndRecommendUserId(Integer articleId, Integer recommendUserId);

    /**
     * Recommendを新規作成する
     * @param recommend
     */
    void insert(Recommend recommend);

    /**
     * 自動採番されたプライマリーキーの値を取得する
     * @return 自動採番の値
     */
    Integer getAutoIncrementKey();

    /**
     * Recommendを削除する
     * @param recommendId
     */
    void delete(Integer recommendId);
}
