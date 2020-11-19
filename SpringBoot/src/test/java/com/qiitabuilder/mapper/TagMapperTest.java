package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@MybatisTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TagMapperTest {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    private void beforeEach(){
        jdbcTemplate.execute("create table tags\n" +
                "(\n" +
                "   tag_id   int auto_increment\n" +
                "       primary key,\n" +
                "   tag_name text null\n" +
                ");\n");
    }

    @AfterEach
    private void beforeAfter() {
        jdbcTemplate.execute("DROP TABLE tags");
    }


    @Test
    void findAll_正常系() {
//        DBにtagsをinsert
        String[] tagsSqlArr = CollectionSQL.insertTags.split("\n", 0);
        for(String sql:tagsSqlArr){
            jdbcTemplate.execute(sql);
        }
        List<Tag> tags=tagMapper.findAll();
        assertEquals(5,tags.get(0).getTagId());
        assertEquals(1,tags.get(1).getTagId());
        assertEquals(3,tags.get(2).getTagId());
        assertEquals(4,tags.get(3).getTagId());
        assertEquals(2,tags.get(4).getTagId());
        assertEquals("go",tags.get(0).getTagName());
        assertEquals("Java",tags.get(1).getTagName());
        assertEquals("javascript",tags.get(2).getTagName());
        assertEquals("php",tags.get(3).getTagName());
        assertEquals("ruby",tags.get(4).getTagName());
    }

    @Test
    void postTag() {
    }

    @Test
    void findAllArticleTag() {
    }

    @Test
    void findByUserId() {
    }

    @Test
    void insertArticleTag() {
    }

    @Test
    void deleteArticleTag() {
    }

    @Test
    void insertTag() {
    }
}