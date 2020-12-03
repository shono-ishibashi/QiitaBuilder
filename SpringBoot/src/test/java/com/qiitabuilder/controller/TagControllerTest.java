package com.qiitabuilder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.security.SimpleLoginUser;
import com.qiitabuilder.service.TagService;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class TagControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private String BASE_URL="/tag/";

    @MockBean
    private TagService tagService;


    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    @Test
    @WithMockUser
    void fetchTag_正常系() throws Exception {
//        表示させたいtagをlistにつめる
//        そのlistをjson型に詰める
//        doreturnでfetchTagを使用した時の返しを疑似的に決定する
//        andExpectのcontent().json(resbody)で、期待するresbodyが帰ってくるか確認する

        Tag tag1=Tag.builder()
                .tagId(1)
                .tagName("java")
                .build();
        Tag tag2=Tag.builder()
                .tagId(2)
                .tagName("ruby")
                .build();
        Tag tag3=Tag.builder()
                .tagId(3)
                .tagName("go")
                .build();
        List<Tag> tags=new ArrayList<>(Arrays.asList(tag1,tag2,tag3));

        ObjectMapper mapper=new ObjectMapper();
        String resBody=mapper.writeValueAsString(tags);

        doReturn(tags).when(tagService).fetchTag();

        this.mockMvc
                .perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(resBody));
    }
    @Test
    @WithMockUser
    void fetchTag_正常系_タグがない時() throws Exception{
        this.mockMvc
                .perform(get(BASE_URL))
                .andExpect(status().isOk());
    }

    @Test
    void fetchTag_異常系_非ログイン時のテスト() throws Exception {
        this.mockMvc
                .perform(get(BASE_URL))
                .andExpect(status().isUnauthorized());
    }

}