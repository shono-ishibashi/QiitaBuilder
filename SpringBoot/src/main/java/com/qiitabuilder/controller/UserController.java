package com.qiitabuilder.controller;

import com.qiitabuilder.domain.User;
import com.qiitabuilder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(value = "/")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *
     * userを新規登録するメソッド
     *
     * @param requestBody フロント側から投げられてくるリクエストボディ
     * @return
     */
    @RequestMapping(value = "/" , method = RequestMethod.POST)
    public Map<String, String> insertUser(@RequestBody Map<String, String> requestBody) {
        User user = new User();
        user.setUid(requestBody.get("uid"));
        user.setPassword(requestBody.get("password"));

        userService.insertUser(user);

        return null;
    }

}
