package com.qiitabuilder.service;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RankingUserServiceTest {

    @Test
    void fetchFBCountRank() {
    }

    @Test
    void fetchArticleCountRank() {
    }

    @Test
    void fetchQiitaCountRank() {
    }

    @Test
    void setRelationArticle() {
    }

    @Test
    void setRank() {
    }
}