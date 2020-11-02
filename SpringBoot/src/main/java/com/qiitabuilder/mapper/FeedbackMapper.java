package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FeedbackMapper {
//    void insert(Feedback feedback);
//    Integer getAutoIncrementKey();
//    void update(Feedback feedback);
    List<Integer> getFBRank();
    List<Integer> getArticleIdByUserId(Integer userId);
    Integer getCount(Integer userId);
}
