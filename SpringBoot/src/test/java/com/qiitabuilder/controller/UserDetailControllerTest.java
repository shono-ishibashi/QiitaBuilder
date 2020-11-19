package com.qiitabuilder.controller;

import com.qiitabuilder.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserDetailControllerTest {

    @Autowired
    private UserDetailController userDetailController;

    @Test
    void fetchUserDetails_Nullチェック() {
        User user=userDetailController.fetchUserDetails(null);
        assertNull(user);
    }
}