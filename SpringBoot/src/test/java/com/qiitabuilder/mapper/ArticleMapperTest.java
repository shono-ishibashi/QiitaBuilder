package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.domain.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.*;

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
    private NamedParameterJdbcTemplate jdbcTemplate;

    private void beforeEach(){
        String resetSql
                = "DROP TABLE IF EXISTS qiita_configurations;" +
                "DROP TABLE IF EXISTS qiita_recommends;" +
                "DROP TABLE IF EXISTS my_articles;" +
                "DROP TABLE IF EXISTS feedbacks;" +
                "DROP TABLE IF EXISTS articles_tags_relations;" +
                "DROP TABLE IF EXISTS tags;" +
                "DROP TABLE IF EXISTS articles;" +
                "DROP TABLE IF EXISTS users;" +
                "create table users" +
                "(" +
                "    user_id      int auto_increment" +
                "        primary key," +
                "    uid          text null," +
                "    photo_url    text null," +
                "    display_name text null," +
                "    password     text null" +
                ");" +
                "create table articles" +
                "(" +
                "    article_id       int auto_increment" +
                "        primary key," +
                "    user_id          int          null," +
                "    created_at       datetime     null," +
                "    updated_at       datetime     null," +
                "    title            varchar(100) null," +
                "    content          text         null," +
                "    qiita_article_id text         null," +
                "    state_flag       int          null," +
                "    constraint fk_articles_userid" +
                "        foreign key (user_id) references users (user_id)" +
                ");" +
                "" +
                "create table tags" +
                "(" +
                "    tag_id   int auto_increment" +
                "        primary key," +
                "    tag_name text null" +
                ");" +
                "" +
                "create table articles_tags_relations" +
                "(" +
                "    tags_relation_id int auto_increment" +
                "        primary key," +
                "    article_id       int null," +
                "    posted_user_id   int null," +
                "    tag_id           int null," +
                "    constraint fk_tags_relations_articleid" +
                "        foreign key (article_id) references articles (article_id)," +
                "    constraint fk_tags_relations_tagid" +
                "        foreign key (tag_id) references tags (tag_id)," +
                "    constraint fk_tags_relations_userid" +
                "        foreign key (posted_user_id) references articles (user_id)" +
                ");" +
                "" +
                "create table feedbacks" +
                "(" +
                "    feedback_id int auto_increment" +
                "        primary key," +
                "    article_id  int      null," +
                "    user_id     int      null," +
                "    created_at  datetime null," +
                "    updated_at  datetime null," +
                "    content     text     null," +
                "    delete_flag int      null," +
                "    constraint fk_feedbacks_articleid" +
                "        foreign key (article_id) references articles (article_id)," +
                "    constraint fk_feedbacks_userid" +
                "        foreign key (user_id) references users (user_id)" +
                ");" +
                "" +
                "create table my_articles" +
                "(" +
                "    my_article_id    int auto_increment" +
                "        primary key," +
                "    article_id       int null," +
                "    posted_user_id   int null," +
                "    register_user_id int null," +
                "    constraint fk_myarticles_articleid" +
                "        foreign key (article_id) references articles (article_id)," +
                "    constraint fk_myarticles_posted_userid" +
                "        foreign key (posted_user_id) references articles (user_id)," +
                "    constraint fk_myarticles_register_userid" +
                "        foreign key (register_user_id) references users (user_id)" +
                ");" +
                "" +
                "create table qiita_configurations" +
                "(" +
                "    qiita_cofiguration_id int auto_increment" +
                "        primary key," +
                "    user_id               int         not null," +
                "    state                 varchar(36) null," +
                "    code                  varchar(40) null," +
                "    token                 varchar(40) null," +
                "    constraint qiita_configurations_user_id_uindex" +
                "        unique (user_id)," +
                "    constraint qiita_configurations_users_user_id_fk" +
                "        foreign key (user_id) references users (user_id)" +
                ");" +
                "" +
                "create table qiita_recommends" +
                "(" +
                "    recommend_id      int auto_increment" +
                "        primary key," +
                "    article_id        int null," +
                "    posted_user_id    int null," +
                "    recommend_user_id int null," +
                "    constraint fk_qiitarecommends_articleid" +
                "        foreign key (article_id) references articles (article_id)," +
                "    constraint fk_qiitarecommends_posted_userid" +
                "        foreign key (posted_user_id) references articles (user_id)," +
                "    constraint fk_qiitarecommends_recommend_userid" +
                "        foreign key (recommend_user_id) references users (user_id)" +
                ");" +
                "";

        SqlParameterSource param = new EmptySqlParameterSource();


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
                .articleId(1)
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

        List<Map<String, Object>> resultArticles = jdbcTemplate.queryForList(articleSelectSql,param);

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
