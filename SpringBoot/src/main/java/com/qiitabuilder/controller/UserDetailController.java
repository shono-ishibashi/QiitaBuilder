package com.qiitabuilder.controller;

import com.qiitabuilder.domain.User;
import com.qiitabuilder.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping(value = "/user/detail")
public class UserDetailController {
    @Autowired
    private UserDetailService userDetailService;

    /**
     * user詳細画面上部に表示する情報を取得するメソッド
     * ユーザー名、写真URL、Qiita投稿済み記事件数、フィードバック数、総Qiita投稿推薦数、タグ名と使用回数
     *
     * @param userId
     * @return　取得したいユーザー情報
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public User fetchUserDetails(String userId) {
        if (Objects.isNull(userId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try {
            Integer id=Integer.valueOf(userId);
            return userDetailService.fetchUserDetails(id);
        }catch (NumberFormatException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }
}
