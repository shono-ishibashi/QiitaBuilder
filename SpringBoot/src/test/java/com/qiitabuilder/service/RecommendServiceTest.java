package com.qiitabuilder.service;

import com.qiitabuilder.domain.Recommend;
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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RecommendServiceTest {

    @Autowired
    private RecommendService recommendService;
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

    // fetchRecommend()
    @Test
    void fetchRecommend正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 1, 1)");

        Recommend recommend = recommendService.fetchRecommend(1);
        assertEquals(1, recommend.getRecommendId());
        assertEquals(1, recommend.getArticleId());
        assertEquals(1, recommend.getPostedUserId());
        assertEquals(1, recommend.getRecommendUserId());
    }

    @Test
    void fetchRecommend異常系_該当のQiita推薦情報が存在しない場合_articleIdなし() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1

        // ステータスコード204をスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            recommendService.fetchRecommend(1);
        });
        String expectedMessage = "204 NO_CONTENT";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void fetchRecommend異常系_該当のQiita推薦情報が存在しない場合_recommendUserIdなし() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 1, 2)");

        // ステータスコード204をスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            recommendService.fetchRecommend(1);
        });
        String expectedMessage = "204 NO_CONTENT";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    //// postRecommend()
    @Test
    void postRecommend正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key

        Recommend recommend = Recommend.builder()
                .articleId(1)
                .build();

        Recommend actual = recommendService.postRecommend(recommend);

        // 戻り値の確認
        assertEquals(1, actual.getRecommendId());
        assertEquals(1, actual.getArticleId());
        assertEquals(2, actual.getPostedUserId());
        assertEquals(1, actual.getRecommendUserId());

        // DBへの書き込みが正常に行われているか確認
        String sql = "SELECT * FROM qiita_recommends ORDER BY recommend_id";
        SqlParameterSource param = new EmptySqlParameterSource();
        List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, param);

        assertEquals(1, result.get(0).get("recommend_id"));
        assertEquals(1, result.get(0).get("article_id"));
        assertEquals(2, result.get(0).get("posted_user_id"));
        assertEquals(1, result.get(0).get("recommend_user_id"));
    }

    @Test
    void postRecommend異常系_記事IDが存在しない場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key

        Recommend recommend = Recommend.builder()
                .articleId(1)
                .build();

        // ステータスコード400をスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            recommendService.postRecommend(recommend);
        });
        String expectedMessage = "400 BAD_REQUEST";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        // DBへの書き込みが実行されていないか確認
        String sql = "SELECT * FROM qiita_recommends ORDER BY recommend_id";
        SqlParameterSource param = new EmptySqlParameterSource();
        List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, param);

        assertTrue(result.isEmpty());
    }

    @Test
    void postRecommend異常系_すでにDBに登録されている場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 2, 1)");

        Recommend recommend = Recommend.builder()
                .articleId(1)
                .build();

        // ステータスコード409をスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            recommendService.postRecommend(recommend);
        });
        String expectedMessage = "409 CONFLICT";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        // DBの状態が変更されていないか確認
        String sql = "SELECT * FROM qiita_recommends ORDER BY recommend_id";
        SqlParameterSource param = new EmptySqlParameterSource();
        List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, param);

        assertTrue(result.size() == 1);
        assertEquals(1, result.get(0).get("recommend_id"));
        assertEquals(1, result.get(0).get("article_id"));
        assertEquals(2, result.get(0).get("posted_user_id"));
        assertEquals(1, result.get(0).get("recommend_user_id"));
    }

    //// deleteRecommend()
    @Test
    void deleteRecommend正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key 記事

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 2, 1)");

        recommendService.deleteRecommend(1);

        // check
        String sql = "SELECT * FROM qiita_recommends WHERE recommend_id = 1";

        SqlParameterSource param = new EmptySqlParameterSource();

        List<Map<String, Object>> actual = namedParameterJdbcTemplate.queryForList(sql, param);

        assertTrue(actual.isEmpty());
    }

    @Test
    void deleteRecommend異常系_recommendIdが存在しない場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key 記事
        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 2, 1)");

        // ステータスコード409をスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            recommendService.deleteRecommend(2);
        });
        String expectedMessage = "409 CONFLICT";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        // DBの状態が変更されていないか確認
        String sql = "SELECT * FROM qiita_recommends ORDER BY recommend_id";
        SqlParameterSource param = new EmptySqlParameterSource();
        List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, param);

        assertTrue(result.size() == 1);
        assertEquals(1, result.get(0).get("recommend_id"));
        assertEquals(1, result.get(0).get("article_id"));
        assertEquals(2, result.get(0).get("posted_user_id"));
        assertEquals(1, result.get(0).get("recommend_user_id"));
    }

    @Test
    void deleteRecommend異常系_recommendIdがログインユーザーのものではない場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key 記事

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 1, 2)");


        // ステータスコード403をスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            recommendService.deleteRecommend(1);
        });
        String expectedMessage = "403 FORBIDDEN";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}