package com.qiitabuilder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiitabuilder.domain.QiitaConfiguration;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.security.SimpleLoginUser;
import com.qiitabuilder.service.ArticleService;
import com.qiitabuilder.service.QiitaAPIService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class QiitaAPIRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Service層をモックする
    @MockBean
    private QiitaAPIService qiitaAPIService;

    private SimpleLoginUser createLoginUser() {
        User user = new User();
        user.setUserId(1);
        user.setUid("uid");
        user.setPassword("password");

        return new SimpleLoginUser(user);
    }

    @Test
    void checkQiitaAPIAuthentication_正常系() throws Exception {

        QiitaConfiguration qiitaConfiguration = new QiitaConfiguration();
        qiitaConfiguration.setCode("code");
        qiitaConfiguration.setState("state");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(qiitaConfiguration);


        doReturn(true)
                .when(qiitaAPIService)
                .isAuthenticated(any());

        doNothing().when(qiitaAPIService).saveToken();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/qiita/check-qiita-api-authentication/")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())
                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void checkQiitaAPIAuthentication_異常系() throws Exception {

        QiitaConfiguration qiitaConfiguration = new QiitaConfiguration();
        qiitaConfiguration.setCode("code");
        qiitaConfiguration.setState("state");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(qiitaConfiguration);

        doReturn(false)
                .when(qiitaAPIService)
                .isAuthenticated(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/qiita/check-qiita-api-authentication")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void toQiitaAPIAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/qiita/to-qiita-api-authentication")
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void saveArticleToQiita() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/qiita/to-qiita-api-authentication")
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void isLinkedToQiita_true() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/qiita/is-linked-to-qiita")
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
