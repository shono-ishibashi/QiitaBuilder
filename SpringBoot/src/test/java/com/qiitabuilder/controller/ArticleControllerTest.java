package com.qiitabuilder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.security.SimpleLoginUser;
import com.qiitabuilder.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Service層をモックする
    @MockBean
    private ArticleService articleService;

    private SimpleLoginUser createLoginUser(){
        User user = new User();
        user.setUserId(1);
        user.setUid("uid");
        user.setPassword("password");

        return new SimpleLoginUser(user);
    }


    @Test
    void searchArticles() {
    }

    @Test
    void totalPage() {
    }

    @Test
    void fetchArticle() {
    }

    @Test
    void postArticle_正常系() throws Exception {
        List<Tag> tags = new ArrayList<>(Arrays.asList(new Tag(1, "tag1", null), new Tag(2, "tag2", null)));


        Article article = Article.builder()
                .articleId(null)
                .title("title_test")
                .tags(tags)
                .content("content_test")
                .build();

        Article expectBody = Article.builder()
                .articleId(1)
                .title("title_test")
                .tags(tags)
                .content("content_test")
                .build();


        doReturn(expectBody).when(articleService).saveArticle(article);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(article);

        mockMvc.perform(MockMvcRequestBuilders.post("/article/")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(content().json(mapper.writeValueAsString(expectBody)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    void postArticle_異常系_tagが0件の場合() throws Exception {
        List<Tag> tags = new ArrayList<>();


        Article article = Article.builder()
                .articleId(null)
                .title("title_test")
                .tags(tags)
                .content("content_test")
                .build();

        Article expectBody = Article.builder()
                .articleId(1)
                .title("title_test")
                .tags(tags)
                .content("content_test")
                .build();


        doReturn(expectBody).when(articleService).saveArticle(article);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(article);

        mockMvc.perform(MockMvcRequestBuilders.post("/article/")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void editArticle() {
    }

    @Test
    void getFeedbackedArticlesByUserId_userIdがnullの処理() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/article/feedbacked/")
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getFeedbackedArticlesByUserId_正常系() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/article/feedbacked/?userId=1")
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getMyArticlesByUserId_userIdがnullの処理() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/article/my-articles/")
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getMyArticlesByUserId_正常系() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/article/my-articles/?userId=1")
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getArticlesByUserId_userIdがnullの処理() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/article/posted/")
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getArticlesByUserId_正常系() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/article/posted/?userId=1")
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
