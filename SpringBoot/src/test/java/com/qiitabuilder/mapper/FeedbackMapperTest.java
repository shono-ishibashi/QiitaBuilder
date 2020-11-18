package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedbackMapperTest {

    @Autowired
    private FeedbackMapper feedbackMapper;

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
                "   article_id        int not null,\n" +
                "   posted_user_id    int not null,\n" +
                "   recommend_user_id int not null,\n" +
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

    //// insert()
    @Test
    void insertのテスト正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        LocalDateTime ldtnow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // ns以下の時間単位を切り落とし
        Timestamp tsNow = Timestamp.valueOf(ldtnow);

        Feedback feedback = Feedback.builder()
                .articleId(1)
                .postedUser(User.builder().userId(1).build())
                .createdAt(ldtnow)
                .content("sample")
                .deleteFlag(0)
                .build();

        feedbackMapper.insert(feedback);

        // check
        String sql = "SELECT * FROM feedbacks ORDER BY feedback_id";

        SqlParameterSource param = new EmptySqlParameterSource();

        List<Map<String, Object>> resultRecommends = namedParameterJdbcTemplate.queryForList(sql, param);

        assertEquals(1, resultRecommends.get(0).get("feedback_id"));
        assertEquals(1, resultRecommends.get(0).get("article_id"));
        assertEquals(1, resultRecommends.get(0).get("user_id"));
        assertEquals(tsNow, resultRecommends.get(0).get("created_at"));
        assertEquals("sample", resultRecommends.get(0).get("content"));
        assertEquals(0, resultRecommends.get(0).get("delete_flag"));
    }

    @Test
    void insertのテスト異常系_外部制約例外() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key

        LocalDateTime ldtnow = LocalDateTime.now();

        Feedback feedback = Feedback.builder()
                .articleId(1)
                .postedUser(User.builder().userId(1).build())
                .createdAt(ldtnow)
                .content("sample")
                .deleteFlag(0)
                .build();

        // check
        Exception exception = assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            feedbackMapper.insert(feedback);
        });

        String expectedMessage = "Cannot add or update a child row: a foreign key constraint fails";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    //// update()
    @Test
    void updateのテスト正常系() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key

        String insertSql = "INSERT INTO feedbacks(article_id, user_id, created_at, content, delete_flag) VALUES(:articleId, :userId, :createdAt, :content, :deleteFlag);";
        LocalDateTime ldtnow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // ns以下の時間単位を切り落とし

        Feedback setUpFeedback = Feedback.builder()
                .articleId(1)
                .postedUser(User.builder().userId(1).build())
                .createdAt(ldtnow)
                .content("sample")
                .deleteFlag(0)
                .build();
        SqlParameterSource setUpParam = new MapSqlParameterSource()
                .addValue("articleId", setUpFeedback.getArticleId())
                .addValue("userId", setUpFeedback.getPostedUser().getUserId())
                .addValue("createdAt", setUpFeedback.getCreatedAt())
                .addValue("content", setUpFeedback.getContent())
                .addValue("deleteFlag", setUpFeedback.getDeleteFlag());
        namedParameterJdbcTemplate.update(insertSql, setUpParam);

        // target method
        Feedback updateFeedback = Feedback.builder()
                .feedbackId(1)
                .articleId(2)
                .postedUser(User.builder().userId(2).build())
                .updatedAt(ldtnow.plusHours(3))
                .content("changed")
                .deleteFlag(1)
                .build();
        Integer actualResult = feedbackMapper.update(updateFeedback);
        // check
        assertEquals(1, actualResult);

        String sql = "SELECT * FROM feedbacks ORDER BY feedback_id";

        SqlParameterSource param = new EmptySqlParameterSource();

        List<Map<String, Object>> resultFeedbacks = namedParameterJdbcTemplate.queryForList(sql, param);

        assertEquals(1, resultFeedbacks.get(0).get("feedback_id"));
        assertEquals(2, resultFeedbacks.get(0).get("article_id"));
        assertEquals(2, resultFeedbacks.get(0).get("user_id"));
        assertEquals(Timestamp.valueOf(ldtnow), resultFeedbacks.get(0).get("created_at"));
        assertEquals(Timestamp.valueOf(ldtnow.plusHours(3)), resultFeedbacks.get(0).get("updated_at"));
        assertEquals("changed", resultFeedbacks.get(0).get("content"));
        assertEquals(1, resultFeedbacks.get(0).get("delete_flag"));
    }

    @Test
    void updateのテスト異常系_feedbackIdが存在しない場合() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        LocalDateTime ldtnow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // ns以下の時間単位を切り落とし

        // target method
        Feedback updateFeedback = Feedback.builder()
                .feedbackId(1)
                .articleId(1)
                .postedUser(User.builder().userId(1).build())
                .updatedAt(ldtnow)
                .content("changed")
                .deleteFlag(1)
                .build();
        Integer actualResult = feedbackMapper.update(updateFeedback);
        // check
        assertEquals(0, actualResult);
    }

    @Test
    void updateのテスト異常系_引数がNullの場合() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        LocalDateTime ldtnow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // ns以下の時間単位を切り落とし

        // target method
        Feedback updateFeedback = Feedback.builder()
                .build();
        Integer actualResult = feedbackMapper.update(updateFeedback);
        // check
        assertEquals(0, actualResult);
    }

    @Test
    void getFBRank() {
    }

    @Test
    void getArticleIdByUserId() {
    }

    //// load()
    @Test
    void loadのテスト正常系() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        String insertSql = "INSERT INTO feedbacks(article_id, user_id, created_at, content, delete_flag) VALUES(:articleId, :userId, :createdAt, :content, :deleteFlag);";
        LocalDateTime ldtnow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // ns以下の時間単位を切り落とし

        Feedback setUpFeedback = Feedback.builder()
                .articleId(1)
                .postedUser(User.builder().userId(1).build())
                .createdAt(ldtnow)
                .content("sample")
                .deleteFlag(0)
                .build();
        SqlParameterSource setUpParam = new MapSqlParameterSource()
                .addValue("articleId", setUpFeedback.getArticleId())
                .addValue("userId", setUpFeedback.getPostedUser().getUserId())
                .addValue("createdAt", setUpFeedback.getCreatedAt())
                .addValue("content", setUpFeedback.getContent())
                .addValue("deleteFlag", setUpFeedback.getDeleteFlag());
        namedParameterJdbcTemplate.update(insertSql, setUpParam);

        String updateSql = "UPDATE feedbacks SET updated_at = :updatedAt WHERE feedback_id = :feedbackId";
        SqlParameterSource updateParam = new MapSqlParameterSource()
                .addValue("updatedAt", ldtnow.plusHours(3))
                .addValue("feedbackId", 1);
        namedParameterJdbcTemplate.update(updateSql, updateParam);

        // target method
        Feedback actualResult = feedbackMapper.load(1);

        // check
        assertEquals(1, actualResult.getFeedbackId());
        assertEquals(1, actualResult.getArticleId());
        assertEquals(1, actualResult.getPostedUser().getUserId());
        assertEquals(ldtnow, actualResult.getCreatedAt());
        assertEquals(ldtnow.plusHours(3), actualResult.getUpdatedAt());
        assertEquals("sample", actualResult.getContent());
        assertEquals(0, actualResult.getDeleteFlag());
    }

    @Test
    void loadのテスト異常系_feedbackIdが存在しない場合() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        // target method
        Feedback actualResult = feedbackMapper.load(1);

        // check
        assertNull(actualResult);
    }

    @Test
    void getFeedbackedArticlesByUserIdのテスト正常系() {
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

        //userId=4のテスト
        List<Article> articles = feedbackMapper.getFeedbackedArticlesByUserId(4);

        LocalDateTime createDate = LocalDateTime.of(2020, 11, 8, 00, 00, 00);
        LocalDateTime updateDate = createDate.plusDays(1);

        //記事件数と一個目の記事のみ取得してきたもの全てテスト
        assertEquals(5, articles.size());
        assertEquals(190, articles.get(0).getArticleId());
        assertEquals("title190", articles.get(0).getTitle());
        assertEquals(createDate, articles.get(0).getCreatedAt());
        assertEquals(updateDate, articles.get(0).getUpdatedAt());
        assertEquals(1, articles.get(0).getStateFlag());
        assertEquals(2, articles.get(0).getTags().get(0).getTagId());
        assertEquals("ruby", articles.get(0).getTags().get(0).getTagName());
        assertEquals(4, articles.get(0).getTags().get(1).getTagId());
        assertEquals("php", articles.get(0).getTags().get(1).getTagName());
        assertEquals(2, articles.get(0).getFeedbackCount());
        assertEquals(1, articles.get(0).getRegisteredMyArticleCount());
        assertEquals(37, articles.get(0).getPostedUser().getUserId());
        assertEquals("user37", articles.get(0).getPostedUser().getDisplayName());
        assertEquals("photo37", articles.get(0).getPostedUser().getPhotoUrl());
        assertEquals(1, articles.get(0).getQiitaRecommendPoint());
    }
}