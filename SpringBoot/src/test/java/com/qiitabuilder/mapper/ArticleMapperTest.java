package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.form.SearchArticleForm;
import org.apache.ibatis.annotations.Insert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;

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

    private void searchArticlesSqlTemplate(){
        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr = CollectionSQL.insertArticles.split("\n", 0);
        String[] feedbackSqlArr = CollectionSQL.insertFeedbacks.split("\n", 0);
        String[] qiitaRecommendSqlArr = CollectionSQL.insertQiitaRecommends.split("\n", 0);
        String[] myArticlesSqlArr=CollectionSQL.insertMyArticles.split("\n", 0);
        String[] tagsSqlArr=CollectionSQL.insertTags.split("\n", 0);
        String[] articlesTagsRelationsSqlArr = CollectionSQL.insertArticlesTagsRelations.split("\n", 0);

        for(String sql:userSqlArr){
            jdbcTemplate.execute(sql);
        }
        for(String sql:articleSqlArr){
            jdbcTemplate.execute(sql);
        }
        for(String sql:feedbackSqlArr){
            jdbcTemplate.execute(sql);
        }
        for(String sql:qiitaRecommendSqlArr){
            jdbcTemplate.execute(sql);
        }
        for(String sql:myArticlesSqlArr){
            jdbcTemplate.execute(sql);
        }
        for(String sql:tagsSqlArr){
            jdbcTemplate.execute(sql);
        }
        for(String sql:articlesTagsRelationsSqlArr){
            jdbcTemplate.execute(sql);
        }
    }

    @Test
    void searchArticles() {

    }

//    sortNum(0:新着順,1:更新順,2:qiita,3:my記事)
//    preriod(0:週間:,1:月間)
//    toggleSearchWord(0:記事タイトル,1:ユーザー名)

    @Test
    void searchArticlesId正常系_TC1() {
        searchArticlesSqlTemplate();
        SearchArticleForm searchArticleForm=new SearchArticleForm();
        searchArticleForm.setSortNum(0);
        searchArticleForm.setPeriod(0);
        searchArticleForm.setSearchWord("");
        searchArticleForm.setToggleSearchWord(0);
        searchArticleForm.setSearchTag(null);
        searchArticleForm.setPageSize(10);
        searchArticleForm.setCurrentPage(1);

        List<Integer> articles=articleMapper.searchArticlesId(searchArticleForm);

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


    }

    @Test
    void findArticleById() {
    }

    @Test
    void getArticleIdListByUserId() {

        String[] userSqlArr = CollectionSQL.insertUsers.split("\n", 0);
        String[] articleSqlArr = CollectionSQL.insertArticles.split("\n", 0);

        for(String sql : userSqlArr){
            jdbcTemplate.execute(sql);
        }
        for(String sql : articleSqlArr){
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

    @Test
    void getArticleAndFeedback() {
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
}
