package com.qiitabuilder.service;

import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.security.SimpleLoginUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedbackServiceTest {

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void setAuthenticationInfo() {
        User user = User.builder()
                .userId(1)
                .uid("test_uid")
                .password("test_password")
                .build();
        SimpleLoginUser loginUser = new SimpleLoginUser(user);
        Authentication authentication = new TestingAuthenticationToken(loginUser, null);
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public static void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @BeforeEach
    private void beforeEach() {
        setAuthenticationInfo();
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
                "   feedback_version int not null default 1,\n" +
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
        clearContext();
        jdbcTemplate.execute("DROP TABLE qiita_recommends");
        jdbcTemplate.execute("DROP TABLE qiita_configurations");
        jdbcTemplate.execute("DROP TABLE my_articles");
        jdbcTemplate.execute("DROP TABLE feedbacks");
        jdbcTemplate.execute("DROP TABLE articles_tags_relations");
        jdbcTemplate.execute("DROP TABLE tags");
        jdbcTemplate.execute("DROP TABLE articles");
        jdbcTemplate.execute("DROP TABLE users");
    }

    //// fetchFeedback()
    @Test
    void fetchFeedback正常系() {
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
        Feedback actualResult = feedbackService.fetchFeedback(1);

        // check
        assertEquals(1, actualResult.getFeedbackId());
        assertEquals(1, actualResult.getArticleId());
        assertEquals(1, actualResult.getPostedUser().getUserId());
        assertEquals(ldtnow, actualResult.getCreatedAt());
        assertEquals(ldtnow.plusHours(3), actualResult.getUpdatedAt());
        assertEquals("sample", actualResult.getContent());
        assertEquals(1, actualResult.getFeedbackVersion());
        assertEquals(0, actualResult.getDeleteFlag());
    }

    @Test
    void fetchFeedback異常系_feedbackIdが存在しない場合() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        // target method
        Feedback actualResult = feedbackService.fetchFeedback(1);

        // check
        assertNull(actualResult);
    }

    //// postFeedback()
    @Test
    void postFeedback正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        Feedback feedback = Feedback.builder()
                .articleId(1)
                .content("sample")
                .deleteFlag(0)
                .build();

        Feedback actual = feedbackService.postFeedback(feedback);

        // 戻り値の確認
        assertEquals(1, actual.getFeedbackId());
        assertEquals(User.builder().userId(1).build(), actual.getPostedUser());
        assertNotNull(actual.getCreatedAt());
        assertEquals("sample", actual.getContent());
        assertEquals(1, actual.getFeedbackVersion());
        assertEquals(0, actual.getDeleteFlag());

        // DBへの書き込みが正常に行われているか
        String sql = "SELECT * FROM feedbacks ORDER BY feedback_id";
        SqlParameterSource param = new EmptySqlParameterSource();
        List<Map<String, Object>> resultRecommends = namedParameterJdbcTemplate.queryForList(sql, param);

        assertEquals(1, resultRecommends.get(0).get("feedback_id"));
        assertEquals(1, resultRecommends.get(0).get("article_id"));
        assertEquals(1, resultRecommends.get(0).get("user_id"));
        assertEquals("sample", resultRecommends.get(0).get("content"));
        assertEquals(1, resultRecommends.get(0).get("feedback_version"));
        assertEquals(0, resultRecommends.get(0).get("delete_flag"));
    }

    //// updateFeedback()
    @Test
    void updateFeedback正常系() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key

        String insertFeed1 = "INSERT INTO feedbacks (article_id, user_id, created_at, updated_at, content, delete_flag) VALUES (1, 1, '2020-11-03 00:00:00', '2020-11-04 00:00:00', 'feedback content1', 0);";
        jdbcTemplate.execute(insertFeed1);

        // actual
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime updatedAt = LocalDateTime.parse("2020-11-05 00:00", dtf);
        Feedback feedback = Feedback.builder()
                .feedbackId(1)
                .articleId(1)
                .feedbackVersion(1)
                .content("changed")
                .deleteFlag(1)
                .build();
        Feedback actual = feedbackService.updateFeedback(feedback);
        // 戻り値の確認
        assertEquals(1, actual.getFeedbackId());
        assertEquals(1, actual.getArticleId());
        assertEquals(User.builder().userId(1).build(), actual.getPostedUser());
        assertNotNull(actual.getCreatedAt());
        assertEquals("changed", actual.getContent());
        assertEquals(2, actual.getFeedbackVersion());
        assertEquals(1, actual.getDeleteFlag());

        // DBへの書き込みが正常に行われているか
        String sql = "SELECT * FROM feedbacks ORDER BY feedback_id";
        SqlParameterSource param = new EmptySqlParameterSource();
        List<Map<String, Object>> resultRecommends = namedParameterJdbcTemplate.queryForList(sql, param);

        assertEquals(1, resultRecommends.get(0).get("feedback_id"));
        assertEquals(1, resultRecommends.get(0).get("article_id"));
        assertEquals(1, resultRecommends.get(0).get("user_id"));
        assertEquals("changed", resultRecommends.get(0).get("content"));
        assertEquals(2, resultRecommends.get(0).get("feedback_version"));
        assertEquals(1, resultRecommends.get(0).get("delete_flag"));
    }

    @Test
    void updateFeedback異常系_FBの投稿者とログインユーザーが一致しない場合() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key

        String insertFeed1 = "INSERT INTO feedbacks (article_id, user_id, created_at, updated_at, content, delete_flag) VALUES (1, 2, '2020-11-03 00:00:00', '2020-11-04 00:00:00', 'feedback content1', 0);";
        jdbcTemplate.execute(insertFeed1);

        // actual
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime createdAt = LocalDateTime.parse("2020-11-03 00:00", dtf);
        LocalDateTime updatedAt = LocalDateTime.parse("2020-11-05 00:00", dtf);
        Feedback feedback = Feedback.builder()
                .feedbackId(1)
                .articleId(1)
                .content("changed")
                .updatedAt(updatedAt)
                .deleteFlag(1)
                .build();

        // 403エラーをスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            Feedback actual = feedbackService.updateFeedback(feedback);
        });
        String expectedMessage = "403 FORBIDDEN";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        // DBの状態が変更されていないか確認
        String sql = "SELECT * FROM feedbacks ORDER BY feedback_id";
        SqlParameterSource param = new EmptySqlParameterSource();
        List<Map<String, Object>> resultFeedbacks = namedParameterJdbcTemplate.queryForList(sql, param);

        assertEquals(1, resultFeedbacks.get(0).get("feedback_id"));
        assertEquals(1, resultFeedbacks.get(0).get("article_id"));
        assertEquals(2, resultFeedbacks.get(0).get("user_id"));
        assertEquals(Timestamp.valueOf(createdAt), resultFeedbacks.get(0).get("created_at"));
        assertEquals("feedback content1", resultFeedbacks.get(0).get("content"));
        assertEquals(0, resultFeedbacks.get(0).get("delete_flag"));
    }
    @Test
    void updateFeedback異常系_FBのVerionが一致しない場合() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key

        String insertFeed1 = "INSERT INTO feedbacks (article_id, user_id, created_at, updated_at, content, delete_flag) VALUES (1, 1, '2020-11-03 00:00:00', '2020-11-04 00:00:00', 'feedback content1', 0);";
        jdbcTemplate.execute(insertFeed1);

        // actual
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime createdAt = LocalDateTime.parse("2020-11-03 00:00", dtf);
        LocalDateTime updatedAt = LocalDateTime.parse("2020-11-05 00:00", dtf);
        Feedback feedback = Feedback.builder()
                .feedbackId(1)
                .articleId(1)
                .content("changed")
                .feedbackVersion(10)
                .updatedAt(updatedAt)
                .deleteFlag(1)
                .build();

        // 409エラーをスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            Feedback actual = feedbackService.updateFeedback(feedback);
        });
        String expectedMessage = "409 CONFLICT";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        // DBの状態が変更されていないか確認
        String sql = "SELECT * FROM feedbacks ORDER BY feedback_id";
        SqlParameterSource param = new EmptySqlParameterSource();
        List<Map<String, Object>> resultFeedbacks = namedParameterJdbcTemplate.queryForList(sql, param);

        assertEquals(1, resultFeedbacks.get(0).get("feedback_id"));
        assertEquals(1, resultFeedbacks.get(0).get("article_id"));
        assertEquals(1, resultFeedbacks.get(0).get("user_id"));
        assertEquals(Timestamp.valueOf(createdAt), resultFeedbacks.get(0).get("created_at"));
        assertEquals("feedback content1", resultFeedbacks.get(0).get("content"));
        assertEquals(1, resultFeedbacks.get(0).get("feedback_version"));
        assertEquals(0, resultFeedbacks.get(0).get("delete_flag"));
    }
}
