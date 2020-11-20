package com.qiitabuilder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.RankingUser;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.service.RankingUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RankingUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RankingUserService rankingUserService;

    @Test
    void 非ログイン時() throws Exception {
        mockMvc.perform(get("/user/ranking"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void fetchFBCountRank() throws Exception {
        List<RankingUser> rankingUserList = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {
            RankingUser rankingUser = new RankingUser();
            User user = new User();
            user.setUserId(i);
            user.setDisplayName("user" + i);
            user.setPhotoUrl("photo" + i);
            user.setFeedbackCount(26 - i);
            user.setPostedArticleCount(3);
            user.setQiitaRecommendedAllCount(2);
            rankingUser.setUser(user);
            Article article = new Article();
            rankingUser.setRelationArticle(article);
            rankingUser.setRank(i);
            rankingUserList.add(rankingUser);
        }

        ObjectMapper mapper = new ObjectMapper();
        String resBody = mapper.writeValueAsString(rankingUserList);

        doReturn(rankingUserList).when(rankingUserService).fetchFBCountRank();

        mockMvc.perform(MockMvcRequestBuilders.get("/user/ranking/FBCount"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(resBody));
    }

    @Test
    @WithMockUser
    void fetchArticleCountRank() throws Exception {
        List<RankingUser> rankingUserList = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {
            RankingUser rankingUser = new RankingUser();
            User user = new User();
            user.setUserId(i);
            user.setDisplayName("user" + i);
            user.setPhotoUrl("photo" + i);
            user.setFeedbackCount(1);
            user.setPostedArticleCount(26 - i);
            user.setQiitaRecommendedAllCount(2);
            rankingUser.setUser(user);
            Article article = new Article();
            rankingUser.setRelationArticle(article);
            rankingUser.setRank(i);
            rankingUserList.add(rankingUser);
        }

        ObjectMapper mapper = new ObjectMapper();
        String resBody = mapper.writeValueAsString(rankingUserList);

        doReturn(rankingUserList).when(rankingUserService).fetchArticleCountRank();

        mockMvc.perform(MockMvcRequestBuilders.get("/user/ranking/articleCount"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(resBody));
    }

    @Test
    @WithMockUser
    void fetchQiitaCountRank() throws Exception {
        List<RankingUser> rankingUserList = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {
            RankingUser rankingUser = new RankingUser();
            User user = new User();
            user.setUserId(i);
            user.setDisplayName("user" + i);
            user.setPhotoUrl("photo" + i);
            user.setFeedbackCount(1);
            user.setPostedArticleCount(3);
            user.setQiitaRecommendedAllCount(26 - i);
            rankingUser.setUser(user);
            Article article = new Article();
            rankingUser.setRelationArticle(article);
            rankingUser.setRank(i);
            rankingUserList.add(rankingUser);
        }

        ObjectMapper mapper = new ObjectMapper();
        String resBody = mapper.writeValueAsString(rankingUserList);

        doReturn(rankingUserList).when(rankingUserService).fetchQiitaCountRank();

        mockMvc.perform(MockMvcRequestBuilders.get("/user/ranking/qiitaRecommendedCount"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(resBody));
    }
}