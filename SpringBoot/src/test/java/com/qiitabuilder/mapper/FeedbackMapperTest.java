package com.qiitabuilder.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedbackMapperTest {

    @Test
    void insert() {
    }

    @Test
    void update() {
    }

    @Test
    void getFBRank() {
    }

    @Test
    void getArticleIdByUserId() {
    }

    @Test
    void load() {
    }

    @Test
    void getFeedbackedArticlesByUserId() {
    }
}