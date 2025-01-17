package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.MyArticle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MyArticleMapperTest {

    @Autowired
    private MyArticleMapper myArticleMapper;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    private void beforeEach() {
        jdbcTemplate.execute("create table users\n" +
                "(\n" +
                "   user_id      int auto_increment\n" +
                "       primary key,\n" +
                "   uid          text null,\n" +
                "   photo_url    text null,\n" +
                "   display_name text null,\n" +
                "   password     text null\n" +
                ");\n");

        jdbcTemplate.execute("create table articles\n" +
                "(\n" +
                "   article_id       int auto_increment\n" +
                "       primary key,\n" +
                "   user_id          int          null,\n" +
                "   created_at       datetime     null,\n" +
                "   updated_at       datetime     null,\n" +
                "   title            varchar(100) null,\n" +
                "   content          text         null,\n" +
                "   qiita_article_id text         null,\n" +
                "   state_flag       int          null,\n" +
                "   article_version  int          not null default 1,\n" +
                "   constraint fk_articles_userid\n" +
                "       foreign key (user_id) references users (user_id)\n" +
                ");\n");

        jdbcTemplate.execute("create table tags\n" +
                "(\n" +
                "   tag_id   int auto_increment\n" +
                "       primary key,\n" +
                "   tag_name text null\n" +
                ");\n");

        jdbcTemplate.execute("create table articles_tags_relations\n" +
                "(\n" +
                "   tags_relation_id int auto_increment\n" +
                "       primary key,\n" +
                "   article_id       int null,\n" +
                "   posted_user_id   int null,\n" +
                "   tag_id           int null,\n" +
                "   constraint fk_tags_relations_articleid\n" +
                "       foreign key (article_id) references articles (article_id),\n" +
                "   constraint fk_tags_relations_tagid\n" +
                "       foreign key (tag_id) references tags (tag_id),\n" +
                "   constraint fk_tags_relations_userid\n" +
                "       foreign key (posted_user_id) references articles (user_id)\n" +
                ");\n");

        jdbcTemplate.execute("create table feedbacks\n" +
                "(\n" +
                "   feedback_id int auto_increment\n" +
                "       primary key,\n" +
                "   article_id  int      null,\n" +
                "   user_id     int      null,\n" +
                "   created_at  datetime null,\n" +
                "   updated_at  datetime null,\n" +
                "   content     text     null,\n" +
                "   delete_flag int      null,\n" +
                "   constraint fk_feedbacks_articleid\n" +
                "       foreign key (article_id) references articles (article_id),\n" +
                "   constraint fk_feedbacks_userid\n" +
                "       foreign key (user_id) references users (user_id)\n" +
                ");\n");

        jdbcTemplate.execute("create table my_articles\n" +
                "(\n" +
                "   my_article_id    int auto_increment\n" +
                "       primary key,\n" +
                "   article_id       int not null,\n" +
                "   posted_user_id   int not null,\n" +
                "   register_user_id int not null,\n" +
                "   constraint fk_myarticles_articleid\n" +
                "       foreign key (article_id) references articles (article_id),\n" +
                "   constraint fk_myarticles_posted_userid\n" +
                "       foreign key (posted_user_id) references articles (user_id),\n" +
                "   constraint fk_myarticles_register_userid\n" +
                "       foreign key (register_user_id) references users (user_id)\n" +
                ");\n");

        jdbcTemplate.execute("create table qiita_configurations\n" +
                "(\n" +
                "   qiita_cofiguration_id int auto_increment\n" +
                "       primary key,\n" +
                "   user_id               int         not null,\n" +
                "   state                 varchar(36) null,\n" +
                "   code                  varchar(40) null,\n" +
                "   token                 varchar(40) null,\n" +
                "   constraint qiita_configurations_user_id_uindex\n" +
                "       unique (user_id),\n" +
                "   constraint qiita_configurations_users_user_id_fk\n" +
                "       foreign key (user_id) references users (user_id)\n" +
                ");\n");

        jdbcTemplate.execute("create table qiita_recommends\n" +
                "(\n" +
                "   recommend_id      int auto_increment\n" +
                "       primary key,\n" +
                "   article_id        int null,\n" +
                "   posted_user_id    int null,\n" +
                "   recommend_user_id int null,\n" +
                "   constraint fk_qiitarecommends_articleid\n" +
                "       foreign key (article_id) references articles (article_id),\n" +
                "   constraint fk_qiitarecommends_posted_userid\n" +
                "       foreign key (posted_user_id) references articles (user_id),\n" +
                "   constraint fk_qiitarecommends_recommend_userid\n" +
                "       foreign key (recommend_user_id) references users (user_id)\n" +
                ");\n");
    }


    @AfterEach
    private void beforeAfter() {
        jdbcTemplate.execute("DROP TABLE qiita_recommends");
        jdbcTemplate.execute("DROP TABLE qiita_configurations");
        jdbcTemplate.execute("DROP TABLE my_articles");
        jdbcTemplate.execute("DROP TABLE feedbacks");
        jdbcTemplate.execute("DROP TABLE articles_tags_relations");
        jdbcTemplate.execute("DROP TABLE tags");
        jdbcTemplate.execute("DROP TABLE articles");
        jdbcTemplate.execute("DROP TABLE users");
    }

    @Test
    void getMyArticlesByUserIdのテスト正常系() {
        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr = CollectionSQL.insertArticles.split("\n", 0);
        String[] feedbackSqlArr = CollectionSQL.insertFeedbacks.split("\n", 0);
        String[] qiitaRecommendSqlArr = CollectionSQL.insertQiitaRecommends.split("\n", 0);
        String[] tagSqlArr = CollectionSQL.insertTags.split("\n", 0);
        String[] tagRelationSqlArr = CollectionSQL.insertArticlesTagsRelations.split("\n", 0);
        String[] myArticleSqlArr = CollectionSQL.insertMyArticles.split("\n", 0);

        Arrays.stream(userSqlArr).forEach((sql) -> jdbcTemplate.execute(sql));
        Arrays.stream(articleSqlArr).forEach((sql) -> jdbcTemplate.execute(sql));
        Arrays.stream(feedbackSqlArr).forEach((sql) -> jdbcTemplate.execute(sql));
        Arrays.stream(qiitaRecommendSqlArr).forEach((sql) -> jdbcTemplate.execute(sql));
        Arrays.stream(tagSqlArr).forEach((sql) -> jdbcTemplate.execute(sql));
        Arrays.stream(tagRelationSqlArr).forEach((sql) -> jdbcTemplate.execute(sql));
        Arrays.stream(myArticleSqlArr).forEach((sql) -> jdbcTemplate.execute(sql));

        //userId=1のテスト
        List<Article> articles = myArticleMapper.getMyArticlesByUserId(1);

        LocalDateTime createDateFirst = LocalDateTime.of(2020, 11, 5, 00, 00, 00);
        LocalDateTime updateDateFirst = createDateFirst.plusDays(1);
        LocalDateTime createDateLast = LocalDateTime.of(2020, 10, 8, 00, 00, 00);
        LocalDateTime updateDateLast = createDateLast.plusDays(1);

        //記事件数と最初,最後の記事の取得してきたもの全てテスト
        assertEquals(6, articles.size());
        assertEquals(28, articles.get(0).getArticleId());
        assertEquals("title28", articles.get(0).getTitle());
        assertEquals(createDateFirst, articles.get(0).getCreatedAt());
        assertEquals(updateDateFirst, articles.get(0).getUpdatedAt());
        assertEquals(2, articles.get(0).getStateFlag());
        assertEquals(3, articles.get(0).getTags().get(0).getTagId());
        assertEquals("javascript", articles.get(0).getTags().get(0).getTagName());
        assertEquals(5, articles.get(0).getTags().get(1).getTagId());
        assertEquals("go", articles.get(0).getTags().get(1).getTagName());
        assertEquals(2, articles.get(0).getFeedbackCount());
        assertEquals(2, articles.get(0).getRegisteredMyArticleCount());
        assertEquals(3, articles.get(0).getPostedUser().getUserId());
        assertEquals("c", articles.get(0).getPostedUser().getDisplayName());
        assertEquals("c", articles.get(0).getPostedUser().getPhotoUrl());
        assertEquals(3, articles.get(0).getQiitaRecommendPoint());

        assertEquals(14, articles.get(5).getArticleId());
        assertEquals("title14", articles.get(5).getTitle());
        assertEquals(createDateLast, articles.get(5).getCreatedAt());
        assertEquals(updateDateLast, articles.get(5).getUpdatedAt());
        assertEquals(1, articles.get(5).getStateFlag());
        assertEquals(3, articles.get(5).getTags().get(0).getTagId());
        assertEquals("javascript", articles.get(5).getTags().get(0).getTagName());
        assertEquals(4, articles.get(5).getTags().get(1).getTagId());
        assertEquals("php", articles.get(5).getTags().get(1).getTagName());
        assertEquals(0, articles.get(5).getFeedbackCount());
        assertEquals(1, articles.get(5).getRegisteredMyArticleCount());
        assertEquals(2, articles.get(5).getPostedUser().getUserId());
        assertEquals("b", articles.get(5).getPostedUser().getDisplayName());
        assertEquals("b", articles.get(5).getPostedUser().getPhotoUrl());
        assertEquals(2, articles.get(5).getQiitaRecommendPoint());
    }

    //// findByArticleIdAndRegisterUserId()
    @Test
    void findByArticleIdAndRegisterUserIdのテスト正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 1, 1)");

        MyArticle myArticle = myArticleMapper.findByArticleIdAndRegisterUserId(1, 1);
        assertEquals(1, myArticle.getMyArticleId());
        assertEquals(1, myArticle.getArticleId());
        assertEquals(1, myArticle.getPostedUserId());
        assertEquals(1, myArticle.getRegisterUserId());
    }

    //// load()
    @Test
    void loadのテスト正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 1, 1)");

        MyArticle myArticle = myArticleMapper.load(1);
        assertEquals(1, myArticle.getMyArticleId());
        assertEquals(1, myArticle.getArticleId());
        assertEquals(1, myArticle.getPostedUserId());
        assertEquals(1, myArticle.getRegisterUserId());
    }

    @Test
    void findByArticleIdAndRegisterUserIdのテスト異常系_MyArticleが存在しない場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 1, 1)");

        MyArticle myArticle = myArticleMapper.findByArticleIdAndRegisterUserId(2, 1);
        assertNull(myArticle);
    }

    @Test
    void findByArticleIdAndRegisterUserIdのテスト異常系_引数がNullの場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 1, 1)");

        MyArticle myArticle = myArticleMapper.findByArticleIdAndRegisterUserId(null, null);
        assertNull(myArticle);
    }

    //// insert()
    @Test
    void insertのテスト正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        MyArticle myArticle = MyArticle.builder()
                .articleId(1)
                .postedUserId(1)
                .registerUserId(1)
                .build();

        myArticleMapper.insert(myArticle);

        // check
        String sql = "SELECT * FROM my_articles ORDER BY my_article_id";

        SqlParameterSource param = new EmptySqlParameterSource();

        List<Map<String, Object>> resultMyArticles = namedParameterJdbcTemplate.queryForList(sql, param);

        assertEquals(1, resultMyArticles.get(0).get("my_article_id"));
        assertEquals(1, resultMyArticles.get(0).get("article_id"));
        assertEquals(1, resultMyArticles.get(0).get("posted_user_id"));
        assertEquals(1, resultMyArticles.get(0).get("register_user_id"));
    }

    @Test
    void insertのテスト異常系_外部制約例外() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key

        MyArticle myArticle = MyArticle.builder()
                .articleId(1)
                .postedUserId(1)
                .registerUserId(1)
                .build();

        // check
        Exception exception = assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            myArticleMapper.insert(myArticle);
        });

        String expectedMessage = "Cannot add or update a child row: a foreign key constraint fails";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void insertのテスト異常系_引数がNullの場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key

        MyArticle myArticle = new MyArticle();

        // check
        Exception exception = assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            myArticleMapper.insert(myArticle);
        });
        String expectedMessage = "Column 'article_id' cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    //// getAutoIncrementKey()
    @Test
    void getAutoIncrementKeyのテスト正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 1, 1)");
        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(2, 1, 1)");

        Integer actualResult2 = myArticleMapper.getAutoIncrementKey();
        assertEquals(2, actualResult2);
    }

    //// delete()
    @Test
    void deleteのテスト正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 1, 1)");

        boolean actualResult = myArticleMapper.delete(1);
        assertTrue(actualResult);
    }

    @Test
    void deleteのテスト異常系_MyArticleが存在しない場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 1, 1)");

        boolean actualResult = myArticleMapper.delete(2);
        assertFalse(actualResult);
    }

    @Test
    void deleteのテスト異常系_引数がNullの場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 1, 1)");

        // check
        boolean actualResult = myArticleMapper.delete(null);
        assertFalse(actualResult);
    }
}
