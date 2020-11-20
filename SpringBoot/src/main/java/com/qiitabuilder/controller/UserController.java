package com.qiitabuilder.controller;

import com.qiitabuilder.domain.User;
import com.qiitabuilder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;


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
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Map<String, String> insertUser(@RequestBody User user) {

        userService.insertUser(user);

        return null;
    }

    @RequestMapping(value = "userId", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Integer findUserIdByUid(String uid) {
        if (Objects.isNull(uid))throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Integer userId=userService.findUserIdByUid(uid);
        if (Objects.isNull(userId))throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return userId;
    }

}
