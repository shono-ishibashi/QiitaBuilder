package com.qiitabuilder.controller;

import com.qiitabuilder.domain.User;
import com.qiitabuilder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/" , method = RequestMethod.POST)
    public User insertUser(User user) {
        return null;
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(){
        userService.test();
        return "test";
    }

}
