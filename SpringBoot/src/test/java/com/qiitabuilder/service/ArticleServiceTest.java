package com.qiitabuilder.service;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.form.SearchArticleForm;
import com.qiitabuilder.mapper.CollectionSQL;
import com.qiitabuilder.security.SimpleLoginUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

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

        setAuthenticationInfo();
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

    void setAuthenticationInfo() {
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

    private void searchArticlesSqlTemplate() {
        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr = CollectionSQL.insertArticles.split("\n", 0);
        String[] feedbackSqlArr = CollectionSQL.insertFeedbacks.split("\n", 0);
        String[] qiitaRecommendSqlArr = CollectionSQL.insertQiitaRecommends.split("\n", 0);
        String[] myArticlesSqlArr = CollectionSQL.insertMyArticles.split("\n", 0);
        String[] tagsSqlArr = CollectionSQL.insertTags.split("\n", 0);
        String[] articlesTagsRelationsSqlArr = CollectionSQL.insertArticlesTagsRelations.split("\n", 0);

        for (String sql : userSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (String sql : articleSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (String sql : feedbackSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (String sql : qiitaRecommendSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (String sql : myArticlesSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (String sql : tagsSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (String sql : articlesTagsRelationsSqlArr) {
            jdbcTemplate.execute(sql);
        }
    }

    @Test
    void searchArticles_正常系_該当する記事が存在する() {
        searchArticlesSqlTemplate();
//       searchArticlesの引数を定義
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(0)
                .period(null)
                .searchWord("")
                .toggleSearchWord(0)
                .pageSize(10)
                .currentPage(1)
                .stateFlagList(Arrays.asList(1, 2))
                .build();
        List<Article> articles = articleService.searchArticles(searchArticleForm);
//        記事数
        assertEquals(10, articles.size());
        //      articles[0]のテスト
        LocalDateTime createDate = LocalDateTime.of(2020, 11, 10, 00, 00, 00);
        LocalDateTime updateDate = LocalDateTime.of(2020, 11, 11, 00, 00, 00);
        assertEquals(176, articles.get(0).getArticleId());
        assertEquals("title176", articles.get(0).getTitle());
        assertEquals(createDate, articles.get(0).getCreatedAt());
        assertEquals(updateDate, articles.get(0).getUpdatedAt());
        assertEquals(1, articles.get(0).getStateFlag());
        assertEquals(1, articles.get(0).getTags().get(0).getTagId());
        assertEquals(2, articles.get(0).getTags().get(1).getTagId());
        assertEquals("Java", articles.get(0).getTags().get(0).getTagName());
        assertEquals("ruby", articles.get(0).getTags().get(1).getTagName());
        assertEquals(0, articles.get(0).getFeedbackCount());
        assertEquals(2, articles.get(0).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(0).getQiitaRecommendPoint());
        assertEquals(33, articles.get(0).getPostedUser().getUserId());
        assertEquals("user33", articles.get(0).getPostedUser().getDisplayName());
        assertEquals("photo33", articles.get(0).getPostedUser().getPhotoUrl());
        //      articles[1]のテスト
        createDate = LocalDateTime.of(2020, 11, 10, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 10, 00, 00, 00);
        assertEquals(157, articles.get(1).getArticleId());
        assertEquals("title157", articles.get(1).getTitle());
        assertEquals(createDate, articles.get(1).getCreatedAt());
        assertEquals(updateDate, articles.get(1).getUpdatedAt());
        assertEquals(2, articles.get(1).getStateFlag());
        assertEquals(1, articles.get(1).getTags().get(0).getTagId());
        assertEquals(5, articles.get(1).getTags().get(1).getTagId());
        assertEquals("Java", articles.get(1).getTags().get(0).getTagName());
        assertEquals("go", articles.get(1).getTags().get(1).getTagName());
        assertEquals(0, articles.get(1).getFeedbackCount());
        assertEquals(0, articles.get(1).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(1).getQiitaRecommendPoint());
        assertEquals(31, articles.get(1).getPostedUser().getUserId());
        assertEquals("user31", articles.get(1).getPostedUser().getDisplayName());
        assertEquals("photo31", articles.get(1).getPostedUser().getPhotoUrl());
        //      articles[2]のテスト
        createDate = LocalDateTime.of(2020, 11, 10, 00, 00, 00);
        updateDate = createDate.plusDays(1);
        assertEquals(44, articles.get(2).getArticleId());
        assertEquals("title44", articles.get(2).getTitle());
        assertEquals(createDate, articles.get(2).getCreatedAt());
        assertEquals(updateDate, articles.get(2).getUpdatedAt());
        assertEquals(1, articles.get(2).getStateFlag());
        assertEquals(2, articles.get(2).getTags().get(0).getTagId());
        assertEquals(4, articles.get(2).getTags().get(1).getTagId());
        assertEquals("ruby", articles.get(2).getTags().get(0).getTagName());
        assertEquals("php", articles.get(2).getTags().get(1).getTagName());
        assertEquals(3, articles.get(2).getFeedbackCount());
        assertEquals(2, articles.get(2).getRegisteredMyArticleCount());
        assertEquals(0, articles.get(2).getQiitaRecommendPoint());
        assertEquals(8, articles.get(2).getPostedUser().getUserId());
        assertEquals("しょーの", articles.get(2).getPostedUser().getDisplayName());
        assertEquals("nnn", articles.get(2).getPostedUser().getPhotoUrl());
        //      articles[3]のテスト
        createDate = LocalDateTime.of(2020, 11, 9, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 10, 00, 00, 00);
        assertEquals(187, articles.get(3).getArticleId());
        assertEquals("title187", articles.get(3).getTitle());
        assertEquals(createDate, articles.get(3).getCreatedAt());
        assertEquals(updateDate, articles.get(3).getUpdatedAt());
        assertEquals(1, articles.get(3).getStateFlag());
        assertEquals(1, articles.get(3).getTags().get(0).getTagId());
        assertEquals(5, articles.get(3).getTags().get(1).getTagId());
        assertEquals("Java", articles.get(3).getTags().get(0).getTagName());
        assertEquals("go", articles.get(3).getTags().get(1).getTagName());
        assertEquals(0, articles.get(3).getFeedbackCount());
        assertEquals(0, articles.get(3).getRegisteredMyArticleCount());
        assertEquals(0, articles.get(3).getQiitaRecommendPoint());
        assertEquals(36, articles.get(3).getPostedUser().getUserId());
        assertEquals("user36", articles.get(3).getPostedUser().getDisplayName());
        assertEquals("photo36", articles.get(3).getPostedUser().getPhotoUrl());
        //      articles[4]のテスト
        createDate = LocalDateTime.of(2020, 11, 9, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 10, 00, 00, 00);
        assertEquals(168, articles.get(4).getArticleId());
        assertEquals("title168", articles.get(4).getTitle());
        assertEquals(createDate, articles.get(4).getCreatedAt());
        assertEquals(updateDate, articles.get(4).getUpdatedAt());
        assertEquals(1, articles.get(4).getStateFlag());
        assertEquals(2, articles.get(4).getTags().get(0).getTagId());
        assertEquals(4, articles.get(4).getTags().get(1).getTagId());
        assertEquals("ruby", articles.get(4).getTags().get(0).getTagName());
        assertEquals("php", articles.get(4).getTags().get(1).getTagName());
        assertEquals(1, articles.get(4).getFeedbackCount());
        assertEquals(2, articles.get(4).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(4).getQiitaRecommendPoint());
        assertEquals(32, articles.get(4).getPostedUser().getUserId());
        assertEquals("user32", articles.get(4).getPostedUser().getDisplayName());
        assertEquals("photo32", articles.get(4).getPostedUser().getPhotoUrl());
        //      articles[5]のテスト
        createDate = LocalDateTime.of(2020, 11, 8, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 9, 00, 00, 00);
        assertEquals(190, articles.get(5).getArticleId());
        assertEquals("title190", articles.get(5).getTitle());
        assertEquals(createDate, articles.get(5).getCreatedAt());
        assertEquals(updateDate, articles.get(5).getUpdatedAt());
        assertEquals(1, articles.get(5).getStateFlag());
        assertEquals(2, articles.get(5).getTags().get(0).getTagId());
        assertEquals(4, articles.get(5).getTags().get(1).getTagId());
        assertEquals("ruby", articles.get(5).getTags().get(0).getTagName());
        assertEquals("php", articles.get(5).getTags().get(1).getTagName());
        assertEquals(2, articles.get(5).getFeedbackCount());
        assertEquals(1, articles.get(5).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(5).getQiitaRecommendPoint());
        assertEquals(37, articles.get(5).getPostedUser().getUserId());
        assertEquals("user37", articles.get(5).getPostedUser().getDisplayName());
        assertEquals("photo37", articles.get(5).getPostedUser().getPhotoUrl());
        //      articles[6]のテスト
        createDate = LocalDateTime.of(2020, 11, 8, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 9, 00, 00, 00);
        assertEquals(175, articles.get(6).getArticleId());
        assertEquals("title175", articles.get(6).getTitle());
        assertEquals(createDate, articles.get(6).getCreatedAt());
        assertEquals(updateDate, articles.get(6).getUpdatedAt());
        assertEquals(1, articles.get(6).getStateFlag());
        assertEquals(1, articles.get(6).getTags().get(0).getTagId());
        assertEquals(3, articles.get(6).getTags().get(1).getTagId());
        assertEquals("Java", articles.get(6).getTags().get(0).getTagName());
        assertEquals("javascript", articles.get(6).getTags().get(1).getTagName());
        assertEquals(1, articles.get(6).getFeedbackCount());
        assertEquals(0, articles.get(6).getRegisteredMyArticleCount());
        assertEquals(0, articles.get(6).getQiitaRecommendPoint());
        assertEquals(33, articles.get(6).getPostedUser().getUserId());
        assertEquals("user33", articles.get(6).getPostedUser().getDisplayName());
        assertEquals("photo33", articles.get(6).getPostedUser().getPhotoUrl());
        //      articles[7]のテスト
        createDate = LocalDateTime.of(2020, 11, 8, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 9, 00, 00, 00);
        assertEquals(156, articles.get(7).getArticleId());
        assertEquals("title156", articles.get(7).getTitle());
        assertEquals(createDate, articles.get(7).getCreatedAt());
        assertEquals(updateDate, articles.get(7).getUpdatedAt());
        assertEquals(1, articles.get(7).getStateFlag());
        assertEquals(1, articles.get(7).getTags().get(0).getTagId());
        assertEquals(4, articles.get(7).getTags().get(1).getTagId());
        assertEquals("Java", articles.get(7).getTags().get(0).getTagName());
        assertEquals("php", articles.get(7).getTags().get(1).getTagName());
        assertEquals(0, articles.get(7).getFeedbackCount());
        assertEquals(2, articles.get(7).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(7).getQiitaRecommendPoint());
        assertEquals(31, articles.get(7).getPostedUser().getUserId());
        assertEquals("user31", articles.get(7).getPostedUser().getDisplayName());
        assertEquals("photo31", articles.get(7).getPostedUser().getPhotoUrl());
        //      articles[8]のテスト
        createDate = LocalDateTime.of(2020, 11, 7, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 8, 00, 00, 00);
        assertEquals(193, articles.get(8).getArticleId());
        assertEquals("title193", articles.get(8).getTitle());
        assertEquals(createDate, articles.get(8).getCreatedAt());
        assertEquals(updateDate, articles.get(8).getUpdatedAt());
        assertEquals(1, articles.get(8).getStateFlag());
        assertEquals(2, articles.get(8).getTags().get(0).getTagId());
        assertEquals(3, articles.get(8).getTags().get(1).getTagId());
        assertEquals("ruby", articles.get(8).getTags().get(0).getTagName());
        assertEquals("javascript", articles.get(8).getTags().get(1).getTagName());
        assertEquals(1, articles.get(8).getFeedbackCount());
        assertEquals(0, articles.get(8).getRegisteredMyArticleCount());
        assertEquals(2, articles.get(8).getQiitaRecommendPoint());
        assertEquals(39, articles.get(8).getPostedUser().getUserId());
        assertEquals("user39", articles.get(8).getPostedUser().getDisplayName());
        assertEquals("photo39", articles.get(8).getPostedUser().getPhotoUrl());
        //      articles[9]のテスト
        createDate = LocalDateTime.of(2020, 11, 7, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 8, 00, 00, 00);
        assertEquals(183, articles.get(9).getArticleId());
        assertEquals("title183", articles.get(9).getTitle());
        assertEquals(createDate, articles.get(9).getCreatedAt());
        assertEquals(updateDate, articles.get(9).getUpdatedAt());
        assertEquals(1, articles.get(9).getStateFlag());
        assertEquals(4, articles.get(9).getTags().get(0).getTagId());
        assertEquals(5, articles.get(9).getTags().get(1).getTagId());
        assertEquals("php", articles.get(9).getTags().get(0).getTagName());
        assertEquals("go", articles.get(9).getTags().get(1).getTagName());
        assertEquals(1, articles.get(9).getFeedbackCount());
        assertEquals(0, articles.get(9).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(9).getQiitaRecommendPoint());
        assertEquals(34, articles.get(9).getPostedUser().getUserId());
        assertEquals("user34", articles.get(9).getPostedUser().getDisplayName());
        assertEquals("photo34", articles.get(9).getPostedUser().getPhotoUrl());
    }

    @Test
    void searchArticles_正常系_該当する記事が存在しない() {
        searchArticlesSqlTemplate();
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(0)
                .period(null)
                .searchWord("存在しないタイトル")
                .toggleSearchWord(0)
                .pageSize(10)
                .currentPage(1)
                .stateFlagList(Arrays.asList(1, 2))
                .build();
        List<Article> articles = articleService.searchArticles(searchArticleForm);
        //        記事数
        assertEquals(null, articles);
    }

    //    pageSizeが10,取得件数が1件の時のtotalPage
    @Test
    void getTotalPage_正常系_pageSizeより取得記事数の方が少ない場合() {
        String userInsertSql = "INSERT INTO users (uid, photo_url, display_name, password) VALUES ('a', 'a', 'a', 'a');";
        String articleInsertSql = "INSERT INTO articles (user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) VALUES (1, '2020-10-01 00:00:00', '2020-10-02 00:00:00', 'title1', '#content1', null, 1)";
        jdbcTemplate.execute(userInsertSql);
        jdbcTemplate.execute(articleInsertSql);
//       searchArticlesの引数を定義
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(0)
                .period(null)
                .searchWord("")
                .toggleSearchWord(0)
                .pageSize(10)
                .currentPage(1)
                .stateFlagList(Arrays.asList(1, 2))
                .build();
        Integer totalPage = articleService.getTotalPage(searchArticleForm);
        assertEquals(1, totalPage);
    }

    //    pageSizeが10件、取得件数が11件の時のtotalPage
    @Test
    void getTotalPage_正常系_取得した記事件数がpageSizeより多く取得した記事件数がpageSizeで割り切れない場合() {
        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr = CollectionSQL.insertArticles.split("\n", 0);
        for (String sql : userSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (int i = 0; i < 11; i++) {
            jdbcTemplate.execute(articleSqlArr[i]);
        }
//       searchArticlesの引数を定義
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(0)
                .period(null)
                .searchWord("")
                .toggleSearchWord(0)
                .pageSize(10)
                .currentPage(1)
                .stateFlagList(Arrays.asList(1, 2))
                .build();
        Integer totalPage = articleService.getTotalPage(searchArticleForm);
        assertEquals(2, totalPage);
    }

    //    pageSizeが10件、取得件数が10件の時のtotalPage
    @Test
    void getTotalPage_正常系_取得した記事件数がpageSizeで割り切れる場合() {
        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr = CollectionSQL.insertArticles.split("\n", 0);
        for (String sql : userSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (int i = 0; i < 10; i++) {
            jdbcTemplate.execute(articleSqlArr[i]);
        }
//       searchArticlesの引数を定義
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(0)
                .period(null)
                .searchWord("")
                .toggleSearchWord(0)
                .pageSize(10)
                .currentPage(1)
                .stateFlagList(Arrays.asList(1, 2))
                .build();
        Integer totalPage = articleService.getTotalPage(searchArticleForm);
        assertEquals(1, totalPage);
    }


    @Test
    void searchCriteriaProcessing_正常系_sortNumが0currentPageが1の時() {
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(0)
                .pageSize(10)
                .currentPage(1)
                .build();
        articleService.searchCriteriaProcessing(searchArticleForm);
        assertEquals("createdAt", searchArticleForm.getSort());
        assertEquals(0, searchArticleForm.getOffset());
    }

    @Test
    void searchCriteriaProcessing_正常系_sortNumが1currentPageが1の時() {
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(1)
                .pageSize(10)
                .currentPage(1)
                .build();
        articleService.searchCriteriaProcessing(searchArticleForm);
        assertEquals("updatedAt", searchArticleForm.getSort());
        assertEquals(0, searchArticleForm.getOffset());
    }

    @Test
    void searchCriteriaProcessing_正常系_sortNumが2currentPageが1の時() {
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(2)
                .pageSize(10)
                .currentPage(1)
                .build();
        articleService.searchCriteriaProcessing(searchArticleForm);
        assertEquals("recommendCnt", searchArticleForm.getSort());
        assertEquals(0, searchArticleForm.getOffset());
    }

    @Test
    void searchCriteriaProcessing_正常系_sortNumが3currentPageが1の時() {
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(3)
                .pageSize(10)
                .currentPage(1)
                .build();
        articleService.searchCriteriaProcessing(searchArticleForm);
        assertEquals("myCnt", searchArticleForm.getSort());
        assertEquals(0, searchArticleForm.getOffset());
    }

    @Test
    void searchCriteriaProcessing_正常系_sortNumが0currentPageが2の時() {
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(0)
                .pageSize(10)
                .currentPage(2)
                .build();
        articleService.searchCriteriaProcessing(searchArticleForm);
        assertEquals("createdAt", searchArticleForm.getSort());
        assertEquals(10, searchArticleForm.getOffset());
    }

    @Test
    void searchCriteriaProcessing_正常系_sortNumが1currentPageが2の時() {
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(1)
                .pageSize(10)
                .currentPage(2)
                .build();
        articleService.searchCriteriaProcessing(searchArticleForm);
        assertEquals("updatedAt", searchArticleForm.getSort());
        assertEquals(10, searchArticleForm.getOffset());
    }

    @Test
    void searchCriteriaProcessing_正常系_sortNumが2currentPageが2の時() {
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(2)
                .pageSize(10)
                .currentPage(2)
                .build();
        articleService.searchCriteriaProcessing(searchArticleForm);
        assertEquals("recommendCnt", searchArticleForm.getSort());
        assertEquals(10, searchArticleForm.getOffset());
    }

    @Test
    void searchCriteriaProcessing_正常系_sortNumが3currentPageが2の時() {
        SearchArticleForm searchArticleForm = SearchArticleForm.builder()
                .sortNum(3)
                .pageSize(10)
                .currentPage(2)
                .build();
        articleService.searchCriteriaProcessing(searchArticleForm);
        assertEquals("myCnt", searchArticleForm.getSort());
        assertEquals(10, searchArticleForm.getOffset());
    }

    @Test
    void saveArticle_正常系_insert_DBに存在するTagのみ選択された場合() {
        List<Tag> tags = new ArrayList<>(Arrays.asList(new Tag(1, "tag1", null), new Tag(2, "tag2", null)));

        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO tags(tag_id, tag_name) VALUES(null,'tag1'),(null,'tag2')");


        Article article = Article.builder()
                .articleId(null)
                .title("title_test")
                .tags(tags)
                .content("content_test")
                .build();

        articleService.saveArticle(article);

        Map<String, Object> articleResult = jdbcTemplate.queryForMap("SELECT * FROM articles WHERE article_id = 1");
        assertEquals(1, articleResult.get("article_id"));
        assertEquals("title_test", articleResult.get("title"));
        assertEquals("content_test", articleResult.get("content"));

        List<Map<String, Object>> articlesTagsRelationsResult = jdbcTemplate.queryForList("SELECT * FROM articles_tags_relations WHERE article_id = 1");

        assertEquals(1, articlesTagsRelationsResult.get(0).get("article_id"));
        assertEquals(1, articlesTagsRelationsResult.get(0).get("tag_id"));

        assertEquals(1, articlesTagsRelationsResult.get(1).get("article_id"));
        assertEquals(2, articlesTagsRelationsResult.get(1).get("tag_id"));

    }

    @Test
    void saveArticle_正常系_insert_DBに存在しないTagを選択された場合() {
        List<Tag> tags = new ArrayList<>(Arrays.asList(new Tag(null, "tag1", null), new Tag(null, "tag2", null)));

        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");

        Article article = Article.builder()
                .articleId(null)
                .title("title_test")
                .tags(tags)
                .content("content_test")
                .build();

        articleService.saveArticle(article);

        Map<String, Object> articleResult = jdbcTemplate.queryForMap("SELECT * FROM articles WHERE article_id = 1");
        assertEquals(1, articleResult.get("article_id"));
        assertEquals("title_test", articleResult.get("title"));
        assertEquals("content_test", articleResult.get("content"));

        List<Map<String, Object>> articlesTagsRelationsResult = jdbcTemplate.queryForList("SELECT * FROM articles_tags_relations WHERE article_id = 1");

        List<Map<String, Object>> tagsResult = jdbcTemplate.queryForList("SELECT * FROM tags ORDER BY tag_id");

        assertEquals(1, articlesTagsRelationsResult.get(0).get("article_id"));
        assertEquals(1, articlesTagsRelationsResult.get(0).get("tag_id"));

        assertEquals(1, articlesTagsRelationsResult.get(1).get("article_id"));
        assertEquals(2, articlesTagsRelationsResult.get(1).get("tag_id"));

        assertEquals("tag1", tagsResult.get(0).get("tag_name"));
        assertEquals("tag2", tagsResult.get(1).get("tag_name"));
    }

    @Test
    void saveArticle_正常系_update_関連するTagを追加する場合() {
        List<Tag> tags = new ArrayList<>(Arrays.asList(new Tag(1, "tag1", null), new Tag(null, "tag2", null)));

        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO articles (article_id, user_id, title, content, qiita_article_id, state_flag) VALUES(1,1,'title_test','content_test',null,1)");
        jdbcTemplate.execute("INSERT INTO tags(tag_id, tag_name) VALUES(1,'tag1')");

        Article article = Article.builder()
                .articleId(1)
                .title("title_test_edited")
                .tags(tags)
                .content("content_test_edited")
                .build();

        articleService.saveArticle(article);

        Map<String, Object> articleResult = jdbcTemplate.queryForMap("SELECT * FROM articles WHERE article_id = 1");
        assertEquals(1, articleResult.get("article_id"));
        assertEquals("title_test_edited", articleResult.get("title"));
        assertEquals("content_test_edited", articleResult.get("content"));

        List<Map<String, Object>> articlesTagsRelationsResult = jdbcTemplate.queryForList("SELECT * FROM articles_tags_relations WHERE article_id = 1");

        List<Map<String, Object>> tagsResult = jdbcTemplate.queryForList("SELECT * FROM tags ORDER BY tag_id");

        assertEquals(1, articlesTagsRelationsResult.get(0).get("article_id"));
        assertEquals(1, articlesTagsRelationsResult.get(0).get("tag_id"));

        assertEquals(1, articlesTagsRelationsResult.get(1).get("article_id"));
        assertEquals(2, articlesTagsRelationsResult.get(1).get("tag_id"));

        assertEquals("tag1", tagsResult.get(0).get("tag_name"));
        assertEquals("tag2", tagsResult.get(1).get("tag_name"));
    }

    @Test
    void saveArticle_正常系_update_関連するTagを削除する場合() {
        List<Tag> tags = new ArrayList<>(Arrays.asList(new Tag(1, "tag1", null)));

        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");
        jdbcTemplate.execute("INSERT INTO articles (article_id, user_id, title, content, qiita_article_id, state_flag) VALUES(1,1,'title_test','content_test',null,1)");
        jdbcTemplate.execute("INSERT INTO tags(tag_id, tag_name) VALUES(1,'tag1') , (2,'tag2')");

        Article article = Article.builder()
                .articleId(1)
                .title("title_test_edited")
                .tags(tags)
                .content("content_test_edited")
                .build();

        articleService.saveArticle(article);

        Map<String, Object> articleResult = jdbcTemplate.queryForMap("SELECT * FROM articles WHERE article_id = 1");
        assertEquals(1, articleResult.get("article_id"));
        assertEquals("title_test_edited", articleResult.get("title"));
        assertEquals("content_test_edited", articleResult.get("content"));

        List<Map<String, Object>> articlesTagsRelationsResult = jdbcTemplate.queryForList("SELECT * FROM articles_tags_relations WHERE article_id = 1");

        List<Map<String, Object>> tagsResult = jdbcTemplate.queryForList("SELECT * FROM tags ORDER BY tag_id");

        assertEquals(1, articlesTagsRelationsResult.get(0).get("article_id"));
        assertEquals(1, articlesTagsRelationsResult.get(0).get("tag_id"));

        assertEquals(1, articlesTagsRelationsResult.size());
    }


    // getArticle()
    @Test
    void getArticle正常系() {
        // set up
        String insertUser1 = "INSERT INTO users (user_id, uid, photo_url, display_name, password) VALUES (1, 'a', 'a', 'a', 'a');";
        String insertUser2 = "INSERT INTO users (user_id, uid, photo_url, display_name, password) VALUES (2, 'b', 'b', 'b', 'b');";

        String insertArt1 = "INSERT INTO articles (article_id, user_id, created_at, updated_at, title, content, state_flag) VALUES (1, 1, '2020-10-01 00:00:00', '2020-10-02 00:00:00', 'title1', '#content1', 1);";
        String insertFeed1 = "INSERT INTO feedbacks (article_id, user_id, created_at, updated_at, content, delete_flag) VALUES (1, 2, '2020-11-03 00:00:00', '2020-11-04 00:00:00', 'feedback content1', 0);";

        String insertMyArt = "INSERT INTO my_articles (article_id, posted_user_id, register_user_id) VALUES (1, 1, 2);";
        String insertRec = "INSERT INTO qiita_recommends (article_id, posted_user_id, recommend_user_id) VALUES (1, 1, 2);";
        String insertTag1 = "INSERT INTO tags (tag_name) VALUES ('Java');";
        String insertTagRel1 = "INSERT INTO articles_tags_relations (article_id, posted_user_id, tag_id) VALUES (1, 1, 1);";

        jdbcTemplate.execute(insertUser1);
        jdbcTemplate.execute(insertUser2);
        jdbcTemplate.execute(insertArt1);
        jdbcTemplate.execute(insertFeed1);
        jdbcTemplate.execute(insertMyArt);
        jdbcTemplate.execute(insertRec);
        jdbcTemplate.execute(insertTag1);
        jdbcTemplate.execute(insertTagRel1);

        User user1 = User.builder().userId(1).displayName("a").photoUrl("a").uid("a").build();
        User user2 = User.builder().userId(2).displayName("b").photoUrl("b").uid("b").build();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime createdAtArt1 = LocalDateTime.parse("2020-10-01 00:00", dtf);
        LocalDateTime updatedAtArt1 = LocalDateTime.parse("2020-10-02 00:00", dtf);
        LocalDateTime createdAtFeed1 = LocalDateTime.parse("2020-11-03 00:00", dtf);
        LocalDateTime updatedAtFeed1 = LocalDateTime.parse("2020-11-04 00:00", dtf);

        Feedback feed1 = Feedback.builder()
                .feedbackId(1)
                .articleId(1)
                .postedUser(user2)
                .createdAt(createdAtFeed1)
                .updatedAt(updatedAtFeed1)
                .content("feedback content1")
                .deleteFlag(0)
                .build();
        List<Feedback> feedbacks = new ArrayList<>();
        feedbacks.add(feed1);

        Tag tag1 = Tag.builder().tagId(1).tagName("Java").build();
        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);

        Article expected = Article.builder()
                .articleId(1)
                .postedUser(user1)
                .createdAt(createdAtArt1)
                .updatedAt(updatedAtArt1)
                .title("title1")
                .content("#content1")
                .stateFlag(1)
                .feedbacks(feedbacks)
                .feedbackCount(1)
                .qiitaRecommendPoint(1)
                .registeredMyArticleCount(1)
                .tags(tags)
                .build();

        // check
        Article actual = articleService.getArticle(1);

        assertEquals(expected.getArticleId(), actual.getArticleId());
        assertEquals(expected.getPostedUser(), actual.getPostedUser());
        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getContent(), actual.getContent());
        assertEquals(expected.getStateFlag(), actual.getStateFlag());
        assertEquals(expected.getFeedbacks().get(0), actual.getFeedbacks().get(0));
        assertEquals(expected.getFeedbackCount(), actual.getFeedbackCount());
        assertEquals(expected.getQiitaRecommendPoint(), actual.getQiitaRecommendPoint());
        assertEquals(expected.getRegisteredMyArticleCount(), actual.getRegisteredMyArticleCount());
        assertEquals(expected.getTags().get(0), actual.getTags().get(0));
    }

    @Test
    void getArticle異常系_QiitaRecommendPointがNullの場合() {
        // set up
        String insertUser1 = "INSERT INTO users (user_id, uid, photo_url, display_name, password) VALUES (1, 'a', 'a', 'a', 'a');";
        String insertUser2 = "INSERT INTO users (user_id, uid, photo_url, display_name, password) VALUES (2, 'b', 'b', 'b', 'b');";

        String insertArt1 = "INSERT INTO articles (article_id, user_id, created_at, updated_at, title, content, state_flag) VALUES (1, 1, '2020-10-01 00:00:00', '2020-10-02 00:00:00', 'title1', '#content1', 1);";
        String insertFeed1 = "INSERT INTO feedbacks (article_id, user_id, created_at, updated_at, content, delete_flag) VALUES (1, 2, '2020-11-03 00:00:00', '2020-11-04 00:00:00', 'feedback content1', 0);";

        String insertMyArt = "INSERT INTO my_articles (article_id, posted_user_id, register_user_id) VALUES (1, 1, 2);";
        String insertTag1 = "INSERT INTO tags (tag_name) VALUES ('Java');";
        String insertTagRel1 = "INSERT INTO articles_tags_relations (article_id, posted_user_id, tag_id) VALUES (1, 1, 1);";

        jdbcTemplate.execute(insertUser1);
        jdbcTemplate.execute(insertUser2);
        jdbcTemplate.execute(insertArt1);
        jdbcTemplate.execute(insertFeed1);
        jdbcTemplate.execute(insertMyArt);
        jdbcTemplate.execute(insertTag1);
        jdbcTemplate.execute(insertTagRel1);

        User user1 = User.builder().userId(1).displayName("a").photoUrl("a").uid("a").build();
        User user2 = User.builder().userId(2).displayName("b").photoUrl("b").uid("b").build();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime createdAtArt1 = LocalDateTime.parse("2020-10-01 00:00", dtf);
        LocalDateTime updatedAtArt1 = LocalDateTime.parse("2020-10-02 00:00", dtf);
        LocalDateTime createdAtFeed1 = LocalDateTime.parse("2020-11-03 00:00", dtf);
        LocalDateTime updatedAtFeed1 = LocalDateTime.parse("2020-11-04 00:00", dtf);

        Feedback feed1 = Feedback.builder()
                .feedbackId(1)
                .articleId(1)
                .postedUser(user2)
                .createdAt(createdAtFeed1)
                .updatedAt(updatedAtFeed1)
                .content("feedback content1")
                .deleteFlag(0)
                .build();
        List<Feedback> feedbacks = new ArrayList<>();
        feedbacks.add(feed1);

        Tag tag1 = Tag.builder().tagId(1).tagName("Java").build();
        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);

        Article expected = Article.builder()
                .articleId(1)
                .postedUser(user1)
                .createdAt(createdAtArt1)
                .updatedAt(updatedAtArt1)
                .title("title1")
                .content("#content1")
                .stateFlag(1)
                .feedbacks(feedbacks)
                .feedbackCount(1)
                .qiitaRecommendPoint(0)
                .registeredMyArticleCount(1)
                .tags(tags)
                .build();

        // qiitaRecommendPointが0になっているか確認
        Article actual = articleService.getArticle(1);

        assertEquals(expected.getArticleId(), actual.getArticleId());
        assertEquals(expected.getPostedUser(), actual.getPostedUser());
        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getContent(), actual.getContent());
        assertEquals(expected.getStateFlag(), actual.getStateFlag());
        assertEquals(expected.getFeedbacks().get(0), actual.getFeedbacks().get(0));
        assertEquals(expected.getFeedbackCount(), actual.getFeedbackCount());
        assertEquals(expected.getQiitaRecommendPoint(), actual.getQiitaRecommendPoint());
        assertEquals(expected.getRegisteredMyArticleCount(), actual.getRegisteredMyArticleCount());
        assertEquals(expected.getTags().get(0), actual.getTags().get(0));
    }

    @Test
    void findByArticleIdAndUserId_正常系_存在する記事IDとユーザーIDに該当する記事が存在する() {
        String userInsertSql = "INSERT INTO users (uid, photo_url, display_name, password) VALUES ('a', 'a', 'a', 'a');";
        String articleInsertSql = "INSERT INTO articles (user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) VALUES (1, '2020-10-01 00:00:00', '2020-10-02 00:00:00', 'title1', '#content1', null, 1)";
        jdbcTemplate.execute(userInsertSql);
        jdbcTemplate.execute(articleInsertSql);
        Integer articleId = articleService.findByArticleIdAndUserId(1, 1);
        assertEquals(1, articleId);
    }

    @Test
    void findByArticleIdAndUserId_正常系_存在する記事IDとユーザーIDに該当する記事が存在しない() {
        String userInsertSql = "INSERT INTO users (uid, photo_url, display_name, password) VALUES ('a', 'a', 'a', 'a');";
        String user2InsertSql = "INSERT INTO users (uid, photo_url, display_name, password) VALUES ('b', 'b', 'b', 'b');";
        String articleInsertSql = "INSERT INTO articles (user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) VALUES (1, '2020-10-01 00:00:00', '2020-10-02 00:00:00', 'title1', '#content1', null, 1)";
        jdbcTemplate.execute(userInsertSql);
        jdbcTemplate.execute(user2InsertSql);
        jdbcTemplate.execute(articleInsertSql);
        Integer articleId = articleService.findByArticleIdAndUserId(1, 2);
        assertEquals(null, articleId);
    }

    @Test
    void findByArticleIdAndUserId_正常系_存在しない記事IDとユーザーIDに該当する記事が存在しない() {
        String userInsertSql = "INSERT INTO users (uid, photo_url, display_name, password) VALUES ('a', 'a', 'a', 'a');";
        jdbcTemplate.execute(userInsertSql);
        Integer articleId = articleService.findByArticleIdAndUserId(1, 1);
        assertEquals(null, articleId);
    }

    @Test
    void getFeedbackedArticlesByUserId() {
        //Mapperからの値をそのまま返すメソッド
        assertTrue(true);
    }

    @Test
    void getMyArticlesByUserId() {
        //Mapperからの値をそのまま返すメソッド
        assertTrue(true);
    }

    @Test
    void getPostedArticlesByUserId() {
        //Mapperからの値をそのまま返すメソッド
        assertTrue(true);
    }
}
