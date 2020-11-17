package com.qiitabuilder.service;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleServiceTest {

    @Test
    void searchArticles() {
    }

    @Test
    void getTotalPage() {
    }

    @Test
    void searchCriteriaProcessing() {
    }

    @Test
    void saveArticle() {
    }

    @Test
    void getArticle() {
    }

    @Test
    void getFeedbackedArticlesByUserId() {
    }

    @Test
    void getMyArticlesByUserId() {
    }
}