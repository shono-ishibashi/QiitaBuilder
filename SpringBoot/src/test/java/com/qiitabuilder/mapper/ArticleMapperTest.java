package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.domain.RankingUser;
import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.domain.User;
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
import java.util.*;

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

    @Test
    void searchArticles() {

    }

    @Test
    void searchArticlesId() {
    }

    @Test
    void getPostedArticleCountRank() {
        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr = CollectionSQL.insertArticles.split("\n", 0);
        String[] feedbackSqlArr = CollectionSQL.insertFeedbacks.split("\n", 0);
        String[] qiitaRecommendSqlArr = CollectionSQL.insertQiitaRecommends.split("\n", 0);

        for(String sql : userSqlArr){
            jdbcTemplate.execute(sql);
        }
        for(String sql : articleSqlArr){
            jdbcTemplate.execute(sql);
        }
        for(String sql : feedbackSqlArr){
            jdbcTemplate.execute(sql);
        }
        for(String sql : qiitaRecommendSqlArr){
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
    void findArticleById() {
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
        assertEquals(130, articleIdList.get(0));
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
    void getArticleAndFeedback異常系_記事IDが存在しない場合() {
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

        // check
        Article actual = articleMapper.getArticleAndFeedback(2);

        assertNull(actual);
    }

    @Test
    void load正常系() {
        String insertUsers = "INSERT INTO users (user_id) VALUES (1), (2)";
        String insertArticles =
                "INSERT INTO articles ( user_id, created_at, updated_at, title, content, qiita_article_id, state_flag) " +
                        "VALUES(1,NOW(),NOW(),'test title','content title', null, 1)";

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
}
