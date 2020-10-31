package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MyArticleMapper {
    List<Article> getMyArticlesByUserId(@Param("userId") Integer userId);
}
