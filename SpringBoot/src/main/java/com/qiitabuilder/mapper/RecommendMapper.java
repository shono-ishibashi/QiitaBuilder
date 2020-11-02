package com.qiitabuilder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RecommendMapper {

    List<Integer> getQiitaRecommendedRank();
    Integer getAllCountByPostedUserId(Integer postedUserId);
    List<Integer> getMostRecommendedArticleId(Integer userId, Integer sortId);
}
