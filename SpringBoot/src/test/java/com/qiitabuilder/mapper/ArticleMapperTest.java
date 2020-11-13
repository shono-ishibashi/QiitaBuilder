package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
class ArticleMapperTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private NamedParameterJdbcOperations jdbcOperations;

    @Test
    void searchArticles() {

    }

    @Test
    void searchArticlesId() {
    }

    @Test
    void getPostedArticleCountRank() {
    }

    @Test
    void findArticleById() {
    }

    @Test
    void getArticleIdListByUserId() {
    }

    @Test
    void insertArticle() {

    }

    @Test
    void updateArticle() {
    }

    @Test
    void getArticleAndFeedback() {
    }

    @Test
    void load() {
    }
}
