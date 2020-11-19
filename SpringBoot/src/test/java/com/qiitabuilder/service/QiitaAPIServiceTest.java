package com.qiitabuilder.service;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.security.SimpleLoginUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class QiitaAPIServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QiitaAPIService qiitaApiService;

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
                "   article_id       int null,\n" +
                "   posted_user_id   int null,\n" +
                "   register_user_id int null,\n" +
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

    final String URL = "https://qiita.com/api/v2/items";

    @BeforeEach
    public void beforeAll() {
        setAuthenticationInfo();
    }

    static void setAuthenticationInfo() {
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


    @Test
    void generateQiitaAPIAuthenticationURL() {

    }

    @Test
    void isAuthenticated() {
    }

    @Test
    void saveToken() {
        jdbcTemplate.execute("INSERT INTO users(user_id) VALUES (1)");
        jdbcTemplate.execute("INSERT INTO articles " +
                "(user_id,title, content, qiita_article_id, state_flag) " +
                "VALUES (1,'test_title','test_content','qiita_article_id',1)");

        jdbcTemplate.execute("INSERT INTO qiita_configurations(user_id) VALUES (1)");

        final String URL = "https://qiita.com/api/v2/access_tokens";

        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockServer.expect(requestTo(URL))
                .andRespond(withSuccess("{\"token\":\"token_test\"}", MediaType.APPLICATION_JSON_UTF8));

        qiitaApiService.saveToken();

        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT token FROM qiita_configurations");

        assertEquals("token_test", result.get("token"));
    }

    @Test
    void saveArticleToQiita_Qiitaに記事を投稿するとき() {
        jdbcTemplate.execute("INSERT INTO users(user_id) VALUES (1)");
        jdbcTemplate.execute("INSERT INTO articles " +
                "(user_id,title, content, qiita_article_id, state_flag) " +
                "VALUES (1,'test_title','test_content',null,1)");

        jdbcTemplate.execute("INSERT INTO tags(tag_name) " +
                "VALUES ('test_tag1'),('test_tag2')");

        jdbcTemplate.execute("INSERT INTO articles_tags_relations (article_id, posted_user_id, tag_id) " +
                "VALUES (1,1,1),(1,1,2)");


        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockServer.expect(requestTo(URL))
                .andRespond(withSuccess("{\"id\":\"id_test\"}", MediaType.APPLICATION_JSON_UTF8));

        qiitaApiService.saveArticleToQiita(1);

        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT * FROM articles WHERE article_id = 1");

        assertEquals(result.get("qiita_article_id"), "id_test");
        assertEquals(result.get("state_flag"), 2);

        mockServer.verify();
    }

    @Test
    void saveArticleToQiita_Qiitaの記事を更新するとき() {
        jdbcTemplate.execute("INSERT INTO users(user_id) VALUES (1)");
        jdbcTemplate.execute("INSERT INTO articles " +
                "(user_id,title, content, qiita_article_id, state_flag) " +
                "VALUES (1,'test_title','test_content','qiita_article_id',1)");

        jdbcTemplate.execute("INSERT INTO tags(tag_name) " +
                "VALUES ('test_tag1'),('test_tag2')");

        jdbcTemplate.execute("INSERT INTO articles_tags_relations (article_id, posted_user_id, tag_id) " +
                "VALUES (1,1,1),(1,1,2)");


        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockServer.expect(requestTo(URL + "/qiita_article_id"))
                .andRespond(withSuccess("{\"id\":\"id_test\"}", MediaType.APPLICATION_JSON_UTF8));

        qiitaApiService.saveArticleToQiita(1);

        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT * FROM articles WHERE article_id = 1");

        assertEquals(result.get("qiita_article_id"), "qiita_article_id");

        mockServer.verify();
    }

    @Test
    void saveArticleToQiita_異常系_新規投稿時エラーが発生したとき() {
        jdbcTemplate.execute("INSERT INTO users(user_id) VALUES (1)");
        jdbcTemplate.execute("INSERT INTO articles " +
                "(user_id,title, content, qiita_article_id, state_flag) " +
                "VALUES (1,'test_title','test_content',null,1)");

        jdbcTemplate.execute("INSERT INTO tags(tag_name) " +
                "VALUES ('test_tag1'),('test_tag2')");

        jdbcTemplate.execute("INSERT INTO articles_tags_relations (article_id, posted_user_id, tag_id) " +
                "VALUES (1,1,1),(1,1,2)");


        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());

        assertThrows(ResponseStatusException.class, () -> qiitaApiService.saveArticleToQiita(1));

        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT * FROM articles WHERE article_id = 1");

        assertNull(result.get("qiita_article_id"));
        assertEquals(1, result.get("state_flag"));

        mockServer.verify();
    }

    @Test
    void saveArticleToQiita_異常系_更新時エラーが発生したとき() {
        jdbcTemplate.execute("INSERT INTO users(user_id) VALUES (1)");
        jdbcTemplate.execute("INSERT INTO articles " +
                "(user_id,title, content, qiita_article_id, state_flag) " +
                "VALUES (1,'test_title','test_content','qiita_article_id',2)");

        jdbcTemplate.execute("INSERT INTO tags(tag_name) " +
                "VALUES ('test_tag1'),('test_tag2')");

        jdbcTemplate.execute("INSERT INTO articles_tags_relations (article_id, posted_user_id, tag_id) " +
                "VALUES (1,1,1),(1,1,2)");


        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockServer.expect(requestTo(URL + "/qiita_article_id"))
                .andRespond(withServerError());

        assertThrows(ResponseStatusException.class, () -> qiitaApiService.saveArticleToQiita(1));

        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT * FROM articles WHERE article_id = 1");

        assertEquals("qiita_article_id", result.get("qiita_article_id"));
        assertEquals(2, result.get("state_flag"));

        mockServer.verify();
    }

    @Test
    void isLinkedToQiitaTrue() {

        jdbcTemplate.execute("INSERT INTO users(user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO qiita_configurations(user_id, token) VALUES(1,'token_test')");

        assertTrue(qiitaApiService.isLinkedToQiita());
    }

    @Test
    void isLinkedToQiitaFalse() {
        jdbcTemplate.execute("INSERT INTO users(user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO qiita_configurations(user_id, token) VALUES(1,null)");

        assertFalse(qiitaApiService.isLinkedToQiita());
    }
}
