package com.qiitabuilder.service;

import com.qiitabuilder.domain.MyArticle;
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
class MyArticleServiceTest {

    @Autowired
    private MyArticleService myArticleService;
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

    // fetchMyArticle()
    @Test
    void fetchMyArticle正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 1, 1)");

        MyArticle myArticle = myArticleService.fetchMyArticle(1);
        assertEquals(1, myArticle.getMyArticleId());
        assertEquals(1, myArticle.getArticleId());
        assertEquals(1, myArticle.getPostedUserId());
        assertEquals(1, myArticle.getRegisterUserId());
    }

    @Test
    void fetchMyArticle異常系_該当のMy記事情報が存在しない場合_articleIdなし() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1

        // ステータスコード204をスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            MyArticle myArticle = myArticleService.fetchMyArticle(1);
        });
        String expectedMessage = "204 NO_CONTENT";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void fetchMyArticle異常系_該当のMy記事情報が存在しない場合_registerUserIdなし() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 1, 2)");

        // ステータスコード204をスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            MyArticle myArticle = myArticleService.fetchMyArticle(1);
        });
        String expectedMessage = "204 NO_CONTENT";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    //// postMyArticle()
    @Test
    void postMyArticle正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key

        MyArticle myArticle = MyArticle.builder()
                .articleId(1)
                .build();

        MyArticle actual = myArticleService.postMyArticle(myArticle);

        // 戻り値の確認
        assertEquals(1, actual.getMyArticleId());
        assertEquals(1, actual.getArticleId());
        assertEquals(2, actual.getPostedUserId());
        assertEquals(1, actual.getRegisterUserId());

        // DBへの書き込みが正常に行われているか確認
        String sql = "SELECT * FROM my_articles ORDER BY my_article_id";
        SqlParameterSource param = new EmptySqlParameterSource();
        List<Map<String, Object>> resultMyArticles = namedParameterJdbcTemplate.queryForList(sql, param);

        assertEquals(1, resultMyArticles.get(0).get("my_article_id"));
        assertEquals(1, resultMyArticles.get(0).get("article_id"));
        assertEquals(2, resultMyArticles.get(0).get("posted_user_id"));
        assertEquals(1, resultMyArticles.get(0).get("register_user_id"));
    }

    @Test
    void postMyArticle異常系_記事IDが存在しない場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key

        MyArticle myArticle = MyArticle.builder()
                .articleId(1)
                .build();

        // ステータスコード400をスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            myArticleService.postMyArticle(myArticle);
        });
        String expectedMessage = "400 BAD_REQUEST";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        // DBへの書き込みが実行されていないか確認
        String sql = "SELECT * FROM my_articles ORDER BY my_article_id";
        SqlParameterSource param = new EmptySqlParameterSource();
        List<Map<String, Object>> resultMyArticles = namedParameterJdbcTemplate.queryForList(sql, param);

        assertTrue(resultMyArticles.isEmpty());
    }

    @Test
    void postMyArticle異常系_すでにDBに登録されている場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 2, 1)");

        MyArticle myArticle = MyArticle.builder()
                .articleId(1)
                .build();

        // ステータスコード409をスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            myArticleService.postMyArticle(myArticle);
        });
        String expectedMessage = "409 CONFLICT";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        // DBの状態が変更されていないか確認
        String sql = "SELECT * FROM my_articles ORDER BY my_article_id";
        SqlParameterSource param = new EmptySqlParameterSource();
        List<Map<String, Object>> resultMyArticles = namedParameterJdbcTemplate.queryForList(sql, param);

        assertTrue(resultMyArticles.size() == 1);
        assertEquals(1, resultMyArticles.get(0).get("my_article_id"));
        assertEquals(1, resultMyArticles.get(0).get("article_id"));
        assertEquals(2, resultMyArticles.get(0).get("posted_user_id"));
        assertEquals(1, resultMyArticles.get(0).get("register_user_id"));
    }

    //// deleteMyArticle()
    @Test
    void deleteMyArticle正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key 記事

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 2, 1)");

        myArticleService.deleteMyArticle(1);

        // check
        String sql = "SELECT * FROM my_articles WHERE my_article_id = 1";

        SqlParameterSource param = new EmptySqlParameterSource();

        List<Map<String, Object>> actual = namedParameterJdbcTemplate.queryForList(sql, param);

        assertTrue(actual.isEmpty());
    }

    @Test
    void deleteMyArticle異常系_myArticleIdが存在しない場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key 記事
        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 2, 1)");

        // ステータスコード409をスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            myArticleService.deleteMyArticle(2);
        });
        String expectedMessage = "409 CONFLICT";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        // DBの状態が変更されていないか確認
        String sql = "SELECT * FROM my_articles ORDER BY my_article_id";
        SqlParameterSource param = new EmptySqlParameterSource();
        List<Map<String, Object>> resultMyArticles = namedParameterJdbcTemplate.queryForList(sql, param);

        assertTrue(resultMyArticles.size() == 1);
        assertEquals(1, resultMyArticles.get(0).get("my_article_id"));
        assertEquals(1, resultMyArticles.get(0).get("article_id"));
        assertEquals(2, resultMyArticles.get(0).get("posted_user_id"));
        assertEquals(1, resultMyArticles.get(0).get("register_user_id"));    }
    @Test
    void deleteMyArticle異常系_myArticleIdがログインユーザーのものではない場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key 記事

        jdbcTemplate.execute("INSERT INTO my_articles(article_id, posted_user_id, register_user_id) VALUES(1, 1, 2)");


        // ステータスコード403をスローするか確認
        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            myArticleService.deleteMyArticle(1);
        });
        String expectedMessage = "403 FORBIDDEN";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}