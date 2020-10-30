package com.qiitabuilder.controller;

import com.qiitabuilder.domain.User;
import com.qiitabuilder.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "user/detail")
public class UserDetailController {
    @Autowired
    private UserDetailService userDetailService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public User fetchUserDetails(Integer userId) {
        return userDetailService.fetchUserDetails(userId);
    }
}
