package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.QiitaConfiguration;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.security.SimpleLoginUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QiitaConfigurationMapperTest {

    @Autowired
    private QiitaConfigurationMapper mapper;

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
                "   qiita_configuration_id int auto_increment\n" +
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
    void getCodeByUserId() {
        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO articles (article_id, user_id, title, content, qiita_article_id, state_flag)" +
                " VALUES(1,1,'test_title','test_content',null,1 );");
        jdbcTemplate.execute("INSERT INTO qiita_configurations (user_id, code) VALUES(1,'code_test')");

        String result = mapper.getCodeByUserId(1);

        assertEquals(result, "code_test");
    }

    @Test
    void getStateByUserId() {
        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO articles (article_id, user_id, title, content, qiita_article_id, state_flag)" +
                " VALUES(1,1,'test_title','test_content',null,1 );");
        jdbcTemplate.execute("INSERT INTO qiita_configurations (user_id, state) VALUES(1,'state_test')");

        String result = mapper.getStateByUserId(1);

        assertEquals(result, "state_test");
    }

    @Test
    void getTokenByUserId() {
        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO articles (article_id, user_id, title, content, qiita_article_id, state_flag)" +
                " VALUES(1,1,'test_title','test_content',null,1 );");
        jdbcTemplate.execute("INSERT INTO qiita_configurations (user_id, token) VALUES(1,'token_test')");

        String result = mapper.getTokenByUserId(1);

        assertEquals(result, "token_test");
    }

    @Test
    void insertQiitaConfiguration() {
        //init
        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO articles (article_id, user_id, title, content, qiita_article_id, state_flag)" +
                " VALUES(1,1,'test_title','test_content',null,1 );");

        QiitaConfiguration qiitaConfiguration = new QiitaConfiguration();
        qiitaConfiguration.setUserId(1);
        qiitaConfiguration.setState("state_test");
        qiitaConfiguration.setCode("code_test");

        mapper.insertQiitaConfiguration(qiitaConfiguration);

        Map<String,Object> result = jdbcTemplate.queryForMap("SELECT * FROM qiita_configurations WHERE user_id = 1");

        assertEquals(1,result.get("user_id"));
        assertEquals(1,result.get("qiita_configuration_id"));
        assertEquals("state_test",result.get("state"));
        assertEquals("code_test",result.get("code"));
    }

    @Test
    void deleteByUserId() {
        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO articles (article_id, user_id, title, content, qiita_article_id, state_flag)" +
                " VALUES(1,1,'test_title','test_content',null,1 );");

        jdbcTemplate.execute("INSERT INTO qiita_configurations(user_id, state, code) " +
                "VALUES(1,'state_test','code_test')");

        mapper.deleteByUserId(1);

        List<Map<String,Object>> result = jdbcTemplate.queryForList("SELECT * FROM qiita_configurations WHERE user_id = 1");

        assertTrue(result.isEmpty());
    }

    @Test
    void updateQiitaConfigurationCode() {
        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO articles (article_id, user_id, title, content, qiita_article_id, state_flag)" +
                " VALUES(1,1,'test_title','test_content',null,1 );");

        jdbcTemplate.execute("INSERT INTO qiita_configurations(user_id, state, code) " +
                "VALUES(1,'state_test','code_test')");

        QiitaConfiguration qiitaConfiguration = new QiitaConfiguration();
        qiitaConfiguration.setUserId(1);
        qiitaConfiguration.setState("state_test");
        qiitaConfiguration.setCode("code_test_updated");

        mapper.updateQiitaConfigurationCode(qiitaConfiguration);

        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT * FROM qiita_configurations WHERE user_id = 1");

        assertEquals("code_test_updated",result.get("code"));
    }

    @Test
    void updateState() {
        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO articles (article_id, user_id, title, content, qiita_article_id, state_flag)" +
                " VALUES(1,1,'test_title','test_content',null,1 );");

        jdbcTemplate.execute("INSERT INTO qiita_configurations(user_id, state, code) " +
                "VALUES(1,'state_test','code_test')");

        QiitaConfiguration qiitaConfiguration = new QiitaConfiguration();
        qiitaConfiguration.setUserId(1);
        qiitaConfiguration.setState("state_test_updated");
        qiitaConfiguration.setCode("code_test");

        mapper.updateState(qiitaConfiguration);

        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT * FROM qiita_configurations WHERE user_id = 1");

        assertEquals("state_test_updated",result.get("state"));
    }

    @Test
    void saveToken() {
        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO articles (article_id, user_id, title, content, qiita_article_id, state_flag)" +
                " VALUES(1,1,'test_title','test_content',null,1 );");

        jdbcTemplate.execute("INSERT INTO qiita_configurations(user_id, state, code) " +
                "VALUES(1,'state_test','code_test')");

        mapper.saveToken("token",1);

        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT * FROM qiita_configurations WHERE user_id = 1");

        assertEquals("token",result.get("token"));
    }
}
