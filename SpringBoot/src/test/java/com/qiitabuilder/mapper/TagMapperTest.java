package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TagMapperTest {

    @Autowired
    private TagMapper tagMapper;

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
    void findAll_正常系() {
//        DBにtagsをinsert
        String[] tagsSqlArr = CollectionSQL.insertTags.split("\n", 0);
        for (String sql : tagsSqlArr) {
            jdbcTemplate.execute(sql);
        }
        List<Tag> tags = tagMapper.findAll();
        assertEquals(5, tags.get(0).getTagId());
        assertEquals(1, tags.get(1).getTagId());
        assertEquals(3, tags.get(2).getTagId());
        assertEquals(4, tags.get(3).getTagId());
        assertEquals(2, tags.get(4).getTagId());
        assertEquals("go", tags.get(0).getTagName());
        assertEquals("Java", tags.get(1).getTagName());
        assertEquals("javascript", tags.get(2).getTagName());
        assertEquals("php", tags.get(3).getTagName());
        assertEquals("ruby", tags.get(4).getTagName());
    }

    @Test
    void findAllArticleTag() {

        Arrays.stream(CollectionSQL.insertUsers.split("\n", 0)).forEach(jdbcTemplate::execute);

        Arrays.stream(CollectionSQL.insertArticles.split("\n", 0)).forEach(jdbcTemplate::execute);

        Arrays.stream(CollectionSQL.insertTags.split("\n", 0)).forEach(jdbcTemplate::execute);

        Arrays.stream(CollectionSQL.insertArticlesTagsRelations.split("\n", 0)).forEach(jdbcTemplate::execute);

        List<Integer> tags = tagMapper.findAllArticleTag(1, 1);

        assertEquals(1, tags.get(0));
        assertEquals(5, tags.get(1));
    }

    @Test
    void insertArticleTag() {
        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");

        jdbcTemplate.execute("INSERT INTO articles (user_id, title, content, qiita_article_id, state_flag) VALUES (1,'title','content',null,1)");

        Arrays.stream(CollectionSQL.insertTags.split("\n", 0)).forEach(jdbcTemplate::execute);

        tagMapper.insertArticleTag(1, 1, 1);

        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT * FROM articles_tags_relations order by tags_relation_id");

        assertEquals(1, result.get("tags_relation_id"));
        assertEquals(1, result.get("posted_user_id"));
        assertEquals(1, result.get("tag_id"));
        assertEquals(1, result.get("article_id"));
    }

    @Test
    void deleteArticleTag() {
        jdbcTemplate.execute("INSERT INTO users (user_id) VALUES(1)");

        jdbcTemplate.execute("INSERT INTO articles (user_id, title, content, qiita_article_id, state_flag) VALUES (1,'title','content',null,1)");

        Arrays.stream(CollectionSQL.insertTags.split("\n", 0)).forEach(jdbcTemplate::execute);

        jdbcTemplate.execute("INSERT INTO articles_tags_relations VALUES (null,1,1,1)");

        tagMapper.deleteArticleTag(1, 1, 1);

        List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM articles_tags_relations order by tags_relation_id");

        assertTrue(result.isEmpty());
    }

    @Test
    void insertTag() {
        Tag tag1 = new Tag(null, "test1", null);
        tagMapper.insertTag(tag1);

        Tag tag2 = new Tag(null, "test2", null);
        tagMapper.insertTag(tag2);

        var map1 = jdbcTemplate.queryForMap("SELECT * FROM tags WHERE tag_id = 1");
        var map2 = jdbcTemplate.queryForMap("SELECT * FROM tags WHERE tag_id = 2");

        assertEquals(1, tag1.getTagId());
        assertEquals(2, tag2.getTagId());

        assertEquals("test1",map1.get("tag_name"));
        assertEquals("test2",map2.get("tag_name"));
        assertEquals(1,map1.get("tag_id"));
        assertEquals(2,map2.get("tag_id"));
    }
}
