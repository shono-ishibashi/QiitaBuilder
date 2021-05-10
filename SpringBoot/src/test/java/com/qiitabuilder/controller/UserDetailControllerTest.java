package com.qiitabuilder.controller;

import com.qiitabuilder.service.UserDetailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
class UserDetailControllerTest {

    @Mock
    private UserDetailService userDetailService;
    @InjectMocks
    UserDetailController userDetailController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userDetailController).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void fetchUserDetails_userIdがNullの処理() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/detail/"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void fetchUserDetails_正常系() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/detail/?userId=1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}