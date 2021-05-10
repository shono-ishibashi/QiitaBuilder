package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.RankingUser;
import com.qiitabuilder.domain.Recommend;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RecommendMapperTest {
    @Autowired
    private RecommendMapper recommendMapper;

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
                "   article_version  int          not null default 1,\n" +
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

    @Test
    void getQiitaRecommendedRank() {
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

        List<RankingUser> rankingUserList = recommendMapper.getQiitaRecommendedRank();
        assertEquals(35, rankingUserList.size());

        //////////最初(1~5位)
        assertEquals(11, rankingUserList.get(0).getUser().getUserId());
        assertEquals("user", rankingUserList.get(0).getUser().getDisplayName());
        assertEquals("user_photo", rankingUserList.get(0).getUser().getPhotoUrl());
        assertEquals(8, rankingUserList.get(0).getUser().getFeedbackCount());
        assertEquals(13, rankingUserList.get(0).getUser().getPostedArticleCount());
        assertEquals(15, rankingUserList.get(0).getUser().getQiitaRecommendedAllCount());

        assertEquals(1, rankingUserList.get(1).getUser().getUserId());
        assertEquals("a", rankingUserList.get(1).getUser().getDisplayName());
        assertEquals("a", rankingUserList.get(1).getUser().getPhotoUrl());
        assertEquals(8, rankingUserList.get(1).getUser().getFeedbackCount());
        assertEquals(12, rankingUserList.get(1).getUser().getPostedArticleCount());
        assertEquals(14, rankingUserList.get(1).getUser().getQiitaRecommendedAllCount());

        assertEquals(22, rankingUserList.get(2).getUser().getUserId());
        assertEquals("user22", rankingUserList.get(2).getUser().getDisplayName());
        assertEquals("photo22", rankingUserList.get(2).getUser().getPhotoUrl());
        assertEquals(0, rankingUserList.get(2).getUser().getFeedbackCount());
        assertEquals(9, rankingUserList.get(2).getUser().getPostedArticleCount());
        assertEquals(13, rankingUserList.get(2).getUser().getQiitaRecommendedAllCount());

        assertEquals(3, rankingUserList.get(3).getUser().getUserId());
        assertEquals("c", rankingUserList.get(3).getUser().getDisplayName());
        assertEquals("c", rankingUserList.get(3).getUser().getPhotoUrl());
        assertEquals(7, rankingUserList.get(3).getUser().getFeedbackCount());
        assertEquals(7, rankingUserList.get(3).getUser().getPostedArticleCount());
        assertEquals(12, rankingUserList.get(3).getUser().getQiitaRecommendedAllCount());

        assertEquals(31, rankingUserList.get(4).getUser().getUserId());
        assertEquals("user31", rankingUserList.get(4).getUser().getDisplayName());
        assertEquals("photo31", rankingUserList.get(4).getUser().getPhotoUrl());
        assertEquals(7, rankingUserList.get(4).getUser().getFeedbackCount());
        assertEquals(15, rankingUserList.get(4).getUser().getPostedArticleCount());
        assertEquals(11, rankingUserList.get(4).getUser().getQiitaRecommendedAllCount());

        //////////中間(15~19位)
        assertEquals(7, rankingUserList.get(14).getUser().getUserId());
        assertEquals("しんじ", rankingUserList.get(14).getUser().getDisplayName());
        assertEquals("ggg", rankingUserList.get(14).getUser().getPhotoUrl());
        assertEquals(3, rankingUserList.get(14).getUser().getFeedbackCount());
        assertEquals(2, rankingUserList.get(14).getUser().getPostedArticleCount());
        assertEquals(6, rankingUserList.get(14).getUser().getQiitaRecommendedAllCount());

        assertEquals(34, rankingUserList.get(15).getUser().getUserId());
        assertEquals("user34", rankingUserList.get(15).getUser().getDisplayName());
        assertEquals("photo34", rankingUserList.get(15).getUser().getPhotoUrl());
        assertEquals(4, rankingUserList.get(15).getUser().getFeedbackCount());
        assertEquals(7, rankingUserList.get(15).getUser().getPostedArticleCount());
        assertEquals(6, rankingUserList.get(15).getUser().getQiitaRecommendedAllCount());

        assertEquals(10, rankingUserList.get(16).getUser().getUserId());
        assertEquals("そうし", rankingUserList.get(16).getUser().getDisplayName());
        assertEquals("uuu", rankingUserList.get(16).getUser().getPhotoUrl());
        assertEquals(15, rankingUserList.get(16).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(16).getUser().getPostedArticleCount());
        assertEquals(5, rankingUserList.get(16).getUser().getQiitaRecommendedAllCount());

        assertEquals(14, rankingUserList.get(17).getUser().getUserId());
        assertEquals("user14", rankingUserList.get(17).getUser().getDisplayName());
        assertEquals("photo14", rankingUserList.get(17).getUser().getPhotoUrl());
        assertEquals(4, rankingUserList.get(17).getUser().getFeedbackCount());
        assertEquals(6, rankingUserList.get(17).getUser().getPostedArticleCount());
        assertEquals(5, rankingUserList.get(17).getUser().getQiitaRecommendedAllCount());

        assertEquals(8, rankingUserList.get(18).getUser().getUserId());
        assertEquals("しょーの", rankingUserList.get(18).getUser().getDisplayName());
        assertEquals("nnn", rankingUserList.get(18).getUser().getPhotoUrl());
        assertEquals(2, rankingUserList.get(18).getUser().getFeedbackCount());
        assertEquals(2, rankingUserList.get(18).getUser().getPostedArticleCount());
        assertEquals(4, rankingUserList.get(18).getUser().getQiitaRecommendedAllCount());

        //////////最後(31~35位)
        assertEquals(16, rankingUserList.get(30).getUser().getUserId());
        assertEquals("user16", rankingUserList.get(30).getUser().getDisplayName());
        assertEquals("photo16", rankingUserList.get(30).getUser().getPhotoUrl());
        assertEquals(3, rankingUserList.get(30).getUser().getFeedbackCount());
        assertEquals(3, rankingUserList.get(30).getUser().getPostedArticleCount());
        assertEquals(1, rankingUserList.get(30).getUser().getQiitaRecommendedAllCount());

        assertEquals(17, rankingUserList.get(31).getUser().getUserId());
        assertEquals("user17", rankingUserList.get(31).getUser().getDisplayName());
        assertEquals("photo17", rankingUserList.get(31).getUser().getPhotoUrl());
        assertEquals(2, rankingUserList.get(31).getUser().getFeedbackCount());
        assertEquals(3, rankingUserList.get(31).getUser().getPostedArticleCount());
        assertEquals(1, rankingUserList.get(31).getUser().getQiitaRecommendedAllCount());

        assertEquals(18, rankingUserList.get(32).getUser().getUserId());
        assertEquals("user18", rankingUserList.get(32).getUser().getDisplayName());
        assertEquals("photo18", rankingUserList.get(32).getUser().getPhotoUrl());
        assertEquals(2, rankingUserList.get(32).getUser().getFeedbackCount());
        assertEquals(2, rankingUserList.get(32).getUser().getPostedArticleCount());
        assertEquals(1, rankingUserList.get(32).getUser().getQiitaRecommendedAllCount());

        assertEquals(26, rankingUserList.get(33).getUser().getUserId());
        assertEquals("user26", rankingUserList.get(33).getUser().getDisplayName());
        assertEquals("photo26", rankingUserList.get(33).getUser().getPhotoUrl());
        assertEquals(3, rankingUserList.get(33).getUser().getFeedbackCount());
        assertEquals(4, rankingUserList.get(33).getUser().getPostedArticleCount());
        assertEquals(1, rankingUserList.get(33).getUser().getQiitaRecommendedAllCount());

        assertEquals(27, rankingUserList.get(34).getUser().getUserId());
        assertEquals("user27", rankingUserList.get(34).getUser().getDisplayName());
        assertEquals("photo27", rankingUserList.get(34).getUser().getPhotoUrl());
        assertEquals(2, rankingUserList.get(34).getUser().getFeedbackCount());
        assertEquals(3, rankingUserList.get(34).getUser().getPostedArticleCount());
        assertEquals(1, rankingUserList.get(34).getUser().getQiitaRecommendedAllCount());
    }

//  rankItemId =
//  1 : FB投稿数Rank
//  2 : 記事投稿数Rank, Qiita推薦累計数Rank
    @Test
    void getMostRecommendedArticleId_FBRank() {
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
        List<Integer> mostRecommendedArticleId;

        ///////Qiita推薦された記事がある場合
        //check項目
        //Qiita推薦数同率の場合が考慮されているか
        mostRecommendedArticleId = recommendMapper.getMostRecommendedArticleId(20, 1);
        assertEquals(36, mostRecommendedArticleId.get(0));

        ///////Qiita推薦された記事が無い場合
        //check項目
        //FBのdelete_flag=0が考慮されているか
        //articleのstate_flagが考慮されているか
        List<String> sqlArr = new ArrayList<>();
        sqlArr.add("INSERT INTO feedbacks (article_id, user_id, created_at, updated_at, content, delete_flag) VALUES (195, 14, '2020-11-03 00:00:00', '2020-11-04 00:00:00', 'feedback content201', 1);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 1, 195);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 2, 195);");
        sqlArr.forEach(sql -> jdbcTemplate.execute(sql));

        mostRecommendedArticleId = recommendMapper.getMostRecommendedArticleId(14, 1);
        assertNull(mostRecommendedArticleId.get(0));
    }

    @Test
    void getMostRecommendedArticleId_PostedArticleCountRank_QiitaRecommendedCountRank() {
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

        List<Integer> mostRecommendedArticleId;

        ///////Qiita推薦された記事がある場合
        //check項目
        //Qiita推薦数同率の場合が考慮されているか
        mostRecommendedArticleId = recommendMapper.getMostRecommendedArticleId(15, 2);
        assertEquals(86, mostRecommendedArticleId.get(0));
        List<Integer> artIdList = jdbcTemplate.queryForList(
                "SELECT article_id " +
                        "FROM qiita_recommends " +
                        "WHERE article_id IN ( " +
                        "SELECT art.article_id " +
                        "FROM articles AS art " +
                        "WHERE art.user_id = 15 " +
                        "AND (state_flag = 1 OR state_flag = 2) " +
                        ") " +
                        "GROUP BY article_id " +
                        "ORDER BY article_id;", Integer.class);
        assertEquals(83, artIdList.get(0));
        assertEquals(84, artIdList.get(1));
        assertEquals(85, artIdList.get(2));
        assertEquals(86, artIdList.get(3));
        assertEquals(2, jdbcTemplate.queryForObject("SELECT count(*) FROM qiita_recommends WHERE article_id = 83", Integer.class));
        assertEquals(2, jdbcTemplate.queryForObject("SELECT count(*) FROM qiita_recommends WHERE article_id = 84", Integer.class));
        assertEquals(1, jdbcTemplate.queryForObject("SELECT count(*) FROM qiita_recommends WHERE article_id = 85", Integer.class));
        assertEquals(2, jdbcTemplate.queryForObject("SELECT count(*) FROM qiita_recommends WHERE article_id = 86", Integer.class));

        //check項目
        //articleのstate_flagが考慮されているか
        List<String> sqlArr = new ArrayList<>();
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 1, 195);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 2, 195);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 3, 195);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 4, 195);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 6, 195);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 7, 195);");
        sqlArr.add("DELETE FROM qiita_recommends WHERE article_id = 91;");
        sqlArr.forEach(sql -> jdbcTemplate.execute(sql));

        mostRecommendedArticleId = recommendMapper.getMostRecommendedArticleId(10, 2);
        assertEquals(46, mostRecommendedArticleId.get(0));

        ///////Qiita推薦された記事が無い場合
        mostRecommendedArticleId = recommendMapper.getMostRecommendedArticleId(17 ,2);
        assertNull(mostRecommendedArticleId.get(0));
    }

    @Test
    void getMostRecommendedArticleId_指定rankItemId以外() {
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

        List<Integer> mostRecommendedArticleId;

        ///////Qiita推薦された記事がある場合
        //check項目
        //Qiita推薦数同率の場合が考慮されているか
        mostRecommendedArticleId = recommendMapper.getMostRecommendedArticleId(15, 3);
        assertEquals(86, mostRecommendedArticleId.get(0));
        List<Integer> artIdList = jdbcTemplate.queryForList(
                "SELECT article_id " +
                        "FROM qiita_recommends " +
                        "WHERE article_id IN ( " +
                        "SELECT art.article_id " +
                        "FROM articles AS art " +
                        "WHERE art.user_id = 15 " +
                        "AND (state_flag = 1 OR state_flag = 2) " +
                        ") " +
                        "GROUP BY article_id " +
                        "ORDER BY article_id;", Integer.class);
        assertEquals(83, artIdList.get(0));
        assertEquals(84, artIdList.get(1));
        assertEquals(85, artIdList.get(2));
        assertEquals(86, artIdList.get(3));
        assertEquals(2, jdbcTemplate.queryForObject("SELECT count(*) FROM qiita_recommends WHERE article_id = 83", Integer.class));
        assertEquals(2, jdbcTemplate.queryForObject("SELECT count(*) FROM qiita_recommends WHERE article_id = 84", Integer.class));
        assertEquals(1, jdbcTemplate.queryForObject("SELECT count(*) FROM qiita_recommends WHERE article_id = 85", Integer.class));
        assertEquals(2, jdbcTemplate.queryForObject("SELECT count(*) FROM qiita_recommends WHERE article_id = 86", Integer.class));

        //check項目
        //articleのstate_flagが考慮されているか
        List<String> sqlArr = new ArrayList<>();
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 1, 195);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 2, 195);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 3, 195);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 4, 195);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 6, 195);");
        sqlArr.add("INSERT INTO qiita_recommends (posted_user_id, recommend_user_id, article_id) VALUES (10, 7, 195);");
        sqlArr.add("DELETE FROM qiita_recommends WHERE article_id = 91;");
        sqlArr.forEach(sql -> jdbcTemplate.execute(sql));

        mostRecommendedArticleId = recommendMapper.getMostRecommendedArticleId(10, 4);
        assertEquals(46, mostRecommendedArticleId.get(0));

        ///////Qiita推薦された記事が無い場合
        mostRecommendedArticleId = recommendMapper.getMostRecommendedArticleId(17 ,5);
        assertNull(mostRecommendedArticleId.get(0));
    }

    //// findByArticleIdAndRecommendUserId()
    @Test
    void findByArticleIdAndRecommendUserIdのテスト正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 1, 1)");

        Recommend recommend = recommendMapper.findByArticleIdAndRecommendUserId(1, 1);
        assertEquals(1, recommend.getRecommendId());
        assertEquals(1, recommend.getArticleId());
        assertEquals(1, recommend.getPostedUserId());
        assertEquals(1, recommend.getRecommendUserId());
    }

    @Test
    void findByArticleIdAndRecommendUserIdのテスト異常系_MyArticleが存在しない場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 1, 1)");

        Recommend recommend = recommendMapper.findByArticleIdAndRecommendUserId(2, 1);
        assertNull(recommend);
    }

    @Test
    void findByArticleIdAndRecommendUserIdのテスト異常系_引数がNullの場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 1, 1)");

        Recommend recommend = recommendMapper.findByArticleIdAndRecommendUserId(null, null);
        assertNull(recommend);
    }

    //// load()
    @Test
    void loadのテスト正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 1, 1)");

        Recommend recommend = recommendMapper.load(1);
        assertEquals(1, recommend.getRecommendId());
        assertEquals(1, recommend.getArticleId());
        assertEquals(1, recommend.getPostedUserId());
        assertEquals(1, recommend.getRecommendUserId());
    }

    //// insert()
    @Test
    void insertのテスト正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        Recommend recommend = Recommend.builder()
                .articleId(1)
                .postedUserId(1)
                .recommendUserId(1)
                .build();

        recommendMapper.insert(recommend);

        // check
        String sql = "SELECT * FROM qiita_recommends ORDER BY recommend_id";

        SqlParameterSource param = new EmptySqlParameterSource();

        List<Map<String, Object>> resultRecommends = namedParameterJdbcTemplate.queryForList(sql, param);

        assertEquals(1, resultRecommends.get(0).get("recommend_id"));
        assertEquals(1, resultRecommends.get(0).get("article_id"));
        assertEquals(1, resultRecommends.get(0).get("posted_user_id"));
        assertEquals(1, resultRecommends.get(0).get("recommend_user_id"));
    }

    @Test
    void insertのテスト異常系_外部制約例外() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key

        Recommend recommend = Recommend.builder()
                .articleId(1)
                .postedUserId(1)
                .recommendUserId(1)
                .build();

        // check
        Exception exception = assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            recommendMapper.insert(recommend);
        });

        String expectedMessage = "Cannot add or update a child row: a foreign key constraint fails";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void insertのテスト異常系_引数がNullの場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key

        Recommend recommend = new Recommend();

        // check
        Exception exception = assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            recommendMapper.insert(recommend);
        });
        String expectedMessage = "Column 'article_id' cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    //// getAutoIncrementKey()
    @Test
    void getAutoIncrementKeyのテスト正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 1, 1)");
        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(2, 1, 1)");

        Integer actualResult2 = recommendMapper.getAutoIncrementKey();
        assertEquals(2, actualResult2);
    }


    //// delete()
    @Test
    void deleteのテスト正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 1, 1)");

        boolean actualResult = recommendMapper.delete(1);
        assertTrue(actualResult);
    }

    @Test
    void deleteのテスト異常系_MyArticleが存在しない場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 1, 1)");

        boolean actualResult = recommendMapper.delete(2);
        assertFalse(actualResult);
    }

    @Test
    void deleteのテスト異常系_引数がNullの場合() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事1
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key 記事2

        jdbcTemplate.execute("INSERT INTO qiita_recommends(article_id, posted_user_id, recommend_user_id) VALUES(1, 1, 1)");

        // check
        boolean actualResult = recommendMapper.delete(null);
        assertFalse(actualResult);
    }
}