package com.qiitabuilder.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MyArticleMapperTest {

    @Test
    void getMyArticlesByUserId() {
    }

    @Test
    void findByArticleIdAndRegisterUserId() {
    }

    @Test
    void insert() {
    }

    @Test
    void getAutoIncrementKey() {
    }

    @Test
    void delete() {
    }
}