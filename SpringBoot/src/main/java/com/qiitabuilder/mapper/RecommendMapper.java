package com.qiitabuilder.mapper;

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
     * Recommendを新規作成する
     * @param recommend
     */
    void insert(Recommend recommend);

    /**
     * 自動採番されたプライマリーキーの値を取得する
     * @return 自動採番の値
     */
    Integer getAutoIncrementKey();
}
