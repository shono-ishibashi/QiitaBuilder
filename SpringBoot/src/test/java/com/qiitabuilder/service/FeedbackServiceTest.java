package com.qiitabuilder.service;

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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

    //// fetchFeedback
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

    @Test
    void postFeedback() {
    }

    @Test
    void updateFeedback() {
    }
}