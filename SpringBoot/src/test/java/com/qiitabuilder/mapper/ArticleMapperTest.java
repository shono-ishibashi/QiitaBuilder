package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.*;
import com.qiitabuilder.form.SearchArticleForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@MybatisTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleMapperTest {

    @Autowired
    private ArticleMapper articleMapper;

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

    private void searchArticlesSqlTemplate(int period) {
        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr;
        String[] feedbackSqlArr;
        String[] qiitaRecommendSqlArr;
        String[] myArticlesSqlArr;
        String[] tagsSqlArr;
        String[] articlesTagsRelationsSqlArr;
        if (period == 0) {
            articleSqlArr = CollectionSQL.insertArticlesInWeekly.split("\n", 0);
            feedbackSqlArr = CollectionSQL.insertFeedbacksInWeeklyAndMonthly.split("\n", 0);
            qiitaRecommendSqlArr = CollectionSQL.insertQiitaRecommendsInWeeklyAndMonthly.split("\n", 0);
            myArticlesSqlArr = CollectionSQL.insertMyArticlesInWeeklyAndMonthly.split("\n", 0);
            tagsSqlArr = CollectionSQL.insertTags.split("\n", 0);
            articlesTagsRelationsSqlArr = CollectionSQL.insertArticlesTagsRelationsInWeeklyAndMonthly.split("\n", 0);
        } else if (period == 1) {
            articleSqlArr = CollectionSQL.insertArticlesInMonthly.split("\n", 0);
            feedbackSqlArr = CollectionSQL.insertFeedbacksInWeeklyAndMonthly.split("\n", 0);
            qiitaRecommendSqlArr = CollectionSQL.insertQiitaRecommendsInWeeklyAndMonthly.split("\n", 0);
            myArticlesSqlArr = CollectionSQL.insertMyArticlesInWeeklyAndMonthly.split("\n", 0);
            tagsSqlArr = CollectionSQL.insertTags.split("\n", 0);
            articlesTagsRelationsSqlArr = CollectionSQL.insertArticlesTagsRelationsInWeeklyAndMonthly.split("\n", 0);
        } else {
            articleSqlArr = CollectionSQL.insertArticles.split("\n", 0);
            feedbackSqlArr = CollectionSQL.insertFeedbacks.split("\n", 0);
            qiitaRecommendSqlArr = CollectionSQL.insertQiitaRecommends.split("\n", 0);
            myArticlesSqlArr = CollectionSQL.insertMyArticles.split("\n", 0);
            tagsSqlArr = CollectionSQL.insertTags.split("\n", 0);
            articlesTagsRelationsSqlArr = CollectionSQL.insertArticlesTagsRelations.split("\n", 0);
        }

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

    private SearchArticleForm setSearchArticleForm(String sort, Integer pageSize, Integer offset, String searchWord, List<Integer> searchTag, Integer period, Integer toggleSearchWord, List<Integer> stateFlagList) {
        SearchArticleForm searchArticleForm = new SearchArticleForm();
        searchArticleForm.setSort(sort);
        searchArticleForm.setPageSize(pageSize);
        searchArticleForm.setOffset(offset);
        searchArticleForm.setSearchWord(searchWord);
        searchArticleForm.setSearchTag(searchTag);
        searchArticleForm.setPeriod(period);
        searchArticleForm.setToggleSearchWord(toggleSearchWord);
        searchArticleForm.setStateFlagList(stateFlagList);
        return searchArticleForm;
    }

    @Test
    void searchArticles正常系() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "", null, null, 0, Arrays.asList(1, 2));

        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
        searchArticleForm.setArticlesIdList(articleIdList);
        List<Article> articles = articleMapper.searchArticles(searchArticleForm);
//      articles[0]のテスト
        LocalDateTime createDate = LocalDateTime.of(2020, 11, 10, 00, 00, 00);
        LocalDateTime updateDate = createDate.plusDays(1);
        assertEquals(44, articles.get(0).getArticleId());
        assertEquals("title44", articles.get(0).getTitle());
        assertEquals(createDate, articles.get(0).getCreatedAt());
        assertEquals(updateDate, articles.get(0).getUpdatedAt());
        assertEquals(1, articles.get(0).getStateFlag());
        assertEquals(4, articles.get(0).getTags().get(0).getTagId());
        assertEquals(2, articles.get(0).getTags().get(1).getTagId());
        assertEquals("php", articles.get(0).getTags().get(0).getTagName());
        assertEquals("ruby", articles.get(0).getTags().get(1).getTagName());
        assertEquals(3, articles.get(0).getFeedbackCount());
        assertEquals(2, articles.get(0).getRegisteredMyArticleCount());
        assertEquals(null, articles.get(0).getQiitaRecommendPoint());
        assertEquals(8, articles.get(0).getPostedUser().getUserId());
        assertEquals("しょーの", articles.get(0).getPostedUser().getDisplayName());
        assertEquals("nnn", articles.get(0).getPostedUser().getPhotoUrl());
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
        assertEquals(null, articles.get(1).getFeedbackCount());
        assertEquals(null, articles.get(1).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(1).getQiitaRecommendPoint());
        assertEquals(31, articles.get(1).getPostedUser().getUserId());
        assertEquals("user31", articles.get(1).getPostedUser().getDisplayName());
        assertEquals("photo31", articles.get(1).getPostedUser().getPhotoUrl());
        //      articles[2]のテスト
        createDate = LocalDateTime.of(2020, 11, 10, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 11, 00, 00, 00);
        assertEquals(176, articles.get(2).getArticleId());
        assertEquals("title176", articles.get(2).getTitle());
        assertEquals(createDate, articles.get(2).getCreatedAt());
        assertEquals(updateDate, articles.get(2).getUpdatedAt());
        assertEquals(1, articles.get(2).getStateFlag());
        assertEquals(2, articles.get(2).getTags().get(0).getTagId());
        assertEquals(1, articles.get(2).getTags().get(1).getTagId());
        assertEquals("ruby", articles.get(2).getTags().get(0).getTagName());
        assertEquals("Java", articles.get(2).getTags().get(1).getTagName());
        assertEquals(null, articles.get(2).getFeedbackCount());
        assertEquals(2, articles.get(2).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(2).getQiitaRecommendPoint());
        assertEquals(33, articles.get(2).getPostedUser().getUserId());
        assertEquals("user33", articles.get(2).getPostedUser().getDisplayName());
        assertEquals("photo33", articles.get(2).getPostedUser().getPhotoUrl());
        //      articles[3]のテスト
        createDate = LocalDateTime.of(2020, 11, 9, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 10, 00, 00, 00);
        assertEquals(168, articles.get(3).getArticleId());
        assertEquals("title168", articles.get(3).getTitle());
        assertEquals(createDate, articles.get(3).getCreatedAt());
        assertEquals(updateDate, articles.get(3).getUpdatedAt());
        assertEquals(1, articles.get(3).getStateFlag());
        assertEquals(2, articles.get(3).getTags().get(0).getTagId());
        assertEquals(4, articles.get(3).getTags().get(1).getTagId());
        assertEquals("ruby", articles.get(3).getTags().get(0).getTagName());
        assertEquals("php", articles.get(3).getTags().get(1).getTagName());
        assertEquals(1, articles.get(3).getFeedbackCount());
        assertEquals(2, articles.get(3).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(3).getQiitaRecommendPoint());
        assertEquals(32, articles.get(3).getPostedUser().getUserId());
        assertEquals("user32", articles.get(3).getPostedUser().getDisplayName());
        assertEquals("photo32", articles.get(3).getPostedUser().getPhotoUrl());
        //      articles[4]のテスト
        createDate = LocalDateTime.of(2020, 11, 9, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 10, 00, 00, 00);
        assertEquals(187, articles.get(4).getArticleId());
        assertEquals("title187", articles.get(4).getTitle());
        assertEquals(createDate, articles.get(4).getCreatedAt());
        assertEquals(updateDate, articles.get(4).getUpdatedAt());
        assertEquals(1, articles.get(4).getStateFlag());
        assertEquals(5, articles.get(4).getTags().get(0).getTagId());
        assertEquals(1, articles.get(4).getTags().get(1).getTagId());
        assertEquals("go", articles.get(4).getTags().get(0).getTagName());
        assertEquals("Java", articles.get(4).getTags().get(1).getTagName());
        assertEquals(null, articles.get(4).getFeedbackCount());
        assertEquals(null, articles.get(4).getRegisteredMyArticleCount());
        assertEquals(null, articles.get(4).getQiitaRecommendPoint());
        assertEquals(36, articles.get(4).getPostedUser().getUserId());
        assertEquals("user36", articles.get(4).getPostedUser().getDisplayName());
        assertEquals("photo36", articles.get(4).getPostedUser().getPhotoUrl());
        //      articles[5]のテスト
        createDate = LocalDateTime.of(2020, 11, 8, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 9, 00, 00, 00);
        assertEquals(156, articles.get(5).getArticleId());
        assertEquals("title156", articles.get(5).getTitle());
        assertEquals(createDate, articles.get(5).getCreatedAt());
        assertEquals(updateDate, articles.get(5).getUpdatedAt());
        assertEquals(1, articles.get(5).getStateFlag());
        assertEquals(4, articles.get(5).getTags().get(0).getTagId());
        assertEquals(1, articles.get(5).getTags().get(1).getTagId());
        assertEquals("php", articles.get(5).getTags().get(0).getTagName());
        assertEquals("Java", articles.get(5).getTags().get(1).getTagName());
        assertEquals(null, articles.get(5).getFeedbackCount());
        assertEquals(2, articles.get(5).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(5).getQiitaRecommendPoint());
        assertEquals(31, articles.get(5).getPostedUser().getUserId());
        assertEquals("user31", articles.get(5).getPostedUser().getDisplayName());
        assertEquals("photo31", articles.get(5).getPostedUser().getPhotoUrl());
        //      articles[6]のテスト
        createDate = LocalDateTime.of(2020, 11, 8, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 9, 00, 00, 00);
        assertEquals(175, articles.get(6).getArticleId());
        assertEquals("title175", articles.get(6).getTitle());
        assertEquals(createDate, articles.get(6).getCreatedAt());
        assertEquals(updateDate, articles.get(6).getUpdatedAt());
        assertEquals(1, articles.get(6).getStateFlag());
        assertEquals(3, articles.get(6).getTags().get(0).getTagId());
        assertEquals(1, articles.get(6).getTags().get(1).getTagId());
        assertEquals("javascript", articles.get(6).getTags().get(0).getTagName());
        assertEquals("Java", articles.get(6).getTags().get(1).getTagName());
        assertEquals(1, articles.get(6).getFeedbackCount());
        assertEquals(null, articles.get(6).getRegisteredMyArticleCount());
        assertEquals(null, articles.get(6).getQiitaRecommendPoint());
        assertEquals(33, articles.get(6).getPostedUser().getUserId());
        assertEquals("user33", articles.get(6).getPostedUser().getDisplayName());
        assertEquals("photo33", articles.get(6).getPostedUser().getPhotoUrl());
        //      articles[7]のテスト
        createDate = LocalDateTime.of(2020, 11, 8, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 9, 00, 00, 00);
        assertEquals(190, articles.get(7).getArticleId());
        assertEquals("title190", articles.get(7).getTitle());
        assertEquals(createDate, articles.get(7).getCreatedAt());
        assertEquals(updateDate, articles.get(7).getUpdatedAt());
        assertEquals(1, articles.get(7).getStateFlag());
        assertEquals(2, articles.get(7).getTags().get(0).getTagId());
        assertEquals(4, articles.get(7).getTags().get(1).getTagId());
        assertEquals("ruby", articles.get(7).getTags().get(0).getTagName());
        assertEquals("php", articles.get(7).getTags().get(1).getTagName());
        assertEquals(2, articles.get(7).getFeedbackCount());
        assertEquals(1, articles.get(7).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(7).getQiitaRecommendPoint());
        assertEquals(37, articles.get(7).getPostedUser().getUserId());
        assertEquals("user37", articles.get(7).getPostedUser().getDisplayName());
        assertEquals("photo37", articles.get(7).getPostedUser().getPhotoUrl());
        //      articles[8]のテスト
        createDate = LocalDateTime.of(2020, 11, 7, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 8, 00, 00, 00);
        assertEquals(167, articles.get(8).getArticleId());
        assertEquals("title167", articles.get(8).getTitle());
        assertEquals(createDate, articles.get(8).getCreatedAt());
        assertEquals(updateDate, articles.get(8).getUpdatedAt());
        assertEquals(1, articles.get(8).getStateFlag());
        assertEquals(3, articles.get(8).getTags().get(0).getTagId());
        assertEquals(5, articles.get(8).getTags().get(1).getTagId());
        assertEquals("javascript", articles.get(8).getTags().get(0).getTagName());
        assertEquals("go", articles.get(8).getTags().get(1).getTagName());
        assertEquals(null, articles.get(8).getFeedbackCount());
        assertEquals(null, articles.get(8).getRegisteredMyArticleCount());
        assertEquals(null, articles.get(8).getQiitaRecommendPoint());
        assertEquals(32, articles.get(8).getPostedUser().getUserId());
        assertEquals("user32", articles.get(8).getPostedUser().getDisplayName());
        assertEquals("photo32", articles.get(8).getPostedUser().getPhotoUrl());
        //      articles[9]のテスト
        createDate = LocalDateTime.of(2020, 11, 7, 00, 00, 00);
        updateDate = LocalDateTime.of(2020, 11, 8, 00, 00, 00);
        assertEquals(193, articles.get(9).getArticleId());
        assertEquals("title193", articles.get(9).getTitle());
        assertEquals(createDate, articles.get(9).getCreatedAt());
        assertEquals(updateDate, articles.get(9).getUpdatedAt());
        assertEquals(1, articles.get(9).getStateFlag());
        assertEquals(2, articles.get(9).getTags().get(0).getTagId());
        assertEquals(3, articles.get(9).getTags().get(1).getTagId());
        assertEquals("ruby", articles.get(9).getTags().get(0).getTagName());
        assertEquals("javascript", articles.get(9).getTags().get(1).getTagName());
        assertEquals(1, articles.get(9).getFeedbackCount());
        assertEquals(null, articles.get(9).getRegisteredMyArticleCount());
        assertEquals(2, articles.get(9).getQiitaRecommendPoint());
        assertEquals(39, articles.get(9).getPostedUser().getUserId());
        assertEquals("user39", articles.get(9).getPostedUser().getDisplayName());
        assertEquals("photo39", articles.get(9).getPostedUser().getPhotoUrl());
    }

//    sortNum(0:新着順,1:更新順,2:qiita,3:my記事)
//    preriod(0:週間:,1:月間,2:全て)
//    toggleSearchWord(0:記事タイトル,1:ユーザー名)

    @Test
    void searchArticlesId正常系_TC1() {
        searchArticlesSqlTemplate(0);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "", null, 0, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(21, articleIdList.get(0));
        assertEquals(19, articleIdList.get(1));
        assertEquals(1, articleIdList.get(2));
        assertEquals(2, articleIdList.get(3));
        assertEquals(13, articleIdList.get(4));
        assertEquals(6, articleIdList.get(5));
        assertEquals(15, articleIdList.get(6));
        assertEquals(8, articleIdList.get(7));
        assertEquals(20, articleIdList.get(8));
        assertEquals(11, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC2() {
        searchArticlesSqlTemplate(0);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "title", null, 0, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(21, articleIdList.get(0));
        assertEquals(19, articleIdList.get(1));
        assertEquals(1, articleIdList.get(2));
        assertEquals(2, articleIdList.get(3));
        assertEquals(13, articleIdList.get(4));
        assertEquals(6, articleIdList.get(5));
        assertEquals(15, articleIdList.get(6));
        assertEquals(8, articleIdList.get(7));
        assertEquals(20, articleIdList.get(8));
        assertEquals(11, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC3() {
        searchArticlesSqlTemplate(0);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "title", Arrays.asList(1, 2), 0, 0, Arrays.asList(1, 2));
        searchArticleForm.setSort("createdAt");
        searchArticleForm.setPageSize(10);
        searchArticleForm.setOffset(0);
        searchArticleForm.setSearchWord("title");
        searchArticleForm.setSearchTag(Arrays.asList(1, 2));
        searchArticleForm.setPeriod(0);
        searchArticleForm.setToggleSearchWord(0);
        searchArticleForm.setStateFlagList(Arrays.asList(1, 2));

        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(21, articleIdList.get(0));
        assertEquals(19, articleIdList.get(1));
        assertEquals(23, articleIdList.get(2));
        assertEquals(20, articleIdList.get(3));
        assertEquals(33, articleIdList.get(4));
        assertEquals(8, articleIdList.get(5));
        assertEquals(37, articleIdList.get(6));
        assertEquals(11, articleIdList.get(7));
        assertEquals(26, articleIdList.get(8));
        assertEquals(15, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC4() {
        searchArticlesSqlTemplate(0);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "たろ", Arrays.asList(1, 2), 0, 1, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(3, articleIdList.size());
//        取得記事
        assertEquals(38, articleIdList.get(0));
        assertEquals(40, articleIdList.get(1));
        assertEquals(39, articleIdList.get(2));
    }

    @Test
    void searchArticlesId正常系_TC5() {
        searchArticlesSqlTemplate(0);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "たろ", Arrays.asList(1), 0, 1, Arrays.asList(1, 2));
        searchArticleForm.setSort("createdAt");
        searchArticleForm.setPageSize(10);
        searchArticleForm.setOffset(0);
        searchArticleForm.setSearchWord("たろ");
        searchArticleForm.setSearchTag(Arrays.asList(1));
        searchArticleForm.setPeriod(0);
        searchArticleForm.setToggleSearchWord(1);
        searchArticleForm.setStateFlagList(Arrays.asList(1, 2));

        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(2, articleIdList.size());
//        取得記事
        assertEquals(38, articleIdList.get(0));
        assertEquals(40, articleIdList.get(1));
    }

    @Test
    void searchArticlesId正常系_TC6() {
        searchArticlesSqlTemplate(1);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "", null, 1, 0, Arrays.asList(1, 2));
        searchArticleForm.setSort("createdAt");
        searchArticleForm.setPageSize(10);
        searchArticleForm.setOffset(0);
        searchArticleForm.setSearchWord("");
        searchArticleForm.setSearchTag(null);
        searchArticleForm.setPeriod(1);
        searchArticleForm.setToggleSearchWord(0);
        searchArticleForm.setStateFlagList(Arrays.asList(1, 2));

        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(25, articleIdList.get(0));
        assertEquals(16, articleIdList.get(1));
        assertEquals(26, articleIdList.get(2));
        assertEquals(6, articleIdList.get(3));
        assertEquals(30, articleIdList.get(4));
        assertEquals(8, articleIdList.get(5));
        assertEquals(11, articleIdList.get(6));
        assertEquals(12, articleIdList.get(7));
        assertEquals(14, articleIdList.get(8));
        assertEquals(15, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC7() {
        searchArticlesSqlTemplate(1);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "title", null, 1, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(25, articleIdList.get(0));
        assertEquals(16, articleIdList.get(1));
        assertEquals(26, articleIdList.get(2));
        assertEquals(6, articleIdList.get(3));
        assertEquals(30, articleIdList.get(4));
        assertEquals(8, articleIdList.get(5));
        assertEquals(11, articleIdList.get(6));
        assertEquals(12, articleIdList.get(7));
        assertEquals(14, articleIdList.get(8));
        assertEquals(15, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC8() {
        searchArticlesSqlTemplate(1);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "title", Arrays.asList(1, 2), 1, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(16, articleIdList.get(0));
        assertEquals(8, articleIdList.get(1));
        assertEquals(11, articleIdList.get(2));
        assertEquals(26, articleIdList.get(3));
        assertEquals(12, articleIdList.get(4));
        assertEquals(15, articleIdList.get(5));
        assertEquals(27, articleIdList.get(6));
        assertEquals(39, articleIdList.get(7));
        assertEquals(7, articleIdList.get(8));
        assertEquals(3, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC9() {
        searchArticlesSqlTemplate(1);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "たろ", null, 1, 1, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(2, articleIdList.size());
//        取得記事
        assertEquals(39, articleIdList.get(0));
        assertEquals(40, articleIdList.get(1));
    }

    @Test
    void searchArticlesId正常系_TC10() {
        searchArticlesSqlTemplate(1);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "たろ", Arrays.asList(1, 2), 1, 1, Arrays.asList(1, 2));
        searchArticleForm.setSort("createdAt");
        searchArticleForm.setPageSize(10);
        searchArticleForm.setOffset(0);
        searchArticleForm.setSearchWord("たろ");
        searchArticleForm.setSearchTag(Arrays.asList(1, 2));
        searchArticleForm.setPeriod(1);
        searchArticleForm.setToggleSearchWord(1);
        searchArticleForm.setStateFlagList(Arrays.asList(1, 2));

        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(2, articleIdList.size());
//        取得記事
        assertEquals(39, articleIdList.get(0));
        assertEquals(40, articleIdList.get(1));
    }

    @Test
    void searchArticlesId正常系_TC11() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "", null, null, 0, Arrays.asList(1, 2));

        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(157, articleIdList.get(0));
        assertEquals(44, articleIdList.get(1));
        assertEquals(176, articleIdList.get(2));
        assertEquals(187, articleIdList.get(3));
        assertEquals(168, articleIdList.get(4));
        assertEquals(156, articleIdList.get(5));
        assertEquals(190, articleIdList.get(6));
        assertEquals(175, articleIdList.get(7));
        assertEquals(167, articleIdList.get(8));
        assertEquals(193, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC12() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("updatedAt", 10, 0, "", null, null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
        assertEquals(10, articleIdList.size());
        assertEquals(44, articleIdList.get(0));
        assertEquals(176, articleIdList.get(1));
        assertEquals(157, articleIdList.get(2));
        assertEquals(187, articleIdList.get(3));
        assertEquals(168, articleIdList.get(4));
        assertEquals(156, articleIdList.get(5));
        assertEquals(190, articleIdList.get(6));
        assertEquals(175, articleIdList.get(7));
        assertEquals(167, articleIdList.get(8));
        assertEquals(193, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC13() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("recommendCnt", 10, 0, "", null, null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
        assertEquals(10, articleIdList.size());
        assertEquals(46, articleIdList.get(0));
        assertEquals(38, articleIdList.get(1));
        assertEquals(43, articleIdList.get(2));
        assertEquals(22, articleIdList.get(3));
        assertEquals(39, articleIdList.get(4));
        assertEquals(45, articleIdList.get(5));
        assertEquals(42, articleIdList.get(6));
        assertEquals(28, articleIdList.get(7));
        assertEquals(23, articleIdList.get(8));
        assertEquals(11, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC14() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("myCnt", 10, 0, "", null, null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
        assertEquals(10, articleIdList.size());
        assertEquals(24, articleIdList.get(0));
        assertEquals(32, articleIdList.get(1));
        assertEquals(16, articleIdList.get(2));
        assertEquals(4, articleIdList.get(3));
        assertEquals(20, articleIdList.get(4));
        assertEquals(40, articleIdList.get(5));
        assertEquals(8, articleIdList.get(6));
        assertEquals(28, articleIdList.get(7));
        assertEquals(36, articleIdList.get(8));
        assertEquals(12, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC15() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 20, 0, "", null, null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(20, articleIdList.size());
//        取得記事
        assertEquals(176, articleIdList.get(0));
        assertEquals(157, articleIdList.get(1));
        assertEquals(44, articleIdList.get(2));
        assertEquals(187, articleIdList.get(3));
        assertEquals(168, articleIdList.get(4));
        assertEquals(156, articleIdList.get(5));
        assertEquals(190, articleIdList.get(6));
        assertEquals(175, articleIdList.get(7));
        assertEquals(193, articleIdList.get(8));
        assertEquals(167, articleIdList.get(9));
        assertEquals(183, articleIdList.get(10));
        assertEquals(182, articleIdList.get(11));
        assertEquals(166, articleIdList.get(12));
        assertEquals(155, articleIdList.get(13));
        assertEquals(174, articleIdList.get(14));
        assertEquals(28, articleIdList.get(15));
        assertEquals(186, articleIdList.get(16));
        assertEquals(59, articleIdList.get(17));
        assertEquals(94, articleIdList.get(18));
        assertEquals(109, articleIdList.get(19));
    }

    @Test
    void searchArticlesId正常系_TC16() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 30, 0, "", null, null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(30, articleIdList.size());
//        取得記事
        assertEquals(176, articleIdList.get(0));
        assertEquals(157, articleIdList.get(1));
        assertEquals(44, articleIdList.get(2));
        assertEquals(168, articleIdList.get(3));
        assertEquals(187, articleIdList.get(4));
        assertEquals(175, articleIdList.get(5));
        assertEquals(190, articleIdList.get(6));
        assertEquals(156, articleIdList.get(7));
        assertEquals(167, articleIdList.get(8));
        assertEquals(193, articleIdList.get(9));
        assertEquals(183, articleIdList.get(10));
        assertEquals(166, articleIdList.get(11));
        assertEquals(182, articleIdList.get(12));
        assertEquals(155, articleIdList.get(13));
        assertEquals(186, articleIdList.get(14));
        assertEquals(174, articleIdList.get(15));
        assertEquals(28, articleIdList.get(16));
        assertEquals(94, articleIdList.get(17));
        assertEquals(109, articleIdList.get(18));
        assertEquals(21, articleIdList.get(19));
        assertEquals(59, articleIdList.get(20));
        assertEquals(58, articleIdList.get(21));
        assertEquals(86, articleIdList.get(22));
        assertEquals(139, articleIdList.get(23));
        assertEquals(68, articleIdList.get(24));
        assertEquals(81, articleIdList.get(25));
        assertEquals(108, articleIdList.get(26));
        assertEquals(138, articleIdList.get(27));
        assertEquals(20, articleIdList.get(28));
        assertEquals(37, articleIdList.get(29));

    }

    @Test
    void searchArticlesId正常系_TC17() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "title", null, null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(157, articleIdList.get(0));
        assertEquals(44, articleIdList.get(1));
        assertEquals(176, articleIdList.get(2));
        assertEquals(187, articleIdList.get(3));
        assertEquals(168, articleIdList.get(4));
        assertEquals(156, articleIdList.get(5));
        assertEquals(190, articleIdList.get(6));
        assertEquals(175, articleIdList.get(7));
        assertEquals(167, articleIdList.get(8));
        assertEquals(193, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC18() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "2", null, null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(182, articleIdList.get(0));
        assertEquals(28, articleIdList.get(1));
        assertEquals(21, articleIdList.get(2));
        assertEquals(20, articleIdList.get(3));
        assertEquals(192, articleIdList.get(4));
        assertEquals(42, articleIdList.get(5));
        assertEquals(132, articleIdList.get(6));
        assertEquals(27, articleIdList.get(7));
        assertEquals(172, articleIdList.get(8));
        assertEquals(26, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC19() {
        searchArticlesSqlTemplate(2);
        String sql = "INSERT INTO articles (user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) " +
                "VALUES (1, '2020-10-03 00:00:00', '2020-10-04 00:00:00', " +
                "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', " +
                "'#content2', null, 1);";
        jdbcTemplate.execute(sql);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", null, null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(1, articleIdList.size());
//        取得記事
        assertEquals(201, articleIdList.get(0));
    }

    @Test
    void searchArticlesId正常系_TC20() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "存在しない記事タイトル", null, null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(0, articleIdList.size());
    }

    @Test
    void searchArticlesId正常系_TC21() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "ん", null, null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(0, articleIdList.size());
    }

    @Test
    void searchArticlesId正常系_TC22() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb", null, null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(0, articleIdList.size());
    }

    @Test
    void searchArticlesId正常系_TC24() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "12", null, null, 1, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(9, articleIdList.size());
//        取得記事
        assertEquals(68, articleIdList.get(0));
        assertEquals(67, articleIdList.get(1));
        assertEquals(66, articleIdList.get(2));
        assertEquals(65, articleIdList.get(3));
        assertEquals(64, articleIdList.get(4));
        assertEquals(63, articleIdList.get(5));
        assertEquals(62, articleIdList.get(6));
        assertEquals(61, articleIdList.get(7));
        assertEquals(60, articleIdList.get(8));
    }

    @Test
    void searchArticlesId正常系_TC25() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "1", null, null, 1, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(157, articleIdList.get(0));
        assertEquals(156, articleIdList.get(1));
        assertEquals(155, articleIdList.get(2));
        assertEquals(94, articleIdList.get(3));
        assertEquals(109, articleIdList.get(4));
        assertEquals(86, articleIdList.get(5));
        assertEquals(108, articleIdList.get(6));
        assertEquals(81, articleIdList.get(7));
        assertEquals(68, articleIdList.get(8));
        assertEquals(154, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC26() {
        searchArticlesSqlTemplate(2);
        String userSql = "INSERT INTO users (uid, photo_url, display_name, password) VALUES ('uid41', 'photo41', 'cccccccccccccccccccccccccccccc', 'pass41');";
        String articleSql = "INSERT INTO articles (user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) VALUES (41, '2020-10-03 00:00:00', '2020-10-04 00:00:00', 'title41', '#content41', null, 1);";
        jdbcTemplate.execute(userSql);
        jdbcTemplate.execute(articleSql);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "cccccccccccccccccccccccccccccc", null, null, 1, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(1, articleIdList.size());
//        取得記事
        assertEquals(201, articleIdList.get(0));
    }

    @Test
    void searchArticlesId正常系_TC27() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "存在しないユーザー名", null, null, 1, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(0, articleIdList.size());
    }

    @Test
    void searchArticlesId正常系_TC28() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "q", null, null, 1, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(0, articleIdList.size());
    }

    @Test
    void searchArticlesId正常系_TC29() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "dddddddddddddddddddddddddddddd", null, null, 1, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(0, articleIdList.size());
    }

    @Test
    void searchArticlesId正常系_TC31() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "", Arrays.asList(1, 2), null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(44, articleIdList.get(0));
        assertEquals(157, articleIdList.get(1));
        assertEquals(176, articleIdList.get(2));
        assertEquals(168, articleIdList.get(3));
        assertEquals(187, articleIdList.get(4));
        assertEquals(190, articleIdList.get(5));
        assertEquals(156, articleIdList.get(6));
        assertEquals(175, articleIdList.get(7));
        assertEquals(193, articleIdList.get(8));
        assertEquals(166, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC32() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "", Arrays.asList(3), null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(175, articleIdList.get(0));
        assertEquals(167, articleIdList.get(1));
        assertEquals(193, articleIdList.get(2));
        assertEquals(28, articleIdList.get(3));
        assertEquals(174, articleIdList.get(4));
        assertEquals(155, articleIdList.get(5));
        assertEquals(58, articleIdList.get(6));
        assertEquals(108, articleIdList.get(7));
        assertEquals(37, articleIdList.get(8));
        assertEquals(154, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC33() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "", Arrays.asList(1, 2, 3, 4, 5), null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(157, articleIdList.get(0));
        assertEquals(44, articleIdList.get(1));
        assertEquals(176, articleIdList.get(2));
        assertEquals(187, articleIdList.get(3));
        assertEquals(168, articleIdList.get(4));
        assertEquals(156, articleIdList.get(5));
        assertEquals(190, articleIdList.get(6));
        assertEquals(175, articleIdList.get(7));
        assertEquals(167, articleIdList.get(8));
        assertEquals(193, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC35() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "title", Arrays.asList(1, 2), null, 0, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(44, articleIdList.get(0));
        assertEquals(157, articleIdList.get(1));
        assertEquals(176, articleIdList.get(2));
        assertEquals(168, articleIdList.get(3));
        assertEquals(187, articleIdList.get(4));
        assertEquals(190, articleIdList.get(5));
        assertEquals(156, articleIdList.get(6));
        assertEquals(175, articleIdList.get(7));
        assertEquals(193, articleIdList.get(8));
        assertEquals(166, articleIdList.get(9));
    }

    @Test
    void searchArticlesId正常系_TC36() {
        searchArticlesSqlTemplate(2);
        SearchArticleForm searchArticleForm = setSearchArticleForm
                ("createdAt", 10, 0, "2", Arrays.asList(1, 2), null, 1, Arrays.asList(1, 2));
        List<Integer> articleIdList = articleMapper.searchArticlesId(searchArticleForm);
//        記事数
        assertEquals(10, articleIdList.size());
//        取得記事
        assertEquals(168, articleIdList.get(0));
        assertEquals(166, articleIdList.get(1));
        assertEquals(109, articleIdList.get(2));
        assertEquals(139, articleIdList.get(3));
        assertEquals(68, articleIdList.get(4));
        assertEquals(132, articleIdList.get(5));
        assertEquals(136, articleIdList.get(6));
        assertEquals(107, articleIdList.get(7));
        assertEquals(165, articleIdList.get(8));
        assertEquals(67, articleIdList.get(9));
    }


    @Test
    void getPostedArticleCountRank() {
        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr = CollectionSQL.insertArticles.split("\n", 0);
        String[] feedbackSqlArr = CollectionSQL.insertFeedbacks.split("\n", 0);
        String[] qiitaRecommendSqlArr = CollectionSQL.insertQiitaRecommends.split("\n", 0);
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


        List<RankingUser> rankingUserList = articleMapper.getPostedArticleCountRank();
        assertEquals(35, rankingUserList.size());

        //////////最初
        assertEquals(31, rankingUserList.get(0).getUser().getUserId());
        assertEquals("user31", rankingUserList.get(0).getUser().getDisplayName());
        assertEquals("photo31", rankingUserList.get(0).getUser().getPhotoUrl());
        assertEquals(7, rankingUserList.get(0).getUser().getFeedbackCount());
        assertEquals(15, rankingUserList.get(0).getUser().getPostedArticleCount());
        assertEquals(11, rankingUserList.get(0).getUser().getQiitaRecommendedAllCount());

        assertEquals(21, rankingUserList.get(1).getUser().getUserId());
        assertEquals("user21", rankingUserList.get(1).getUser().getDisplayName());
        assertEquals("photo21", rankingUserList.get(1).getUser().getPhotoUrl());
        assertEquals(7, rankingUserList.get(1).getUser().getFeedbackCount());
        assertEquals(14, rankingUserList.get(1).getUser().getPostedArticleCount());
        assertEquals(3, rankingUserList.get(1).getUser().getQiitaRecommendedAllCount());

        assertEquals(11, rankingUserList.get(2).getUser().getUserId());
        assertEquals("user", rankingUserList.get(2).getUser().getDisplayName());
        assertEquals("user_photo", rankingUserList.get(2).getUser().getPhotoUrl());
        assertEquals(8, rankingUserList.get(2).getUser().getFeedbackCount());
        assertEquals(13, rankingUserList.get(2).getUser().getPostedArticleCount());
        assertEquals(15, rankingUserList.get(2).getUser().getQiitaRecommendedAllCount());

        assertEquals(1, rankingUserList.get(3).getUser().getUserId());
        assertEquals("a", rankingUserList.get(3).getUser().getDisplayName());
        assertEquals("a", rankingUserList.get(3).getUser().getPhotoUrl());
        assertEquals(8, rankingUserList.get(3).getUser().getFeedbackCount());
        assertEquals(12, rankingUserList.get(3).getUser().getPostedArticleCount());
        assertEquals(14, rankingUserList.get(3).getUser().getQiitaRecommendedAllCount());

        assertEquals(32, rankingUserList.get(4).getUser().getUserId());
        assertEquals("user32", rankingUserList.get(4).getUser().getDisplayName());
        assertEquals("photo32", rankingUserList.get(4).getUser().getPhotoUrl());
        assertEquals(0, rankingUserList.get(4).getUser().getFeedbackCount());
        assertEquals(11, rankingUserList.get(4).getUser().getPostedArticleCount());
        assertEquals(7, rankingUserList.get(4).getUser().getQiitaRecommendedAllCount());

        //////////中間
        assertEquals(24, rankingUserList.get(14).getUser().getUserId());
        assertEquals("user24", rankingUserList.get(14).getUser().getDisplayName());
        assertEquals("photo24", rankingUserList.get(14).getUser().getPhotoUrl());
        assertEquals(4, rankingUserList.get(14).getUser().getFeedbackCount());
        assertEquals(6, rankingUserList.get(14).getUser().getPostedArticleCount());
        assertEquals(7, rankingUserList.get(14).getUser().getQiitaRecommendedAllCount());

        assertEquals(4, rankingUserList.get(15).getUser().getUserId());
        assertEquals("d", rankingUserList.get(15).getUser().getDisplayName());
        assertEquals("d", rankingUserList.get(15).getUser().getPhotoUrl());
        assertEquals(5, rankingUserList.get(15).getUser().getFeedbackCount());
        assertEquals(5, rankingUserList.get(15).getUser().getPostedArticleCount());
        assertEquals(3, rankingUserList.get(15).getUser().getQiitaRecommendedAllCount());

        assertEquals(15, rankingUserList.get(16).getUser().getUserId());
        assertEquals("user15", rankingUserList.get(16).getUser().getDisplayName());
        assertEquals("photo15", rankingUserList.get(16).getUser().getPhotoUrl());
        assertEquals(9, rankingUserList.get(16).getUser().getFeedbackCount());
        assertEquals(5, rankingUserList.get(16).getUser().getPostedArticleCount());
        assertEquals(7, rankingUserList.get(16).getUser().getQiitaRecommendedAllCount());

        assertEquals(5, rankingUserList.get(17).getUser().getUserId());
        assertEquals("test", rankingUserList.get(17).getUser().getDisplayName());
        assertEquals("test_photo", rankingUserList.get(17).getUser().getPhotoUrl());
        assertEquals(11, rankingUserList.get(17).getUser().getFeedbackCount());
        assertEquals(4, rankingUserList.get(17).getUser().getPostedArticleCount());
        assertEquals(9, rankingUserList.get(17).getUser().getQiitaRecommendedAllCount());

        assertEquals(26, rankingUserList.get(18).getUser().getUserId());
        assertEquals("user26", rankingUserList.get(18).getUser().getDisplayName());
        assertEquals("photo26", rankingUserList.get(18).getUser().getPhotoUrl());
        assertEquals(3, rankingUserList.get(18).getUser().getFeedbackCount());
        assertEquals(4, rankingUserList.get(18).getUser().getPostedArticleCount());
        assertEquals(1, rankingUserList.get(18).getUser().getQiitaRecommendedAllCount());

        //////////最後
        assertEquals(9, rankingUserList.get(30).getUser().getUserId());
        assertEquals("ゆみ", rankingUserList.get(30).getUser().getDisplayName());
        assertEquals("rrr", rankingUserList.get(30).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(30).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(30).getUser().getPostedArticleCount());
        assertEquals(4, rankingUserList.get(30).getUser().getQiitaRecommendedAllCount());

        assertEquals(10, rankingUserList.get(31).getUser().getUserId());
        assertEquals("そうし", rankingUserList.get(31).getUser().getDisplayName());
        assertEquals("uuu", rankingUserList.get(31).getUser().getPhotoUrl());
        assertEquals(15, rankingUserList.get(31).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(31).getUser().getPostedArticleCount());
        assertEquals(5, rankingUserList.get(31).getUser().getQiitaRecommendedAllCount());

        assertEquals(19, rankingUserList.get(32).getUser().getUserId());
        assertEquals("user19", rankingUserList.get(32).getUser().getDisplayName());
        assertEquals("photo19", rankingUserList.get(32).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(32).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(32).getUser().getPostedArticleCount());
        assertEquals(3, rankingUserList.get(32).getUser().getQiitaRecommendedAllCount());

        assertEquals(29, rankingUserList.get(33).getUser().getUserId());
        assertEquals("user29", rankingUserList.get(33).getUser().getDisplayName());
        assertEquals("photo29", rankingUserList.get(33).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(33).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(33).getUser().getPostedArticleCount());
        assertEquals(2, rankingUserList.get(33).getUser().getQiitaRecommendedAllCount());

        assertEquals(39, rankingUserList.get(34).getUser().getUserId());
        assertEquals("user39", rankingUserList.get(34).getUser().getDisplayName());
        assertEquals("photo39", rankingUserList.get(34).getUser().getPhotoUrl());
        assertEquals(0, rankingUserList.get(34).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(34).getUser().getPostedArticleCount());
        assertEquals(2, rankingUserList.get(34).getUser().getQiitaRecommendedAllCount());
    }

    @Test
    void getPostedArticleCountRank_none_exist_rankingUser() {
        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr = CollectionSQL.insertArticles
                .replace("1);", "9);")
                .replace("2);", "9);")
                .split("\n", 0);
        for (String sql : userSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (String sql : articleSqlArr) {
            jdbcTemplate.execute(sql);
        }

        List<RankingUser> rankingUserList = articleMapper.getPostedArticleCountRank();
        assertTrue(rankingUserList.isEmpty());
    }

    @Test
    void findArticleById() {
        String[] tagsSqlArr = CollectionSQL.insertTags.split("\n", 0);
        for (String sql : tagsSqlArr) {
            jdbcTemplate.execute(sql);
        }
        List<String> sqlArr = new ArrayList<>();
        sqlArr.add("INSERT INTO users (display_name, photo_url) VALUES ('test_user1', 'test_photo1');");
        sqlArr.add("INSERT INTO users (display_name, photo_url) VALUES ('test_user2', 'test_photo2');");
        sqlArr.add("INSERT INTO users (display_name, photo_url) VALUES ('test_user3', 'test_photo3');");
        sqlArr.add("INSERT INTO articles (user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) VALUES (1, '2020-10-31 00:00:00', '2020-11-01 00:00:00', 'title test', '#content test', null, 1);");
        sqlArr.add("INSERT INTO articles_tags_relations (article_id, posted_user_id, tag_id) VALUES (1, 1, 2);");
        sqlArr.add("INSERT INTO articles_tags_relations (article_id, posted_user_id, tag_id) VALUES (1, 1, 4);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (1, 2, 1);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (1, 3, 1);");
        sqlArr.add("INSERT INTO my_articles (article_id, posted_user_id, register_user_id) VALUES (1, 1, 2);");
        sqlArr.add("INSERT INTO feedbacks (article_id, user_id, created_at, updated_at, content, delete_flag) VALUES (1, 3, '2020-11-03 00:00:00', '2020-11-04 00:00:00', 'feedback content', 0);");
        sqlArr.add("INSERT INTO feedbacks (article_id, user_id, created_at, updated_at, content, delete_flag) VALUES (1, 2, '2020-11-03 00:00:00', '2020-11-04 00:00:00', 'feedback content', 1);");
        sqlArr.forEach(sql -> jdbcTemplate.execute(sql));

        List<Article> article = articleMapper.findArticleById(1);

        assertEquals(1, article.size());
        assertEquals(1, article.get(0).getArticleId());
        assertEquals(1, article.get(0).getPostedUser().getUserId());
        assertEquals("test_user1", article.get(0).getPostedUser().getDisplayName());
        assertEquals("test_photo1", article.get(0).getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 31, 0, 0, 0), article.get(0).getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 11, 1, 0, 0, 0), article.get(0).getUpdatedAt());
        assertEquals("title test", article.get(0).getTitle());
        assertNull(article.get(0).getQiitaArticleId());
        assertEquals(1, article.get(0).getStateFlag());
        assertEquals(2, article.get(0).getTags().get(0).getTagId());
        assertEquals("ruby", article.get(0).getTags().get(0).getTagName());
        assertEquals(4, article.get(0).getTags().get(1).getTagId());
        assertEquals("php", article.get(0).getTags().get(1).getTagName());
        assertEquals(2, article.get(0).getQiitaRecommendPoint());
        assertEquals(1, article.get(0).getRegisteredMyArticleCount());
        assertEquals(1, article.get(0).getFeedbackCount());
    }

    @Test
    void getArticleIdListByUserId() {
        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr = CollectionSQL.insertArticles.split("\n", 0);
        for (String sql : userSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (String sql : articleSqlArr) {
            jdbcTemplate.execute(sql);
        }

        List<Integer> articleIdList = articleMapper.getArticleIdListByUserId(24);

        assertEquals(6, articleIdList.size());
        assertEquals(132, articleIdList.get(0));
        assertEquals(131, articleIdList.get(1));
        assertEquals(130, articleIdList.get(2));
        assertEquals(129, articleIdList.get(3));
        assertEquals(128, articleIdList.get(4));
        assertEquals(127, articleIdList.get(5));
    }

    @Test
    void insertArticleTest正常系() {

        User user = new User();
        user.setUserId(1);

        String userSql = "INSERT INTO users(user_id) VALUES (1)";
        jdbcTemplate.execute(userSql);

        Article article = Article.builder()
                .postedUser(user)
                .title("title test")
                .content("content test")
                .stateFlag(1)
                .build();

        //テスト
        articleMapper.insertArticle(article);

        String articleSelectSql
                = "SELECT * FROM articles ORDER BY article_id";

        SqlParameterSource param = new EmptySqlParameterSource();

        List<Map<String, Object>> resultArticles = namedParameterJdbcTemplate.queryForList(articleSelectSql, param);

        Map<String, Object> resultArticle = resultArticles.get(0);


        assertEquals(1, resultArticle.get("user_id"));
        assertEquals("title test", resultArticle.get("title"));
        assertEquals("content test", resultArticle.get("content"));
        assertEquals(1, resultArticle.get("state_flag"));
        assertEquals(1, resultArticle.get("user_id"));

    }

    @Test
    void insertArticleTest異常系_外部成約されている値が存在しない場合() {
        User user = new User();
        user.setUserId(1);

        Article article = Article.builder()
                .postedUser(user)
                .title("title test")
                .content("content test")
                .stateFlag(1)
                .build();


        //テスト
        Exception exception = assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> articleMapper.insertArticle(article));
        assertTrue(exception.getMessage().contains("fk_articles_userid` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)"));
    }


    @Test
    void updateArticle正常系() {
        String insertUser = "INSERT INTO users (user_id) values (1)";
        String insertArticle =
                "INSERT INTO articles ( user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) " +
                        "VALUES(1,NOW(),NOW(),'test title','content title',null,1)";

        jdbcTemplate.execute(insertUser);
        jdbcTemplate.execute(insertArticle);

        User user = new User();
        user.setUserId(1);

        Article article = Article.builder()
                .articleId(1)
                .title("test title edited")
                .content("content title edited")
                .stateFlag(2)
                .postedUser(user)
                .build();

        articleMapper.updateArticle(article);

        String articleSelectSql
                = "SELECT * FROM articles ORDER BY article_id";

        SqlParameterSource param = new EmptySqlParameterSource();

        List<Map<String, Object>> resultArticles = namedParameterJdbcTemplate.queryForList(articleSelectSql, param);

        Map<String, Object> resultArticle = resultArticles.get(0);


        assertEquals(1, resultArticle.get("article_id"));
        assertEquals(1, resultArticle.get("user_id"));
        assertEquals("test title edited", resultArticle.get("title"));
        assertEquals("content title edited", resultArticle.get("content"));
        assertEquals(2, resultArticle.get("state_flag"));
    }

    //// getArticleAndFeedback()
    @Test
    void getArticleAndFeedback正常系() {
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
        Article actual = articleMapper.getArticleAndFeedback(1);

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
    void load正常系() {
        String insertUsers = "INSERT INTO users (user_id) VALUES (1), (2)";
        String insertArticles =
                "INSERT INTO articles ( user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) " +
                        "VALUES(1,NOW(),NOW(),'test title','content title',null,1)";

        String insertTags =
                "INSERT INTO tags VALUES (1,'test_tag1'),(2,'test_tag2')";

        String insertArticlesTags = "INSERT INTO articles_tags_relations(tags_relation_id, article_id, posted_user_id, tag_id) VALUES (1,1,1,1),(2,1,1,2)";

        jdbcTemplate.execute(insertUsers);
        jdbcTemplate.execute(insertArticles);
        jdbcTemplate.execute(insertTags);
        jdbcTemplate.execute(insertArticlesTags);

        Article article = articleMapper.load(1);

        assertEquals(article.getArticleId(), 1);
        assertEquals(article.getStateFlag(), 1);
        assertEquals(article.getTitle(), "test title");
        assertEquals(article.getContent(), "content title");

        assertEquals(article.getTags().get(0).getTagId(), 1);
        assertEquals(article.getTags().get(1).getTagId(), 2);

        assertEquals(article.getTags().get(0).getTagName(), "test_tag1");
        assertEquals(article.getTags().get(1).getTagName(), "test_tag2");
    }

    @Test
    void getArticlesByUserId() {
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

        //userId=1のテスト
        List<Article> articles = articleMapper.getArticlesByUserId(1);

        LocalDateTime createDateFirst = LocalDateTime.of(2020, 10, 23, 00, 00, 00);
        LocalDateTime updateDateFirst = createDateFirst.plusDays(1);
        LocalDateTime createDateLast = LocalDateTime.of(2020, 10, 1, 00, 00, 00);
        LocalDateTime updateDateLast = createDateLast.plusDays(1);

        //記事件数と最初,最後の記事の取得してきたもの全てテスト
        assertEquals(12, articles.size());
        assertEquals(12, articles.get(0).getArticleId());
        assertEquals("title12", articles.get(0).getTitle());
        assertEquals(createDateFirst, articles.get(0).getCreatedAt());
        assertEquals(updateDateFirst, articles.get(0).getUpdatedAt());
        assertEquals(1, articles.get(0).getStateFlag());
        assertEquals(1, articles.get(0).getTags().get(0).getTagId());
        assertEquals("Java", articles.get(0).getTags().get(0).getTagName());
        assertEquals(2, articles.get(0).getTags().get(1).getTagId());
        assertEquals("ruby", articles.get(0).getTags().get(1).getTagName());
        assertEquals(4, articles.get(0).getFeedbackCount());
        assertEquals(2, articles.get(0).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(0).getPostedUser().getUserId());
        assertEquals("a", articles.get(0).getPostedUser().getDisplayName());
        assertEquals("a", articles.get(0).getPostedUser().getPhotoUrl());
        assertEquals(1, articles.get(0).getQiitaRecommendPoint());

        assertEquals(1, articles.get(11).getArticleId());
        assertEquals("title1", articles.get(11).getTitle());
        assertEquals(createDateLast, articles.get(11).getCreatedAt());
        assertEquals(updateDateLast, articles.get(11).getUpdatedAt());
        assertEquals(1, articles.get(11).getStateFlag());
        assertEquals(1, articles.get(11).getTags().get(0).getTagId());
        assertEquals("Java", articles.get(11).getTags().get(0).getTagName());
        assertEquals(5, articles.get(11).getTags().get(1).getTagId());
        assertEquals("go", articles.get(11).getTags().get(1).getTagName());
        assertEquals(5, articles.get(11).getFeedbackCount());
        assertNull(articles.get(11).getRegisteredMyArticleCount());
        assertEquals(1, articles.get(11).getPostedUser().getUserId());
        assertEquals("a", articles.get(11).getPostedUser().getDisplayName());
        assertEquals("a", articles.get(11).getPostedUser().getPhotoUrl());
        assertEquals(2, articles.get(11).getQiitaRecommendPoint());
    }

    @Test
    void findByArticleIdAndUserId_正常系_存在する記事IDとユーザーIDに該当する記事が存在する(){
        String userInsertSql="INSERT INTO users (uid, photo_url, display_name, password) VALUES ('a', 'a', 'a', 'a');";
        String articleInsertSql="INSERT INTO articles (user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) VALUES (1, '2020-10-01 00:00:00', '2020-10-02 00:00:00', 'title1', '#content1', null, 1)";
        jdbcTemplate.execute(userInsertSql);
        jdbcTemplate.execute(articleInsertSql);
        Integer articleId=articleMapper.findByArticleIdAndUserId(1,1);
        assertEquals(1,articleId);
    }
    @Test
    void findByArticleIdAndUserId_正常系_存在する記事IDとユーザーIDに該当する記事が存在しない(){
        String userInsertSql="INSERT INTO users (uid, photo_url, display_name, password) VALUES ('a', 'a', 'a', 'a');";
        String user2InsertSql="INSERT INTO users (uid, photo_url, display_name, password) VALUES ('b', 'b', 'b', 'b');";
        String articleInsertSql="INSERT INTO articles (user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) VALUES (1, '2020-10-01 00:00:00', '2020-10-02 00:00:00', 'title1', '#content1', null, 1)";
        jdbcTemplate.execute(userInsertSql);
        jdbcTemplate.execute(user2InsertSql);
        jdbcTemplate.execute(articleInsertSql);
        Integer articleId=articleMapper.findByArticleIdAndUserId(1,2);
        assertEquals(null,articleId);
    }
    @Test
    void findByArticleIdAndUserId_正常系_存在しない記事IDとユーザーIDに該当する記事が存在しない(){
        String userInsertSql="INSERT INTO users (uid, photo_url, display_name, password) VALUES ('a', 'a', 'a', 'a');";
        jdbcTemplate.execute(userInsertSql);
        Integer articleId=articleMapper.findByArticleIdAndUserId(1,1);
        assertEquals(null,articleId);
    }


    @Test
    void getQiitaArticleId_nonNull(){
        String insertUsers = "INSERT INTO users (user_id) VALUES (1), (2)";
        String insertArticles =
                "INSERT INTO articles ( user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) " +
                        "VALUES(1,NOW(),NOW(),'test title','content title','qiita_article_id',1)";

        jdbcTemplate.execute(insertUsers);
        jdbcTemplate.execute(insertArticles);

        assertEquals("qiita_article_id",articleMapper.getQiitaArticleId(1));
    }

    @Test
    void getQiitaArticleId_null(){
        String insertUsers = "INSERT INTO users (user_id) VALUES (1), (2)";
        String insertArticles =
                "INSERT INTO articles ( user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) " +
                        "VALUES(1,NOW(),NOW(),'test title','content title',null,1)";

        jdbcTemplate.execute(insertUsers);
        jdbcTemplate.execute(insertArticles);

        assertNull(articleMapper.getQiitaArticleId(1));
    }
}
