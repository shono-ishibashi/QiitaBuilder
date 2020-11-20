package com.qiitabuilder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiitabuilder.domain.MyArticle;
import com.qiitabuilder.service.MyArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MyArticleControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MyArticleService myArticleService;

    private MockMvc mockMvc;
    private static final String BASE_URL = "/my-article";

    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    private void beforeEach() {
        setup();
    }

    // 非ログイン時
    @Test
    void 非ログイン時のテスト() throws Exception {
        this.mockMvc
                .perform(get(BASE_URL))
                .andExpect(status().isUnauthorized());
        this.mockMvc
                .perform(post(BASE_URL))
                .andExpect(status().isUnauthorized());
        this.mockMvc
                .perform(delete(BASE_URL))
                .andExpect(status().isUnauthorized());
    }

    // fetchArticle()
    @Test
    @WithMockUser
    void fetchMyArticle正常系() throws Exception {
        MyArticle myArticle = MyArticle.builder()
                .myArticleId(1)
                .articleId(1)
                .postedUserId(1)
                .registerUserId(1)
                .build();

        doReturn(myArticle).when(myArticleService).fetchMyArticle(myArticle.getArticleId());

        this.mockMvc
                .perform(get(BASE_URL + "?articleId=1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void fetchMyArticle異常系_articleIdがNullの場合() throws Exception {
        this.mockMvc
                .perform(get(BASE_URL + "?articleId="))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void fetchMyArticle異常系_articleIdが文字列の場合() throws Exception {
        this.mockMvc
                .perform(get(BASE_URL + "?articleId=test"))
                .andExpect(status().isBadRequest());
    }

    // postArticle()
    @Test
    @WithMockUser
    void postMyArticle正常系() throws Exception {
        // リクエストボディーとレスポンスボディー作成
        MyArticle req = MyArticle.builder()
                .articleId(1)
                .build();
        MyArticle res = MyArticle.builder()
                .articleId(1)
                .articleId(1)
                .postedUserId(1)
                .registerUserId(1)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String reqBody = mapper.writeValueAsString(req);
        String resBody = mapper.writeValueAsString(res);

        // Serviceの挙動を指定
        doReturn(res).when(myArticleService).postMyArticle(req);

        mockMvc
                .perform(post(BASE_URL)
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(resBody));
    }

    @Test
    @WithMockUser
    void postMyArticle異常系_ボディーがNullの場合() throws Exception {
        String reqBody = "{}";

        mockMvc
                .perform(post(BASE_URL)
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void postMyArticle異常系_articleIdがNullの場合() throws Exception {
        MyArticle req = MyArticle.builder()
                .articleId(null)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String reqBody = mapper.writeValueAsString(req);

        mockMvc
                .perform(post(BASE_URL)
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void postMyArticle異常系_articleIdが文字列の場合() throws Exception {
        String reqBody = "{\"articleId\":\"test\"}";

        mockMvc
                .perform(post(BASE_URL)
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // deleteMyArticle()
    @Test
    @WithMockUser
    void deleteMyArticle正常系() throws Exception {
        // Serviceの挙動を指定
        doNothing().when(myArticleService).deleteMyArticle(1);

        mockMvc
                .perform(delete(BASE_URL + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteMyArticle異常系_myArticleIdが文字列の場合() throws Exception {
        // Serviceの挙動を指定
        doNothing().when(myArticleService).deleteMyArticle(1);

        mockMvc
                .perform(delete(BASE_URL + "/test"))
                .andExpect(status().isBadRequest());
    }
}