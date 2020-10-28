package com.qiitabuilder.controller;

import com.qiitabuilder.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/")
public class UserController {
    
    @RequestMapping(value = "/" , method = RequestMethod.POST)
    public User insertUser(User user) {
        return null;
    }
    
}
