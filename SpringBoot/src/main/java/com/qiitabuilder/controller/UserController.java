package com.qiitabuilder.controller;

import com.qiitabuilder.domain.User;
import com.qiitabuilder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(value = "/")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * userを新規登録するメソッド
     *
     * @param user フロント側から投げられてくるリクエストボディ
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void insertUser(@RequestBody User user) {
        userService.insertUser(user);
    }

    @RequestMapping(value = "userId", method = RequestMethod.GET)
    public Integer findUserIdByUid(String uid) {
        return userService.findUserIdByUid(uid);
    }

}
