package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.service.ArticleService;
import com.qiitabuilder.service.UserDetailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleControllerTest {

    @Mock
    private ArticleService articleService;
    @InjectMocks
    ArticleController articleController;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(articleController)
                .build();
        MockitoAnnotations.initMocks(this);
    }

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

    @Test
    void searchArticles() {
    }

    @Test
    void totalPage() {
    }

    @Test
    void fetchArticle() {
    }

    @Test
    void postArticle() {
    }

    @Test
    void editArticle() {
    }

    @Test
    void getFeedbackedArticlesByUserId_userIdがnullの処理() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/article/feedbacked/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void getFeedbackedArticlesByUserId_正常系() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.get("/article/feedbacked/?userId=1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void getMyArticlesByUserId_userIdがnullの処理() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/article/my-articles/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void getMyArticlesByUserId_正常系() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.get("/article/my-articles/?userId=1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void getArticlesByUserId_userIdがnullの処理() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/article/posted/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void getArticlesByUserId_正常系() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.get("/article/posted/?userId=1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}