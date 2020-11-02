package com.qiitabuilder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleMapper {

    List<Integer> getPostedArticleCountRank();
    Integer getCountByUserId(Integer userId);
}
