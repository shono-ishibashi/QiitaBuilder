package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleMapperTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    private void beforeEach() {
        jdbcTemplate.execute("ALTER TABLE qiita_builder_for_test.articles auto_increment = 1; ");
        jdbcTemplate.execute("ALTER TABLE qiita_builder_for_test.articles_tags_relations auto_increment = 1; ");
        jdbcTemplate.execute("ALTER TABLE qiita_builder_for_test.feedbacks auto_increment = 1; ");
        jdbcTemplate.execute("ALTER TABLE qiita_builder_for_test.my_articles auto_increment = 1; ");
        jdbcTemplate.execute("ALTER TABLE qiita_builder_for_test.qiita_configurations auto_increment = 1; ");
        jdbcTemplate.execute("ALTER TABLE qiita_builder_for_test.qiita_recommends auto_increment = 1; ");
        jdbcTemplate.execute("ALTER TABLE qiita_builder_for_test.tags auto_increment = 1; ");
        jdbcTemplate.execute("ALTER TABLE qiita_builder_for_test.users auto_increment = 1; ");
    }

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
    void insertArticleTest() {
        //投稿者ユーザー
        User user = new User();
        user.setUserId(null);

        Tag tag1 = new Tag();
        tag1.setTagId(null);
        tag1.setTagName("Tag1");

        Tag tag2 = new Tag();
        tag2.setTagId(null);
        tag2.setTagName("Tag2");

        List<Tag> tags = new ArrayList<>(Arrays.asList(tag1, tag2));

        Article article = Article.builder()
                .postedUser(user)
                .title("title test")
                .content("content test")
                .stateFlag(1)
                .tags(tags)
                .build();

        articleMapper.insertArticle(article);

        String articleSelectSql
                = "SELECT * FROM articles ORDER BY article_id";

        SqlParameterSource param = new EmptySqlParameterSource();

        List<Map<String, Object>> resultArticles = namedParameterJdbcTemplate.queryForList(articleSelectSql, param);

        System.out.println(resultArticles.get(0).get("article_id"));

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
