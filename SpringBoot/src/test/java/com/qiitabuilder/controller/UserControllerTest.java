package com.qiitabuilder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.security.SimpleLoginUser;
import com.qiitabuilder.service.UserService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    private SimpleLoginUser createLoginUser() {
        User user = new User();
        user.setUserId(1);
        user.setUid("uid");
        user.setPassword("password");

        return new SimpleLoginUser(user);
    }

    @Test
    void insertUser() {
    }

    @Test
    void findUserIdByUid_正常系() throws Exception {
        String uid = "a";
        Integer userId = 1;
        doReturn(userId).when(userService).findUserIdByUid(uid);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.get("/userId/?uid=a")
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(content().json(mapper.writeValueAsString(userId)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findUserIdByUid_異常系_引数userIdがnullの処理() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/userId/")
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void findUserIdByUid_異常系_DBに存在しないユーザーUid処理()throws Exception{
        String uid = "a";
        doReturn(null).when(userService).findUserIdByUid(uid);

        mockMvc.perform(MockMvcRequestBuilders.get("/userId/?uid=a")
                .with(SecurityMockMvcRequestPostProcessors.user(createLoginUser())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}