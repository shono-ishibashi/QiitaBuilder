package com.qiitabuilder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiitabuilder.domain.MyArticle;
import com.qiitabuilder.domain.Recommend;
import com.qiitabuilder.service.MyArticleService;
import com.qiitabuilder.service.RecommendService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecommendControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private RecommendService recommendService;

    private MockMvc mockMvc;
    private static final String BASE_URL = "/recommend";

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

    // fetchRecommend()
    @Test
    @WithMockUser
    void fetchRecommend正常系() throws Exception {
        Recommend res = Recommend.builder()
                .recommendId(1)
                .articleId(1)
                .postedUserId(1)
                .recommendUserId(1)
                .build();

        doReturn(res).when(recommendService).fetchRecommend(1);

        this.mockMvc
                .perform(get(BASE_URL + "?articleId=1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void fetchRecommend異常系_articleIdがNullの場合() throws Exception {
        this.mockMvc
                .perform(get(BASE_URL + "?articleId="))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void fetchRecommend異常系_articleIdが文字列の場合() throws Exception {
        this.mockMvc
                .perform(get(BASE_URL + "?articleId=test"))
                .andExpect(status().isBadRequest());
    }

    // postRecommend()
    @Test
    @WithMockUser
    void postRecommend正常系()throws Exception {
        // リクエストボディーとレスポンスボディー作成
        Recommend req = Recommend.builder()
                .articleId(1)
                .build();
        Recommend res = Recommend.builder()
                .recommendId(1)
                .articleId(1)
                .postedUserId(1)
                .recommendUserId(1)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String reqBody = mapper.writeValueAsString(req);
        String resBody = mapper.writeValueAsString(res);

        // Serviceの挙動を指定
        doReturn(res).when(recommendService).postRecommend(req);

        mockMvc
                .perform(post(BASE_URL)
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(resBody));
    }
    @Test
    @WithMockUser
    void postRecommend異常系_ボディーがNullの場合() throws Exception {
        String reqBody = "{}";

        mockMvc
                .perform(post(BASE_URL)
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void postRecommend異常系_articleIdがNullの場合() throws Exception {
        Recommend req = Recommend.builder()
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
    void postRecommend異常系_articleIdが文字列の場合() throws Exception {
        String reqBody = "{\"articleId\":\"test\"}";

        mockMvc
                .perform(post(BASE_URL)
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // deleteRecommend()
    @Test
    @WithMockUser
    void deleteRecommend正常系() throws Exception {
        // Serviceの挙動を指定
        doNothing().when(recommendService).deleteRecommend(1);

        mockMvc
                .perform(delete(BASE_URL + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteRecommend異常系_myArticleIdが文字列の場合() throws Exception {
        // Serviceの挙動を指定
        doNothing().when(recommendService).deleteRecommend(1);

        mockMvc
                .perform(delete(BASE_URL + "/test"))
                .andExpect(status().isBadRequest());
    }
}