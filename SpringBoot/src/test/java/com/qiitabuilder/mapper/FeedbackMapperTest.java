package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.domain.RankingUser;
import com.qiitabuilder.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedbackMapperTest {

    @Autowired
    private FeedbackMapper feedbackMapper;

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

    //// insert()
    @Test
    void insertのテスト正常系() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        LocalDateTime ldtnow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // ns以下の時間単位を切り落とし
        Timestamp tsNow = Timestamp.valueOf(ldtnow);

        Feedback feedback = Feedback.builder()
                .articleId(1)
                .postedUser(User.builder().userId(1).build())
                .createdAt(ldtnow)
                .content("sample")
                .deleteFlag(0)
                .build();

        feedbackMapper.insert(feedback);

        // check
        String sql = "SELECT * FROM feedbacks ORDER BY feedback_id";

        SqlParameterSource param = new EmptySqlParameterSource();

        List<Map<String, Object>> resultRecommends = namedParameterJdbcTemplate.queryForList(sql, param);

        assertEquals(1, resultRecommends.get(0).get("feedback_id"));
        assertEquals(1, resultRecommends.get(0).get("article_id"));
        assertEquals(1, resultRecommends.get(0).get("user_id"));
        assertEquals(tsNow, resultRecommends.get(0).get("created_at"));
        assertEquals("sample", resultRecommends.get(0).get("content"));
        assertEquals(0, resultRecommends.get(0).get("delete_flag"));
    }

    @Test
    void insertのテスト異常系_外部制約例外() {
        // insert
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key

        LocalDateTime ldtnow = LocalDateTime.now();

        Feedback feedback = Feedback.builder()
                .articleId(1)
                .postedUser(User.builder().userId(1).build())
                .createdAt(ldtnow)
                .content("sample")
                .deleteFlag(0)
                .build();

        // check
        Exception exception = assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            feedbackMapper.insert(feedback);
        });

        String expectedMessage = "Cannot add or update a child row: a foreign key constraint fails";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    //// update()
    @Test
    void updateのテスト正常系() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(2);"); // Foreign key

        String insertSql = "INSERT INTO feedbacks(article_id, user_id, created_at, content, delete_flag) VALUES(:articleId, :userId, :createdAt, :content, :deleteFlag);";
        LocalDateTime ldtnow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // ns以下の時間単位を切り落とし

        Feedback setUpFeedback = Feedback.builder()
                .articleId(1)
                .postedUser(User.builder().userId(1).build())
                .createdAt(ldtnow)
                .content("sample")
                .deleteFlag(0)
                .build();
        SqlParameterSource setUpParam = new MapSqlParameterSource()
                .addValue("articleId", setUpFeedback.getArticleId())
                .addValue("userId", setUpFeedback.getPostedUser().getUserId())
                .addValue("createdAt", setUpFeedback.getCreatedAt())
                .addValue("content", setUpFeedback.getContent())
                .addValue("deleteFlag", setUpFeedback.getDeleteFlag());
        namedParameterJdbcTemplate.update(insertSql, setUpParam);

        // target method
        Feedback updateFeedback = Feedback.builder()
                .feedbackId(1)
                .articleId(2)
                .postedUser(User.builder().userId(2).build())
                .updatedAt(ldtnow.plusHours(3))
                .content("changed")
                .deleteFlag(1)
                .build();
        Integer actualResult = feedbackMapper.update(updateFeedback);
        // check
        assertEquals(1, actualResult);

        String sql = "SELECT * FROM feedbacks ORDER BY feedback_id";

        SqlParameterSource param = new EmptySqlParameterSource();

        List<Map<String, Object>> resultFeedbacks = namedParameterJdbcTemplate.queryForList(sql, param);

        assertEquals(1, resultFeedbacks.get(0).get("feedback_id"));
        assertEquals(2, resultFeedbacks.get(0).get("article_id"));
        assertEquals(2, resultFeedbacks.get(0).get("user_id"));
        assertEquals(Timestamp.valueOf(ldtnow), resultFeedbacks.get(0).get("created_at"));
        assertEquals(Timestamp.valueOf(ldtnow.plusHours(3)), resultFeedbacks.get(0).get("updated_at"));
        assertEquals("changed", resultFeedbacks.get(0).get("content"));
        assertEquals(1, resultFeedbacks.get(0).get("delete_flag"));
    }

    @Test
    void updateのテスト異常系_feedbackIdが存在しない場合() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        LocalDateTime ldtnow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // ns以下の時間単位を切り落とし

        // target method
        Feedback updateFeedback = Feedback.builder()
                .feedbackId(1)
                .articleId(1)
                .postedUser(User.builder().userId(1).build())
                .updatedAt(ldtnow)
                .content("changed")
                .deleteFlag(1)
                .build();
        Integer actualResult = feedbackMapper.update(updateFeedback);
        // check
        assertEquals(0, actualResult);
    }

    @Test
    void updateのテスト異常系_引数がNullの場合() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        LocalDateTime ldtnow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // ns以下の時間単位を切り落とし

        // target method
        Feedback updateFeedback = Feedback.builder()
                .build();
        Integer actualResult = feedbackMapper.update(updateFeedback);
        // check
        assertEquals(0, actualResult);
    }

    @Test
    void getFBRank() {
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

        List<RankingUser> rankingUserList = feedbackMapper.getFBRank();
        assertEquals(35, rankingUserList.size());

        //////////最初(1~5位)
        assertEquals(10, rankingUserList.get(0).getUser().getUserId());
        assertEquals("そうし", rankingUserList.get(0).getUser().getDisplayName());
        assertEquals("uuu", rankingUserList.get(0).getUser().getPhotoUrl());
        assertEquals(15, rankingUserList.get(0).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(0).getUser().getPostedArticleCount());
        assertEquals(5, rankingUserList.get(0).getUser().getQiitaRecommendedAllCount());

        assertEquals(20, rankingUserList.get(1).getUser().getUserId());
        assertEquals("user20", rankingUserList.get(1).getUser().getDisplayName());
        assertEquals("photo20", rankingUserList.get(1).getUser().getPhotoUrl());
        assertEquals(14, rankingUserList.get(1).getUser().getFeedbackCount());
        assertEquals(0, rankingUserList.get(1).getUser().getPostedArticleCount());
        assertEquals(0, rankingUserList.get(1).getUser().getQiitaRecommendedAllCount());

        assertEquals(30, rankingUserList.get(2).getUser().getUserId());
        assertEquals("user30", rankingUserList.get(2).getUser().getDisplayName());
        assertEquals("photo30", rankingUserList.get(2).getUser().getPhotoUrl());
        assertEquals(13, rankingUserList.get(2).getUser().getFeedbackCount());
        assertEquals(0, rankingUserList.get(2).getUser().getPostedArticleCount());
        assertEquals(0, rankingUserList.get(2).getUser().getQiitaRecommendedAllCount());

        assertEquals(40, rankingUserList.get(3).getUser().getUserId());
        assertEquals("user40", rankingUserList.get(3).getUser().getDisplayName());
        assertEquals("photo40", rankingUserList.get(3).getUser().getPhotoUrl());
        assertEquals(12, rankingUserList.get(3).getUser().getFeedbackCount());
        assertEquals(0, rankingUserList.get(3).getUser().getPostedArticleCount());
        assertEquals(0, rankingUserList.get(3).getUser().getQiitaRecommendedAllCount());

        assertEquals(5, rankingUserList.get(4).getUser().getUserId());
        assertEquals("test", rankingUserList.get(4).getUser().getDisplayName());
        assertEquals("test_photo", rankingUserList.get(4).getUser().getPhotoUrl());
        assertEquals(11, rankingUserList.get(4).getUser().getFeedbackCount());
        assertEquals(4, rankingUserList.get(4).getUser().getPostedArticleCount());
        assertEquals(9, rankingUserList.get(4).getUser().getQiitaRecommendedAllCount());

        //////////中間(15~19位)
        assertEquals(23, rankingUserList.get(14).getUser().getUserId());
        assertEquals("user23", rankingUserList.get(14).getUser().getDisplayName());
        assertEquals("photo23", rankingUserList.get(14).getUser().getPhotoUrl());
        assertEquals(6, rankingUserList.get(14).getUser().getFeedbackCount());
        assertEquals(8, rankingUserList.get(14).getUser().getPostedArticleCount());
        assertEquals(8, rankingUserList.get(14).getUser().getQiitaRecommendedAllCount());

        assertEquals(4, rankingUserList.get(15).getUser().getUserId());
        assertEquals("d", rankingUserList.get(15).getUser().getDisplayName());
        assertEquals("d", rankingUserList.get(15).getUser().getPhotoUrl());
        assertEquals(5, rankingUserList.get(15).getUser().getFeedbackCount());
        assertEquals(5, rankingUserList.get(15).getUser().getPostedArticleCount());
        assertEquals(3, rankingUserList.get(15).getUser().getQiitaRecommendedAllCount());
        
        assertEquals(33, rankingUserList.get(16).getUser().getUserId());
        assertEquals("user33", rankingUserList.get(16).getUser().getDisplayName());
        assertEquals("photo33", rankingUserList.get(16).getUser().getPhotoUrl());
        assertEquals(5, rankingUserList.get(16).getUser().getFeedbackCount());
        assertEquals(8, rankingUserList.get(16).getUser().getPostedArticleCount());
        assertEquals(2, rankingUserList.get(16).getUser().getQiitaRecommendedAllCount());
        
        assertEquals(14, rankingUserList.get(17).getUser().getUserId());
        assertEquals("user14", rankingUserList.get(17).getUser().getDisplayName());
        assertEquals("photo14", rankingUserList.get(17).getUser().getPhotoUrl());
        assertEquals(4, rankingUserList.get(17).getUser().getFeedbackCount());
        assertEquals(6, rankingUserList.get(17).getUser().getPostedArticleCount());
        assertEquals(5, rankingUserList.get(17).getUser().getQiitaRecommendedAllCount());

        assertEquals(24, rankingUserList.get(18).getUser().getUserId());
        assertEquals("user24", rankingUserList.get(18).getUser().getDisplayName());
        assertEquals("photo24", rankingUserList.get(18).getUser().getPhotoUrl());
        assertEquals(4, rankingUserList.get(18).getUser().getFeedbackCount());
        assertEquals(6, rankingUserList.get(18).getUser().getPostedArticleCount());
        assertEquals(7, rankingUserList.get(18).getUser().getQiitaRecommendedAllCount());

        //////////最後(31~35位)
        assertEquals(9, rankingUserList.get(30).getUser().getUserId());
        assertEquals("ゆみ", rankingUserList.get(30).getUser().getDisplayName());
        assertEquals("rrr", rankingUserList.get(30).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(30).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(30).getUser().getPostedArticleCount());
        assertEquals(4, rankingUserList.get(30).getUser().getQiitaRecommendedAllCount());

        assertEquals(19, rankingUserList.get(31).getUser().getUserId());
        assertEquals("user19", rankingUserList.get(31).getUser().getDisplayName());
        assertEquals("photo19", rankingUserList.get(31).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(31).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(31).getUser().getPostedArticleCount());
        assertEquals(3, rankingUserList.get(31).getUser().getQiitaRecommendedAllCount());

        assertEquals(28, rankingUserList.get(32).getUser().getUserId());
        assertEquals("user28", rankingUserList.get(32).getUser().getDisplayName());
        assertEquals("photo28", rankingUserList.get(32).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(32).getUser().getFeedbackCount());
        assertEquals(2, rankingUserList.get(32).getUser().getPostedArticleCount());
        assertEquals(2, rankingUserList.get(32).getUser().getQiitaRecommendedAllCount());

        assertEquals(29, rankingUserList.get(33).getUser().getUserId());
        assertEquals("user29", rankingUserList.get(33).getUser().getDisplayName());
        assertEquals("photo29", rankingUserList.get(33).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(33).getUser().getFeedbackCount());
        assertEquals(1, rankingUserList.get(33).getUser().getPostedArticleCount());
        assertEquals(2, rankingUserList.get(33).getUser().getQiitaRecommendedAllCount());

        assertEquals(38, rankingUserList.get(34).getUser().getUserId());
        assertEquals("user38", rankingUserList.get(34).getUser().getDisplayName());
        assertEquals("photo38", rankingUserList.get(34).getUser().getPhotoUrl());
        assertEquals(1, rankingUserList.get(34).getUser().getFeedbackCount());
        assertEquals(2, rankingUserList.get(34).getUser().getPostedArticleCount());
        assertEquals(3, rankingUserList.get(34).getUser().getQiitaRecommendedAllCount());
    }

    @Test
    void getFBRank_none_exist_rankingUser(){
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
        List<RankingUser> rankingUserList = feedbackMapper.getFBRank();
        assertTrue(rankingUserList.isEmpty());
    }

    @Test
    void getArticleIdListByUserId() {
        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr = CollectionSQL.insertArticles.split("\n", 0);
        String[] feedbackSqlArr = CollectionSQL.insertFeedbacks.split("\n", 0);
        for (String sql : userSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (String sql : articleSqlArr) {
            jdbcTemplate.execute(sql);
        }
        for (String sql : feedbackSqlArr) {
            jdbcTemplate.execute(sql);
        }
        List<Integer> articleIdList = feedbackMapper.getArticleIdListByUserId(5);
        assertEquals(11, articleIdList.size());

        assertEquals(10, articleIdList.get(0));
        assertEquals(20, articleIdList.get(1));
        assertEquals(30, articleIdList.get(2));
        assertEquals(40, articleIdList.get(3));
        assertEquals(50, articleIdList.get(4));
        assertEquals(60, articleIdList.get(5));
        assertEquals(70, articleIdList.get(6));
        assertEquals(80, articleIdList.get(7));
        assertEquals(90, articleIdList.get(8));
        assertEquals(100, articleIdList.get(9));
        assertEquals(110, articleIdList.get(10));
    }

    //// load()
    @Test
    void loadのテスト正常系() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        String insertSql = "INSERT INTO feedbacks(article_id, user_id, created_at, content, delete_flag) VALUES(:articleId, :userId, :createdAt, :content, :deleteFlag);";
        LocalDateTime ldtnow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // ns以下の時間単位を切り落とし

        Feedback setUpFeedback = Feedback.builder()
                .articleId(1)
                .postedUser(User.builder().userId(1).build())
                .createdAt(ldtnow)
                .content("sample")
                .deleteFlag(0)
                .build();
        SqlParameterSource setUpParam = new MapSqlParameterSource()
                .addValue("articleId", setUpFeedback.getArticleId())
                .addValue("userId", setUpFeedback.getPostedUser().getUserId())
                .addValue("createdAt", setUpFeedback.getCreatedAt())
                .addValue("content", setUpFeedback.getContent())
                .addValue("deleteFlag", setUpFeedback.getDeleteFlag());
        namedParameterJdbcTemplate.update(insertSql, setUpParam);

        String updateSql = "UPDATE feedbacks SET updated_at = :updatedAt WHERE feedback_id = :feedbackId";
        SqlParameterSource updateParam = new MapSqlParameterSource()
                .addValue("updatedAt", ldtnow.plusHours(3))
                .addValue("feedbackId", 1);
        namedParameterJdbcTemplate.update(updateSql, updateParam);

        // target method
        Feedback actualResult = feedbackMapper.load(1);

        // check
        assertEquals(1, actualResult.getFeedbackId());
        assertEquals(1, actualResult.getArticleId());
        assertEquals(1, actualResult.getPostedUser().getUserId());
        assertEquals(ldtnow, actualResult.getCreatedAt());
        assertEquals(ldtnow.plusHours(3), actualResult.getUpdatedAt());
        assertEquals("sample", actualResult.getContent());
        assertEquals(0, actualResult.getDeleteFlag());
    }

    @Test
    void loadのテスト異常系_feedbackIdが存在しない場合() {
        // set up
        jdbcTemplate.execute("INSERT INTO users() VALUES();"); // Foreign key
        jdbcTemplate.execute("INSERT INTO articles(user_id) VALUES(1);"); // Foreign key

        // target method
        Feedback actualResult = feedbackMapper.load(1);

        // check
        assertNull(actualResult);
    }

    @Test
    void getFeedbackedArticlesByUserIdのテスト正常系() {
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

        //userId=4のテスト
        List<Article> articles = feedbackMapper.getFeedbackedArticlesByUserId(4);

        LocalDateTime createDateFirst = LocalDateTime.of(2020, 11, 8, 00, 00, 00);
        LocalDateTime updateDateFirst = createDateFirst.plusDays(1);
        LocalDateTime createDateLast = LocalDateTime.of(2020, 10, 1, 00, 00, 00);
        LocalDateTime updateDateLast = createDateLast.plusDays(1);

        //記事件数と最初,最後の記事の取得してきたもの全てテスト
        assertEquals(5, articles.size());
        assertEquals(190, articles.get(0).getArticleId());
        assertEquals("title190", articles.get(0).getTitle());
        assertEquals(createDateFirst, articles.get(0).getCreatedAt());
        assertEquals(updateDateFirst, articles.get(0).getUpdatedAt());
        assertEquals(1, articles.get(0).getStateFlag());
        assertEquals(2, articles.get(0).getTags().get(0).getTagId());
        assertEquals("ruby", articles.get(0).getTags().get(0).getTagName());
        assertEquals(4, articles.get(0).getTags().get(1).getTagId());
        assertEquals("php", articles.get(0).getTags().get(1).getTagName());
        assertEquals(2, articles.get(0).getFeedbackCount());
        assertEquals(1, articles.get(0).getRegisteredMyArticleCount());
        assertEquals(37, articles.get(0).getPostedUser().getUserId());
        assertEquals("user37", articles.get(0).getPostedUser().getDisplayName());
        assertEquals("photo37", articles.get(0).getPostedUser().getPhotoUrl());
        assertEquals(1, articles.get(0).getQiitaRecommendPoint());

        assertEquals(191, articles.get(4).getArticleId());
        assertEquals("title191", articles.get(4).getTitle());
        assertEquals(createDateLast, articles.get(4).getCreatedAt());
        assertEquals(updateDateLast, articles.get(4).getUpdatedAt());
        assertEquals(2, articles.get(4).getStateFlag());
        assertEquals(4, articles.get(4).getTags().get(0).getTagId());
        assertEquals("php", articles.get(4).getTags().get(0).getTagName());
        assertEquals(5, articles.get(4).getTags().get(1).getTagId());
        assertEquals("go", articles.get(4).getTags().get(1).getTagName());
        assertEquals(1,articles.get(4).getFeedbackCount());
        assertEquals(0, articles.get(4).getRegisteredMyArticleCount());
        assertEquals(38, articles.get(4).getPostedUser().getUserId());
        assertEquals("user38", articles.get(4).getPostedUser().getDisplayName());
        assertEquals("photo38", articles.get(4).getPostedUser().getPhotoUrl());
        assertEquals(2, articles.get(4).getQiitaRecommendPoint());
    }
}