package com.qiitabuilder.service;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.domain.User;
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

    @Test
    void searchArticles() {
    }

    @Test
    void getTotalPage() {
    }

    @Test
    void searchCriteriaProcessing() {
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

        assertEquals(1,articlesTagsRelationsResult.size());
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
    void getFeedbackedArticlesByUserId() {
    }

    @Test
    void getMyArticlesByUserId() {
    }
}
