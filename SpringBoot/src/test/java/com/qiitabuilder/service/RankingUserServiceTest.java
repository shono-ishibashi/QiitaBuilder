package com.qiitabuilder.service;

import com.qiitabuilder.domain.RankingUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RankingUserServiceTest {

    @Autowired
    private RankingUserService rankingUserService;
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

    public void setUpForTest() {
        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr = CollectionSQL.insertArticles.split("\n", 0);
        String[] feedbackSqlArr = CollectionSQL.insertFeedbacks.split("\n", 0);
        String[] qiitaRecommendSqlArr = CollectionSQL.insertQiitaRecommends.split("\n", 0);
        String[] tagSqlArr = CollectionSQL.insertTags.split("\n", 0);
        String[] artTagRelSqlArr = CollectionSQL.insertArticlesTagsRelations.split("\n", 0);
        String[] myArtSqlArr = CollectionSQL.insertMyArticles.split("\n", 0);
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
        for (String sql : tagSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (String sql : artTagRelSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (String sql : myArtSqlArr) {
            jdbcTemplate.execute(sql);
        }
    }

    @Test
    void fetchFBCountRank() {
        setUpForTest();
        List<RankingUser> rankingUserList = rankingUserService.fetchFBCountRank();
        assertEquals(35, rankingUserList.size());

        //////////最初(1~5位)
        assertEquals(10, rankingUserList.get(0).getUser().getUserId());
        assertEquals("そうし", rankingUserList.get(0).getUser().getDisplayName());
        assertEquals("uuu", rankingUserList.get(0).getUser().getPhotoUrl());
        assertEquals(15, rankingUserList.get(0).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(0).getUser().getPostedArticleCount());
        assertEquals(5, rankingUserList.get(0).getUser().getQiitaRecommendedAllCount());
        assertEquals(41, rankingUserList.get(0).getRelationArticle().getArticleId());
        assertEquals(7, rankingUserList.get(0).getRelationArticle().getPostedUser().getUserId());
        assertEquals("しんじ", rankingUserList.get(0).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("ggg", rankingUserList.get(0).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 4, 0, 0, 0), rankingUserList.get(0).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 5, 0, 0, 0), rankingUserList.get(0).getRelationArticle().getUpdatedAt());
        assertEquals("title41", rankingUserList.get(0).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(0).getRelationArticle().getStateFlag());
        assertEquals(2, rankingUserList.get(0).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("ruby", rankingUserList.get(0).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(3, rankingUserList.get(0).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("javascript", rankingUserList.get(0).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(3, rankingUserList.get(0).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(0).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(2, rankingUserList.get(0).getRelationArticle().getFeedbackCount());
        assertEquals(1, rankingUserList.get(0).getRank());

        assertEquals(20, rankingUserList.get(1).getUser().getUserId());
        assertEquals("user20", rankingUserList.get(1).getUser().getDisplayName());
        assertEquals("photo20", rankingUserList.get(1).getUser().getPhotoUrl());
        assertEquals(14, rankingUserList.get(1).getUser().getFeedbackCount());
        assertEquals(0, rankingUserList.get(1).getUser().getPostedArticleCount());
        assertEquals(0, rankingUserList.get(1).getUser().getQiitaRecommendedAllCount());
        assertEquals(36, rankingUserList.get(1).getRelationArticle().getArticleId());
        assertEquals(5, rankingUserList.get(1).getRelationArticle().getPostedUser().getUserId());
        assertEquals("test", rankingUserList.get(1).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("test_photo", rankingUserList.get(1).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 22, 0, 0, 0), rankingUserList.get(1).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 23, 0, 0, 0), rankingUserList.get(1).getRelationArticle().getUpdatedAt());
        assertEquals("title36", rankingUserList.get(1).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(1).getRelationArticle().getStateFlag());
        assertEquals(3, rankingUserList.get(1).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("javascript", rankingUserList.get(1).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(1).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(1).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(3, rankingUserList.get(1).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(2, rankingUserList.get(1).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(3, rankingUserList.get(1).getRelationArticle().getFeedbackCount());
        assertEquals(2, rankingUserList.get(1).getRank());

        assertEquals(30, rankingUserList.get(2).getUser().getUserId());
        assertEquals("user30", rankingUserList.get(2).getUser().getDisplayName());
        assertEquals("photo30", rankingUserList.get(2).getUser().getPhotoUrl());
        assertEquals(13, rankingUserList.get(2).getUser().getFeedbackCount());
        assertEquals(0, rankingUserList.get(2).getUser().getPostedArticleCount());
        assertEquals(0, rankingUserList.get(2).getUser().getQiitaRecommendedAllCount());
        assertEquals(45, rankingUserList.get(2).getRelationArticle().getArticleId());
        assertEquals(9, rankingUserList.get(2).getRelationArticle().getPostedUser().getUserId());
        assertEquals("ゆみ", rankingUserList.get(2).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("rrr", rankingUserList.get(2).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 31, 0, 0, 0), rankingUserList.get(2).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 11, 1, 0, 0, 0), rankingUserList.get(2).getRelationArticle().getUpdatedAt());
        assertEquals("title45", rankingUserList.get(2).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(2).getRelationArticle().getStateFlag());
        assertEquals(3, rankingUserList.get(2).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("javascript", rankingUserList.get(2).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(2).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(2).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(4, rankingUserList.get(2).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(2).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(2, rankingUserList.get(2).getRelationArticle().getFeedbackCount());
        assertEquals(3, rankingUserList.get(2).getRank());

        assertEquals(40, rankingUserList.get(3).getUser().getUserId());
        assertEquals("user40", rankingUserList.get(3).getUser().getDisplayName());
        assertEquals("photo40", rankingUserList.get(3).getUser().getPhotoUrl());
        assertEquals(12, rankingUserList.get(3).getUser().getFeedbackCount());
        assertEquals(0, rankingUserList.get(3).getUser().getPostedArticleCount());
        assertEquals(0, rankingUserList.get(3).getUser().getQiitaRecommendedAllCount());
        assertEquals(46, rankingUserList.get(3).getRelationArticle().getArticleId());
        assertEquals(10, rankingUserList.get(3).getRelationArticle().getPostedUser().getUserId());
        assertEquals("そうし", rankingUserList.get(3).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("uuu", rankingUserList.get(3).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 15, 0, 0, 0), rankingUserList.get(3).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 16, 0, 0, 0), rankingUserList.get(3).getRelationArticle().getUpdatedAt());
        assertEquals("title46", rankingUserList.get(3).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(3).getRelationArticle().getStateFlag());
        assertEquals(1, rankingUserList.get(3).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("Java", rankingUserList.get(3).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(3).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(3).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(5, rankingUserList.get(3).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(1, rankingUserList.get(3).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(1, rankingUserList.get(3).getRelationArticle().getFeedbackCount());
        assertEquals(4, rankingUserList.get(3).getRank());

        assertEquals(5, rankingUserList.get(4).getUser().getUserId());
        assertEquals("test", rankingUserList.get(4).getUser().getDisplayName());
        assertEquals("test_photo", rankingUserList.get(4).getUser().getPhotoUrl());
        assertEquals(11, rankingUserList.get(4).getUser().getFeedbackCount());
        assertEquals(4, rankingUserList.get(4).getUser().getPostedArticleCount());
        assertEquals(9, rankingUserList.get(4).getUser().getQiitaRecommendedAllCount());
        assertEquals(110, rankingUserList.get(4).getRelationArticle().getArticleId());
        assertEquals(22, rankingUserList.get(4).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user22", rankingUserList.get(4).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo22", rankingUserList.get(4).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 6, 0, 0, 0), rankingUserList.get(4).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 7, 0, 0, 0), rankingUserList.get(4).getRelationArticle().getUpdatedAt());
        assertEquals("title110", rankingUserList.get(4).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(4).getRelationArticle().getStateFlag());
        assertEquals(4, rankingUserList.get(4).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("php", rankingUserList.get(4).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(4).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(4).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(2, rankingUserList.get(4).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(1, rankingUserList.get(4).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(2, rankingUserList.get(4).getRelationArticle().getFeedbackCount());
        assertEquals(5, rankingUserList.get(4).getRank());

        //////////中間(15~19位)
        assertEquals(23, rankingUserList.get(14).getUser().getUserId());
        assertEquals("user23", rankingUserList.get(14).getUser().getDisplayName());
        assertEquals("photo23", rankingUserList.get(14).getUser().getPhotoUrl());
        assertEquals(6, rankingUserList.get(14).getUser().getFeedbackCount());
        assertEquals(8, rankingUserList.get(14).getUser().getPostedArticleCount());
        assertEquals(8, rankingUserList.get(14).getUser().getQiitaRecommendedAllCount());
        assertEquals(11, rankingUserList.get(14).getRelationArticle().getArticleId());
        assertEquals(1, rankingUserList.get(14).getRelationArticle().getPostedUser().getUserId());
        assertEquals("a", rankingUserList.get(14).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("a", rankingUserList.get(14).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 21, 0, 0, 0), rankingUserList.get(14).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 22, 0, 0, 0), rankingUserList.get(14).getRelationArticle().getUpdatedAt());
        assertEquals("title11", rankingUserList.get(14).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(14).getRelationArticle().getStateFlag());
        assertEquals(1, rankingUserList.get(14).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("Java", rankingUserList.get(14).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(3, rankingUserList.get(14).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("javascript", rankingUserList.get(14).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(3, rankingUserList.get(14).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(14).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(2, rankingUserList.get(14).getRelationArticle().getFeedbackCount());
        assertEquals(14, rankingUserList.get(14).getRank());

        assertEquals(4, rankingUserList.get(15).getUser().getUserId());
        assertEquals("d", rankingUserList.get(15).getUser().getDisplayName());
        assertEquals("d", rankingUserList.get(15).getUser().getPhotoUrl());
        assertEquals(5, rankingUserList.get(15).getUser().getFeedbackCount());
        assertEquals(5, rankingUserList.get(15).getUser().getPostedArticleCount());
        assertEquals(3, rankingUserList.get(15).getUser().getQiitaRecommendedAllCount());
        assertEquals(193, rankingUserList.get(15).getRelationArticle().getArticleId());
        assertEquals(39, rankingUserList.get(15).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user39", rankingUserList.get(15).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo39", rankingUserList.get(15).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 11, 7, 0, 0, 0), rankingUserList.get(15).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 11, 8, 0, 0, 0), rankingUserList.get(15).getRelationArticle().getUpdatedAt());
        assertEquals("title193", rankingUserList.get(15).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(15).getRelationArticle().getStateFlag());
        assertEquals(2, rankingUserList.get(15).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("ruby", rankingUserList.get(15).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(3, rankingUserList.get(15).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("javascript", rankingUserList.get(15).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(2, rankingUserList.get(15).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(15).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(1, rankingUserList.get(15).getRelationArticle().getFeedbackCount());
        assertEquals(16, rankingUserList.get(15).getRank());

        assertEquals(33, rankingUserList.get(16).getUser().getUserId());
        assertEquals("user33", rankingUserList.get(16).getUser().getDisplayName());
        assertEquals("photo33", rankingUserList.get(16).getUser().getPhotoUrl());
        assertEquals(5, rankingUserList.get(16).getUser().getFeedbackCount());
        assertEquals(8, rankingUserList.get(16).getUser().getPostedArticleCount());
        assertEquals(2, rankingUserList.get(16).getUser().getQiitaRecommendedAllCount());
        assertEquals(43, rankingUserList.get(16).getRelationArticle().getArticleId());
        assertEquals(8, rankingUserList.get(16).getRelationArticle().getPostedUser().getUserId());
        assertEquals("しょーの", rankingUserList.get(16).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("nnn", rankingUserList.get(16).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 10, 0, 0, 0), rankingUserList.get(16).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 11, 0, 0, 0), rankingUserList.get(16).getRelationArticle().getUpdatedAt());
        assertEquals("title43", rankingUserList.get(16).getRelationArticle().getTitle());
        assertEquals(2, rankingUserList.get(16).getRelationArticle().getStateFlag());
        assertEquals(4, rankingUserList.get(16).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("php", rankingUserList.get(16).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(16).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(16).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(4, rankingUserList.get(16).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(16).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(1, rankingUserList.get(16).getRelationArticle().getFeedbackCount());
        assertEquals(16, rankingUserList.get(16).getRank());

        assertEquals(14, rankingUserList.get(17).getUser().getUserId());
        assertEquals("user14", rankingUserList.get(17).getUser().getDisplayName());
        assertEquals("photo14", rankingUserList.get(17).getUser().getPhotoUrl());
        assertEquals(4, rankingUserList.get(17).getUser().getFeedbackCount());
        assertEquals(6, rankingUserList.get(17).getUser().getPostedArticleCount());
        assertEquals(5, rankingUserList.get(17).getUser().getQiitaRecommendedAllCount());
        assertEquals(101, rankingUserList.get(17).getRelationArticle().getArticleId());
        assertEquals(21, rankingUserList.get(17).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user21", rankingUserList.get(17).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo21", rankingUserList.get(17).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 14, 0, 0, 0), rankingUserList.get(17).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 15, 0, 0, 0), rankingUserList.get(17).getRelationArticle().getUpdatedAt());
        assertEquals("title101", rankingUserList.get(17).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(17).getRelationArticle().getStateFlag());
        assertEquals(2, rankingUserList.get(17).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("ruby", rankingUserList.get(17).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(3, rankingUserList.get(17).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("javascript", rankingUserList.get(17).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(0, rankingUserList.get(17).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(17).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(2, rankingUserList.get(17).getRelationArticle().getFeedbackCount());
        assertEquals(18, rankingUserList.get(17).getRank());

        assertEquals(24, rankingUserList.get(18).getUser().getUserId());
        assertEquals("user24", rankingUserList.get(18).getUser().getDisplayName());
        assertEquals("photo24", rankingUserList.get(18).getUser().getPhotoUrl());
        assertEquals(4, rankingUserList.get(18).getUser().getFeedbackCount());
        assertEquals(6, rankingUserList.get(18).getUser().getPostedArticleCount());
        assertEquals(7, rankingUserList.get(18).getUser().getQiitaRecommendedAllCount());
        assertEquals(45, rankingUserList.get(18).getRelationArticle().getArticleId());
        assertEquals(9, rankingUserList.get(18).getRelationArticle().getPostedUser().getUserId());
        assertEquals("ゆみ", rankingUserList.get(18).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("rrr", rankingUserList.get(18).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 31, 0, 0, 0), rankingUserList.get(18).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 11, 1, 0, 0, 0), rankingUserList.get(18).getRelationArticle().getUpdatedAt());
        assertEquals("title45", rankingUserList.get(18).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(18).getRelationArticle().getStateFlag());
        assertEquals(3, rankingUserList.get(18).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("javascript", rankingUserList.get(18).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(18).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(18).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(4, rankingUserList.get(18).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(18).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(2, rankingUserList.get(18).getRelationArticle().getFeedbackCount());
        assertEquals(18, rankingUserList.get(18).getRank());

        assertNotNull(rankingUserList.get(19).getRelationArticle());
        assertNull(rankingUserList.get(20).getRelationArticle());

        //////////最後(31~35位)
        assertEquals(9, rankingUserList.get(30).getUser().getUserId());
        assertEquals("ゆみ", rankingUserList.get(30).getUser().getDisplayName());
        assertEquals("rrr", rankingUserList.get(30).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(30).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(30).getUser().getPostedArticleCount());
        assertEquals(4, rankingUserList.get(30).getUser().getQiitaRecommendedAllCount());
        assertNull(rankingUserList.get(30).getRelationArticle());
        assertEquals(31, rankingUserList.get(30).getRank());

        assertEquals(19, rankingUserList.get(31).getUser().getUserId());
        assertEquals("user19", rankingUserList.get(31).getUser().getDisplayName());
        assertEquals("photo19", rankingUserList.get(31).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(31).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(31).getUser().getPostedArticleCount());
        assertEquals(3, rankingUserList.get(31).getUser().getQiitaRecommendedAllCount());
        assertNull(rankingUserList.get(31).getRelationArticle());
        assertEquals(31, rankingUserList.get(31).getRank());

        assertEquals(28, rankingUserList.get(32).getUser().getUserId());
        assertEquals("user28", rankingUserList.get(32).getUser().getDisplayName());
        assertEquals("photo28", rankingUserList.get(32).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(32).getUser().getFeedbackCount());
        assertEquals(2, rankingUserList.get(32).getUser().getPostedArticleCount());
        assertEquals(2, rankingUserList.get(32).getUser().getQiitaRecommendedAllCount());
        assertNull(rankingUserList.get(32).getRelationArticle());
        assertEquals(31, rankingUserList.get(32).getRank());

        assertEquals(29, rankingUserList.get(33).getUser().getUserId());
        assertEquals("user29", rankingUserList.get(33).getUser().getDisplayName());
        assertEquals("photo29", rankingUserList.get(33).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(33).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(33).getUser().getPostedArticleCount());
        assertEquals(2, rankingUserList.get(33).getUser().getQiitaRecommendedAllCount());
        assertNull(rankingUserList.get(33).getRelationArticle());
        assertEquals(31, rankingUserList.get(33).getRank());

        assertEquals(38, rankingUserList.get(34).getUser().getUserId());
        assertEquals("user38", rankingUserList.get(34).getUser().getDisplayName());
        assertEquals("photo38", rankingUserList.get(34).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(34).getUser().getFeedbackCount());
        assertEquals(2, rankingUserList.get(34).getUser().getPostedArticleCount());
        assertEquals(3, rankingUserList.get(34).getUser().getQiitaRecommendedAllCount());
        assertNull(rankingUserList.get(34).getRelationArticle());
        assertEquals(31, rankingUserList.get(34).getRank());
    }

    @Test
    void fetchFBCountRank_none_exist_rankingUser() {
        List<RankingUser> rankingUserList = rankingUserService.fetchFBCountRank();
        assertTrue(rankingUserList.isEmpty());
    }

    @Test
    void fetchArticleCountRank() {
        setUpForTest();
        jdbcTemplate.execute("DELETE FROM qiita_recommends WHERE posted_user_id = 26;");
        List<RankingUser> rankingUserList = rankingUserService.fetchArticleCountRank();
        assertEquals(35, rankingUserList.size());

        //////////最初
        assertEquals(31, rankingUserList.get(0).getUser().getUserId());
        assertEquals("user31", rankingUserList.get(0).getUser().getDisplayName());
        assertEquals("photo31", rankingUserList.get(0).getUser().getPhotoUrl());
        assertEquals(7, rankingUserList.get(0).getUser().getFeedbackCount());
        assertEquals(15, rankingUserList.get(0).getUser().getPostedArticleCount());
        assertEquals(11, rankingUserList.get(0).getUser().getQiitaRecommendedAllCount());
        assertEquals(143, rankingUserList.get(0).getRelationArticle().getArticleId());
        assertEquals(31, rankingUserList.get(0).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user31", rankingUserList.get(0).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo31", rankingUserList.get(0).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 4, 0, 0, 0), rankingUserList.get(0).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 5, 0, 0, 0), rankingUserList.get(0).getRelationArticle().getUpdatedAt());
        assertEquals("title143", rankingUserList.get(0).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(0).getRelationArticle().getStateFlag());
        assertEquals(1, rankingUserList.get(0).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("Java", rankingUserList.get(0).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(0).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(0).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(2, rankingUserList.get(0).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(0).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(0, rankingUserList.get(0).getRelationArticle().getFeedbackCount());
        assertEquals(1, rankingUserList.get(0).getRank());

        assertEquals(21, rankingUserList.get(1).getUser().getUserId());
        assertEquals("user21", rankingUserList.get(1).getUser().getDisplayName());
        assertEquals("photo21", rankingUserList.get(1).getUser().getPhotoUrl());
        assertEquals(7, rankingUserList.get(1).getUser().getFeedbackCount());
        assertEquals(14, rankingUserList.get(1).getUser().getPostedArticleCount());
        assertEquals(3, rankingUserList.get(1).getUser().getQiitaRecommendedAllCount());
        assertEquals(109, rankingUserList.get(1).getRelationArticle().getArticleId());
        assertEquals(21, rankingUserList.get(1).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user21", rankingUserList.get(1).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo21", rankingUserList.get(1).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 11, 4, 0, 0, 0), rankingUserList.get(1).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 11, 5, 0, 0, 0), rankingUserList.get(1).getRelationArticle().getUpdatedAt());
        assertEquals("title109", rankingUserList.get(1).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(1).getRelationArticle().getStateFlag());
        assertEquals(2, rankingUserList.get(1).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("ruby", rankingUserList.get(1).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(4, rankingUserList.get(1).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("php", rankingUserList.get(1).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(1, rankingUserList.get(1).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(1).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(1, rankingUserList.get(1).getRelationArticle().getFeedbackCount());
        assertEquals(2, rankingUserList.get(1).getRank());


        assertEquals(11, rankingUserList.get(2).getUser().getUserId());
        assertEquals("user", rankingUserList.get(2).getUser().getDisplayName());
        assertEquals("user_photo", rankingUserList.get(2).getUser().getPhotoUrl());
        assertEquals(8, rankingUserList.get(2).getUser().getFeedbackCount());
        assertEquals(13, rankingUserList.get(2).getUser().getPostedArticleCount());
        assertEquals(15, rankingUserList.get(2).getUser().getQiitaRecommendedAllCount());
        assertEquals(59, rankingUserList.get(2).getRelationArticle().getArticleId());
        assertEquals(11, rankingUserList.get(2).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user", rankingUserList.get(2).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("user_photo", rankingUserList.get(2).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 11, 4, 0, 0, 0), rankingUserList.get(2).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 11, 5, 0, 0, 0), rankingUserList.get(2).getRelationArticle().getUpdatedAt());
        assertEquals("title59", rankingUserList.get(2).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(2).getRelationArticle().getStateFlag());
        assertEquals(2, rankingUserList.get(2).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("ruby", rankingUserList.get(2).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(4, rankingUserList.get(2).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("php", rankingUserList.get(2).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(2, rankingUserList.get(2).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(2).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(0, rankingUserList.get(2).getRelationArticle().getFeedbackCount());
        assertEquals(3, rankingUserList.get(2).getRank());

        assertEquals(1, rankingUserList.get(3).getUser().getUserId());
        assertEquals("a", rankingUserList.get(3).getUser().getDisplayName());
        assertEquals("a", rankingUserList.get(3).getUser().getPhotoUrl());
        assertEquals(8, rankingUserList.get(3).getUser().getFeedbackCount());
        assertEquals(12, rankingUserList.get(3).getUser().getPostedArticleCount());
        assertEquals(14, rankingUserList.get(3).getUser().getQiitaRecommendedAllCount());
        assertEquals(11, rankingUserList.get(3).getRelationArticle().getArticleId());
        assertEquals(1, rankingUserList.get(3).getRelationArticle().getPostedUser().getUserId());
        assertEquals("a", rankingUserList.get(3).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("a", rankingUserList.get(3).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 21, 0, 0, 0), rankingUserList.get(3).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 22, 0, 0, 0), rankingUserList.get(3).getRelationArticle().getUpdatedAt());
        assertEquals("title11", rankingUserList.get(3).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(3).getRelationArticle().getStateFlag());
        assertEquals(1, rankingUserList.get(3).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("Java", rankingUserList.get(3).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(3, rankingUserList.get(3).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("javascript", rankingUserList.get(3).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(3, rankingUserList.get(3).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(3).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(2, rankingUserList.get(3).getRelationArticle().getFeedbackCount());
        assertEquals(4, rankingUserList.get(3).getRank());

        assertEquals(32, rankingUserList.get(4).getUser().getUserId());
        assertEquals("user32", rankingUserList.get(4).getUser().getDisplayName());
        assertEquals("photo32", rankingUserList.get(4).getUser().getPhotoUrl());
        assertEquals(0, rankingUserList.get(4).getUser().getFeedbackCount());
        assertEquals(11, rankingUserList.get(4).getUser().getPostedArticleCount());
        assertEquals(7, rankingUserList.get(4).getUser().getQiitaRecommendedAllCount());
        assertEquals(158, rankingUserList.get(4).getRelationArticle().getArticleId());
        assertEquals(32, rankingUserList.get(4).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user32", rankingUserList.get(4).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo32", rankingUserList.get(4).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 2, 0, 0, 0), rankingUserList.get(4).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 3, 0, 0, 0), rankingUserList.get(4).getRelationArticle().getUpdatedAt());
        assertEquals("title158", rankingUserList.get(4).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(4).getRelationArticle().getStateFlag());
        assertEquals(4, rankingUserList.get(4).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("php", rankingUserList.get(4).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(4).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(4).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(2, rankingUserList.get(4).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(1, rankingUserList.get(4).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(0, rankingUserList.get(4).getRelationArticle().getFeedbackCount());
        assertEquals(5, rankingUserList.get(4).getRank());

        //////////中間
        assertEquals(24, rankingUserList.get(14).getUser().getUserId());
        assertEquals("user24", rankingUserList.get(14).getUser().getDisplayName());
        assertEquals("photo24", rankingUserList.get(14).getUser().getPhotoUrl());
        assertEquals(4, rankingUserList.get(14).getUser().getFeedbackCount());
        assertEquals(6, rankingUserList.get(14).getUser().getPostedArticleCount());
        assertEquals(7, rankingUserList.get(14).getUser().getQiitaRecommendedAllCount());
        assertEquals(131, rankingUserList.get(14).getRelationArticle().getArticleId());
        assertEquals(24, rankingUserList.get(14).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user24", rankingUserList.get(14).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo24", rankingUserList.get(14).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 24, 0, 0, 0), rankingUserList.get(14).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 25, 0, 0, 0), rankingUserList.get(14).getRelationArticle().getUpdatedAt());
        assertEquals("title131", rankingUserList.get(14).getRelationArticle().getTitle());
        assertEquals(2, rankingUserList.get(14).getRelationArticle().getStateFlag());
        assertEquals(2, rankingUserList.get(14).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("ruby", rankingUserList.get(14).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(4, rankingUserList.get(14).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("php", rankingUserList.get(14).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(2, rankingUserList.get(14).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(14).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(3, rankingUserList.get(14).getRelationArticle().getFeedbackCount());
        assertEquals(14, rankingUserList.get(14).getRank());


        assertEquals(4, rankingUserList.get(15).getUser().getUserId());
        assertEquals("d", rankingUserList.get(15).getUser().getDisplayName());
        assertEquals("d", rankingUserList.get(15).getUser().getPhotoUrl());
        assertEquals(5, rankingUserList.get(15).getUser().getFeedbackCount());
        assertEquals(5, rankingUserList.get(15).getUser().getPostedArticleCount());
        assertEquals(3, rankingUserList.get(15).getUser().getQiitaRecommendedAllCount());
        assertEquals(29, rankingUserList.get(15).getRelationArticle().getArticleId());
        assertEquals(4, rankingUserList.get(15).getRelationArticle().getPostedUser().getUserId());
        assertEquals("d", rankingUserList.get(15).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("d", rankingUserList.get(15).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 1, 0, 0, 0), rankingUserList.get(15).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 2, 0, 0, 0), rankingUserList.get(15).getRelationArticle().getUpdatedAt());
        assertEquals("title29", rankingUserList.get(15).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(15).getRelationArticle().getStateFlag());
        assertEquals(2, rankingUserList.get(15).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("ruby", rankingUserList.get(15).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(4, rankingUserList.get(15).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("php", rankingUserList.get(15).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(3, rankingUserList.get(15).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(15).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(2, rankingUserList.get(15).getRelationArticle().getFeedbackCount());
        assertEquals(16, rankingUserList.get(15).getRank());

        assertEquals(15, rankingUserList.get(16).getUser().getUserId());
        assertEquals("user15", rankingUserList.get(16).getUser().getDisplayName());
        assertEquals("photo15", rankingUserList.get(16).getUser().getPhotoUrl());
        assertEquals(9, rankingUserList.get(16).getUser().getFeedbackCount());
        assertEquals(5, rankingUserList.get(16).getUser().getPostedArticleCount());
        assertEquals(7, rankingUserList.get(16).getUser().getQiitaRecommendedAllCount());
        assertEquals(86, rankingUserList.get(16).getRelationArticle().getArticleId());
        assertEquals(15, rankingUserList.get(16).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user15", rankingUserList.get(16).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo15", rankingUserList.get(16).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 11, 3, 0, 0, 0), rankingUserList.get(16).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 11, 4, 0, 0, 0), rankingUserList.get(16).getRelationArticle().getUpdatedAt());
        assertEquals("title86", rankingUserList.get(16).getRelationArticle().getTitle());
        assertEquals(2, rankingUserList.get(16).getRelationArticle().getStateFlag());
        assertEquals(2, rankingUserList.get(16).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("ruby", rankingUserList.get(16).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(4, rankingUserList.get(16).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("php", rankingUserList.get(16).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(2, rankingUserList.get(16).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(1, rankingUserList.get(16).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(0, rankingUserList.get(16).getRelationArticle().getFeedbackCount());
        assertEquals(16, rankingUserList.get(16).getRank());

        assertEquals(5, rankingUserList.get(17).getUser().getUserId());
        assertEquals("test", rankingUserList.get(17).getUser().getDisplayName());
        assertEquals("test_photo", rankingUserList.get(17).getUser().getPhotoUrl());
        assertEquals(11, rankingUserList.get(17).getUser().getFeedbackCount());
        assertEquals(4, rankingUserList.get(17).getUser().getPostedArticleCount());
        assertEquals(9, rankingUserList.get(17).getUser().getQiitaRecommendedAllCount());
        assertEquals(36, rankingUserList.get(17).getRelationArticle().getArticleId());
        assertEquals(5, rankingUserList.get(17).getRelationArticle().getPostedUser().getUserId());
        assertEquals("test", rankingUserList.get(17).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("test_photo", rankingUserList.get(17).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 22, 0, 0, 0), rankingUserList.get(17).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 23, 0, 0, 0), rankingUserList.get(17).getRelationArticle().getUpdatedAt());
        assertEquals("title36", rankingUserList.get(17).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(17).getRelationArticle().getStateFlag());
        assertEquals(3, rankingUserList.get(17).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("javascript", rankingUserList.get(17).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(17).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(17).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(3, rankingUserList.get(17).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(2, rankingUserList.get(17).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(3, rankingUserList.get(17).getRelationArticle().getFeedbackCount());
        assertEquals(18, rankingUserList.get(17).getRank());

        assertEquals(26, rankingUserList.get(18).getUser().getUserId());
        assertEquals("user26", rankingUserList.get(18).getUser().getDisplayName());
        assertEquals("photo26", rankingUserList.get(18).getUser().getPhotoUrl());
        assertEquals(3, rankingUserList.get(18).getUser().getFeedbackCount());
        assertEquals(4, rankingUserList.get(18).getUser().getPostedArticleCount());
        assertEquals(0, rankingUserList.get(18).getUser().getQiitaRecommendedAllCount());
        assertEquals(136, rankingUserList.get(18).getRelationArticle().getArticleId());
        assertEquals(26, rankingUserList.get(18).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user26", rankingUserList.get(18).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo26", rankingUserList.get(18).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 31, 0, 0, 0), rankingUserList.get(18).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 11, 1, 0, 0, 0), rankingUserList.get(18).getRelationArticle().getUpdatedAt());
        assertEquals("title136", rankingUserList.get(18).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(18).getRelationArticle().getStateFlag());
        assertEquals(2, rankingUserList.get(18).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("ruby", rankingUserList.get(18).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(3, rankingUserList.get(18).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("javascript", rankingUserList.get(18).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(0, rankingUserList.get(18).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(2, rankingUserList.get(18).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(0, rankingUserList.get(18).getRelationArticle().getFeedbackCount());
        assertEquals(18, rankingUserList.get(18).getRank());

        assertNotNull(rankingUserList.get(19).getRelationArticle());
        assertNull(rankingUserList.get(20).getRelationArticle());

        //////////最後
        assertEquals(9, rankingUserList.get(30).getUser().getUserId());
        assertEquals("ゆみ", rankingUserList.get(30).getUser().getDisplayName());
        assertEquals("rrr", rankingUserList.get(30).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(30).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(30).getUser().getPostedArticleCount());
        assertEquals(4, rankingUserList.get(30).getUser().getQiitaRecommendedAllCount());
        assertEquals(31, rankingUserList.get(30).getRank());

        assertEquals(10, rankingUserList.get(31).getUser().getUserId());
        assertEquals("そうし", rankingUserList.get(31).getUser().getDisplayName());
        assertEquals("uuu", rankingUserList.get(31).getUser().getPhotoUrl());
        assertEquals(15, rankingUserList.get(31).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(31).getUser().getPostedArticleCount());
        assertEquals(5, rankingUserList.get(31).getUser().getQiitaRecommendedAllCount());
        assertEquals(31, rankingUserList.get(31).getRank());

        assertEquals(19, rankingUserList.get(32).getUser().getUserId());
        assertEquals("user19", rankingUserList.get(32).getUser().getDisplayName());
        assertEquals("photo19", rankingUserList.get(32).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(32).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(32).getUser().getPostedArticleCount());
        assertEquals(3, rankingUserList.get(32).getUser().getQiitaRecommendedAllCount());
        assertEquals(31, rankingUserList.get(32).getRank());

        assertEquals(29, rankingUserList.get(33).getUser().getUserId());
        assertEquals("user29", rankingUserList.get(33).getUser().getDisplayName());
        assertEquals("photo29", rankingUserList.get(33).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(33).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(33).getUser().getPostedArticleCount());
        assertEquals(2, rankingUserList.get(33).getUser().getQiitaRecommendedAllCount());
        assertEquals(31, rankingUserList.get(33).getRank());

        assertEquals(39, rankingUserList.get(34).getUser().getUserId());
        assertEquals("user39", rankingUserList.get(34).getUser().getDisplayName());
        assertEquals("photo39", rankingUserList.get(34).getUser().getPhotoUrl());
        assertEquals(0, rankingUserList.get(34).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(34).getUser().getPostedArticleCount());
        assertEquals(2, rankingUserList.get(34).getUser().getQiitaRecommendedAllCount());
        assertEquals(31, rankingUserList.get(34).getRank());
    }

    @Test
    void fetchArticleCountRank_none_exist_rankingUser() {
        List<RankingUser> rankingUserList = rankingUserService.fetchArticleCountRank();
        assertTrue(rankingUserList.isEmpty());
    }

    @Test
    void fetchQiitaCountRank() {
        setUpForTest();
        List<RankingUser> rankingUserList = rankingUserService.fetchQiitaCountRank();
        assertEquals(35, rankingUserList.size());

        //////////最初(1~5位)
        assertEquals(11, rankingUserList.get(0).getUser().getUserId());
        assertEquals("user", rankingUserList.get(0).getUser().getDisplayName());
        assertEquals("user_photo", rankingUserList.get(0).getUser().getPhotoUrl());
        assertEquals(8, rankingUserList.get(0).getUser().getFeedbackCount());
        assertEquals(13, rankingUserList.get(0).getUser().getPostedArticleCount());
        assertEquals(15, rankingUserList.get(0).getUser().getQiitaRecommendedAllCount());
        assertEquals(59, rankingUserList.get(0).getRelationArticle().getArticleId());
        assertEquals(11, rankingUserList.get(0).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user", rankingUserList.get(0).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("user_photo", rankingUserList.get(0).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 11, 4, 0, 0, 0), rankingUserList.get(0).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 11, 5, 0, 0, 0), rankingUserList.get(0).getRelationArticle().getUpdatedAt());
        assertEquals("title59", rankingUserList.get(0).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(0).getRelationArticle().getStateFlag());
        assertEquals(2, rankingUserList.get(0).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("ruby", rankingUserList.get(0).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(4, rankingUserList.get(0).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("php", rankingUserList.get(0).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(2, rankingUserList.get(0).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(0).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(0, rankingUserList.get(0).getRelationArticle().getFeedbackCount());
        assertEquals(1, rankingUserList.get(0).getRank());

        assertEquals(1, rankingUserList.get(1).getUser().getUserId());
        assertEquals("a", rankingUserList.get(1).getUser().getDisplayName());
        assertEquals("a", rankingUserList.get(1).getUser().getPhotoUrl());
        assertEquals(8, rankingUserList.get(1).getUser().getFeedbackCount());
        assertEquals(12, rankingUserList.get(1).getUser().getPostedArticleCount());
        assertEquals(14, rankingUserList.get(1).getUser().getQiitaRecommendedAllCount());
        assertEquals(11, rankingUserList.get(1).getRelationArticle().getArticleId());
        assertEquals(1, rankingUserList.get(1).getRelationArticle().getPostedUser().getUserId());
        assertEquals("a", rankingUserList.get(1).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("a", rankingUserList.get(1).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 21, 0, 0, 0), rankingUserList.get(1).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 22, 0, 0, 0), rankingUserList.get(1).getRelationArticle().getUpdatedAt());
        assertEquals("title11", rankingUserList.get(1).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(1).getRelationArticle().getStateFlag());
        assertEquals(1, rankingUserList.get(1).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("Java", rankingUserList.get(1).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(3, rankingUserList.get(1).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("javascript", rankingUserList.get(1).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(3, rankingUserList.get(1).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(1).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(2, rankingUserList.get(1).getRelationArticle().getFeedbackCount());
        assertEquals(2, rankingUserList.get(1).getRank());

        assertEquals(22, rankingUserList.get(2).getUser().getUserId());
        assertEquals("user22", rankingUserList.get(2).getUser().getDisplayName());
        assertEquals("photo22", rankingUserList.get(2).getUser().getPhotoUrl());
        assertEquals(0, rankingUserList.get(2).getUser().getFeedbackCount());
        assertEquals(9, rankingUserList.get(2).getUser().getPostedArticleCount());
        assertEquals(13, rankingUserList.get(2).getUser().getQiitaRecommendedAllCount());
        assertEquals(118, rankingUserList.get(2).getRelationArticle().getArticleId());
        assertEquals(22, rankingUserList.get(2).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user22", rankingUserList.get(2).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo22", rankingUserList.get(2).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 27, 0, 0, 0), rankingUserList.get(2).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 28, 0, 0, 0), rankingUserList.get(2).getRelationArticle().getUpdatedAt());
        assertEquals("title118", rankingUserList.get(2).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(2).getRelationArticle().getStateFlag());
        assertEquals(1, rankingUserList.get(2).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("Java", rankingUserList.get(2).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(3, rankingUserList.get(2).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("javascript", rankingUserList.get(2).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(2, rankingUserList.get(2).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(1, rankingUserList.get(2).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(0, rankingUserList.get(2).getRelationArticle().getFeedbackCount());
        assertEquals(3, rankingUserList.get(2).getRank());

        assertEquals(3, rankingUserList.get(3).getUser().getUserId());
        assertEquals("c", rankingUserList.get(3).getUser().getDisplayName());
        assertEquals("c", rankingUserList.get(3).getUser().getPhotoUrl());
        assertEquals(7, rankingUserList.get(3).getUser().getFeedbackCount());
        assertEquals(7, rankingUserList.get(3).getUser().getPostedArticleCount());
        assertEquals(12, rankingUserList.get(3).getUser().getQiitaRecommendedAllCount());
        assertEquals(22, rankingUserList.get(3).getRelationArticle().getArticleId());
        assertEquals(3, rankingUserList.get(3).getRelationArticle().getPostedUser().getUserId());
        assertEquals("c", rankingUserList.get(3).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("c", rankingUserList.get(3).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 5, 0, 0, 0), rankingUserList.get(3).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 6, 0, 0, 0), rankingUserList.get(3).getRelationArticle().getUpdatedAt());
        assertEquals("title22", rankingUserList.get(3).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(3).getRelationArticle().getStateFlag());
        assertEquals(4, rankingUserList.get(3).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("php", rankingUserList.get(3).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(3).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(3).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(4, rankingUserList.get(3).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(1, rankingUserList.get(3).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(1, rankingUserList.get(3).getRelationArticle().getFeedbackCount());
        assertEquals(4, rankingUserList.get(3).getRank());

        assertEquals(31, rankingUserList.get(4).getUser().getUserId());
        assertEquals("user31", rankingUserList.get(4).getUser().getDisplayName());
        assertEquals("photo31", rankingUserList.get(4).getUser().getPhotoUrl());
        assertEquals(7, rankingUserList.get(4).getUser().getFeedbackCount());
        assertEquals(15, rankingUserList.get(4).getUser().getPostedArticleCount());
        assertEquals(11, rankingUserList.get(4).getUser().getQiitaRecommendedAllCount());
        assertEquals(143, rankingUserList.get(4).getRelationArticle().getArticleId());
        assertEquals(31, rankingUserList.get(4).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user31", rankingUserList.get(4).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo31", rankingUserList.get(4).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 4, 0, 0, 0), rankingUserList.get(4).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 5, 0, 0, 0), rankingUserList.get(4).getRelationArticle().getUpdatedAt());
        assertEquals("title143", rankingUserList.get(4).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(4).getRelationArticle().getStateFlag());
        assertEquals(1, rankingUserList.get(4).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("Java", rankingUserList.get(4).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(4).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(4).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(2, rankingUserList.get(4).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(4).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(0, rankingUserList.get(4).getRelationArticle().getFeedbackCount());
        assertEquals(5, rankingUserList.get(4).getRank());

        //////////中間(15~19位)
        assertEquals(7, rankingUserList.get(14).getUser().getUserId());
        assertEquals("しんじ", rankingUserList.get(14).getUser().getDisplayName());
        assertEquals("ggg", rankingUserList.get(14).getUser().getPhotoUrl());
        assertEquals(3, rankingUserList.get(14).getUser().getFeedbackCount());
        assertEquals(2, rankingUserList.get(14).getUser().getPostedArticleCount());
        assertEquals(6, rankingUserList.get(14).getUser().getQiitaRecommendedAllCount());
        assertEquals(42, rankingUserList.get(14).getRelationArticle().getArticleId());
        assertEquals(7, rankingUserList.get(14).getRelationArticle().getPostedUser().getUserId());
        assertEquals("しんじ", rankingUserList.get(14).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("ggg", rankingUserList.get(14).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 11, 1, 0, 0, 0), rankingUserList.get(14).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 11, 2, 0, 0, 0), rankingUserList.get(14).getRelationArticle().getUpdatedAt());
        assertEquals("title42", rankingUserList.get(14).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(14).getRelationArticle().getStateFlag());
        assertEquals(1, rankingUserList.get(14).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("Java", rankingUserList.get(14).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(14).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(14).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(3, rankingUserList.get(14).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(1, rankingUserList.get(14).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(1, rankingUserList.get(14).getRelationArticle().getFeedbackCount());
        assertEquals(15, rankingUserList.get(14).getRank());

        assertEquals(34, rankingUserList.get(15).getUser().getUserId());
        assertEquals("user34", rankingUserList.get(15).getUser().getDisplayName());
        assertEquals("photo34", rankingUserList.get(15).getUser().getPhotoUrl());
        assertEquals(4, rankingUserList.get(15).getUser().getFeedbackCount());
        assertEquals(7, rankingUserList.get(15).getUser().getPostedArticleCount());
        assertEquals(6, rankingUserList.get(15).getUser().getQiitaRecommendedAllCount());
        assertEquals(178, rankingUserList.get(15).getRelationArticle().getArticleId());
        assertEquals(34, rankingUserList.get(15).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user34", rankingUserList.get(15).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo34", rankingUserList.get(15).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 12, 0, 0, 0), rankingUserList.get(15).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 13, 0, 0, 0), rankingUserList.get(15).getRelationArticle().getUpdatedAt());
        assertEquals("title178", rankingUserList.get(15).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(15).getRelationArticle().getStateFlag());
        assertEquals(1, rankingUserList.get(15).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("Java", rankingUserList.get(15).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(2, rankingUserList.get(15).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("ruby", rankingUserList.get(15).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(2, rankingUserList.get(15).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(1, rankingUserList.get(15).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(0, rankingUserList.get(15).getRelationArticle().getFeedbackCount());
        assertEquals(15, rankingUserList.get(15).getRank());

        assertEquals(10, rankingUserList.get(16).getUser().getUserId());
        assertEquals("そうし", rankingUserList.get(16).getUser().getDisplayName());
        assertEquals("uuu", rankingUserList.get(16).getUser().getPhotoUrl());
        assertEquals(15, rankingUserList.get(16).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(16).getUser().getPostedArticleCount());
        assertEquals(5, rankingUserList.get(16).getUser().getQiitaRecommendedAllCount());
        assertEquals(46, rankingUserList.get(16).getRelationArticle().getArticleId());
        assertEquals(10, rankingUserList.get(16).getRelationArticle().getPostedUser().getUserId());
        assertEquals("そうし", rankingUserList.get(16).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("uuu", rankingUserList.get(16).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 15, 0, 0, 0), rankingUserList.get(16).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 16, 0, 0, 0), rankingUserList.get(16).getRelationArticle().getUpdatedAt());
        assertEquals("title46", rankingUserList.get(16).getRelationArticle().getTitle());
        assertEquals(1, rankingUserList.get(16).getRelationArticle().getStateFlag());
        assertEquals(1, rankingUserList.get(16).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("Java", rankingUserList.get(16).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(16).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(16).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(5, rankingUserList.get(16).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(1, rankingUserList.get(16).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(1, rankingUserList.get(16).getRelationArticle().getFeedbackCount());
        assertEquals(17, rankingUserList.get(16).getRank());

        assertEquals(14, rankingUserList.get(17).getUser().getUserId());
        assertEquals("user14", rankingUserList.get(17).getUser().getDisplayName());
        assertEquals("photo14", rankingUserList.get(17).getUser().getPhotoUrl());
        assertEquals(4, rankingUserList.get(17).getUser().getFeedbackCount());
        assertEquals(6, rankingUserList.get(17).getUser().getPostedArticleCount());
        assertEquals(5, rankingUserList.get(17).getUser().getQiitaRecommendedAllCount());
        assertEquals(76, rankingUserList.get(17).getRelationArticle().getArticleId());
        assertEquals(14, rankingUserList.get(17).getRelationArticle().getPostedUser().getUserId());
        assertEquals("user14", rankingUserList.get(17).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("photo14", rankingUserList.get(17).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 10, 0, 0, 0), rankingUserList.get(17).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 11, 0, 0, 0), rankingUserList.get(17).getRelationArticle().getUpdatedAt());
        assertEquals("title76", rankingUserList.get(17).getRelationArticle().getTitle());
        assertEquals(2, rankingUserList.get(17).getRelationArticle().getStateFlag());
        assertEquals(1, rankingUserList.get(17).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("Java", rankingUserList.get(17).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(2, rankingUserList.get(17).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("ruby", rankingUserList.get(17).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(2, rankingUserList.get(17).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(2, rankingUserList.get(17).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(1, rankingUserList.get(17).getRelationArticle().getFeedbackCount());
        assertEquals(17, rankingUserList.get(17).getRank());

        assertEquals(8, rankingUserList.get(18).getUser().getUserId());
        assertEquals("しょーの", rankingUserList.get(18).getUser().getDisplayName());
        assertEquals("nnn", rankingUserList.get(18).getUser().getPhotoUrl());
        assertEquals(2, rankingUserList.get(18).getUser().getFeedbackCount());
        assertEquals(2, rankingUserList.get(18).getUser().getPostedArticleCount());
        assertEquals(4, rankingUserList.get(18).getUser().getQiitaRecommendedAllCount());
        assertEquals(43, rankingUserList.get(18).getRelationArticle().getArticleId());
        assertEquals(8, rankingUserList.get(18).getRelationArticle().getPostedUser().getUserId());
        assertEquals("しょーの", rankingUserList.get(18).getRelationArticle().getPostedUser().getDisplayName());
        assertEquals("nnn", rankingUserList.get(18).getRelationArticle().getPostedUser().getPhotoUrl());
        assertEquals(LocalDateTime.of(2020, 10, 10, 0, 0, 0), rankingUserList.get(18).getRelationArticle().getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 10, 11, 0, 0, 0), rankingUserList.get(18).getRelationArticle().getUpdatedAt());
        assertEquals("title43", rankingUserList.get(18).getRelationArticle().getTitle());
        assertEquals(2, rankingUserList.get(18).getRelationArticle().getStateFlag());
        assertEquals(4, rankingUserList.get(18).getRelationArticle().getTags().get(0).getTagId());
        assertEquals("php", rankingUserList.get(18).getRelationArticle().getTags().get(0).getTagName());
        assertEquals(5, rankingUserList.get(18).getRelationArticle().getTags().get(1).getTagId());
        assertEquals("go", rankingUserList.get(18).getRelationArticle().getTags().get(1).getTagName());
        assertEquals(4, rankingUserList.get(18).getRelationArticle().getQiitaRecommendPoint());
        assertEquals(0, rankingUserList.get(18).getRelationArticle().getRegisteredMyArticleCount());
        assertEquals(1, rankingUserList.get(18).getRelationArticle().getFeedbackCount());
        assertEquals(19, rankingUserList.get(18).getRank());

        assertNotNull(rankingUserList.get(19).getRelationArticle());
        assertNull(rankingUserList.get(20).getRelationArticle());

        //////////最後(31~35位)
        assertEquals(16, rankingUserList.get(30).getUser().getUserId());
        assertEquals("user16", rankingUserList.get(30).getUser().getDisplayName());
        assertEquals("photo16", rankingUserList.get(30).getUser().getPhotoUrl());
        assertEquals(3, rankingUserList.get(30).getUser().getFeedbackCount());
        assertEquals(3, rankingUserList.get(30).getUser().getPostedArticleCount());
        assertEquals(1, rankingUserList.get(30).getUser().getQiitaRecommendedAllCount());
        assertEquals(31, rankingUserList.get(30).getRank());

        assertEquals(17, rankingUserList.get(31).getUser().getUserId());
        assertEquals("user17", rankingUserList.get(31).getUser().getDisplayName());
        assertEquals("photo17", rankingUserList.get(31).getUser().getPhotoUrl());
        assertEquals(2, rankingUserList.get(31).getUser().getFeedbackCount());
        assertEquals(3, rankingUserList.get(31).getUser().getPostedArticleCount());
        assertEquals(1, rankingUserList.get(31).getUser().getQiitaRecommendedAllCount());
        assertEquals(31, rankingUserList.get(31).getRank());

        assertEquals(18, rankingUserList.get(32).getUser().getUserId());
        assertEquals("user18", rankingUserList.get(32).getUser().getDisplayName());
        assertEquals("photo18", rankingUserList.get(32).getUser().getPhotoUrl());
        assertEquals(2, rankingUserList.get(32).getUser().getFeedbackCount());
        assertEquals(2, rankingUserList.get(32).getUser().getPostedArticleCount());
        assertEquals(1, rankingUserList.get(32).getUser().getQiitaRecommendedAllCount());
        assertEquals(31, rankingUserList.get(32).getRank());

        assertEquals(26, rankingUserList.get(33).getUser().getUserId());
        assertEquals("user26", rankingUserList.get(33).getUser().getDisplayName());
        assertEquals("photo26", rankingUserList.get(33).getUser().getPhotoUrl());
        assertEquals(3, rankingUserList.get(33).getUser().getFeedbackCount());
        assertEquals(4, rankingUserList.get(33).getUser().getPostedArticleCount());
        assertEquals(1, rankingUserList.get(33).getUser().getQiitaRecommendedAllCount());
        assertEquals(31, rankingUserList.get(33).getRank());

        assertEquals(27, rankingUserList.get(34).getUser().getUserId());
        assertEquals("user27", rankingUserList.get(34).getUser().getDisplayName());
        assertEquals("photo27", rankingUserList.get(34).getUser().getPhotoUrl());
        assertEquals(2, rankingUserList.get(34).getUser().getFeedbackCount());
        assertEquals(3, rankingUserList.get(34).getUser().getPostedArticleCount());
        assertEquals(1, rankingUserList.get(34).getUser().getQiitaRecommendedAllCount());
        assertEquals(31, rankingUserList.get(34).getRank());
    }

    @Test
    void fetchQiitaCountRank_none_exist_rankingUser() {
        List<RankingUser> rankingUserList = rankingUserService.fetchQiitaCountRank();
        assertTrue(rankingUserList.isEmpty());
    }
}