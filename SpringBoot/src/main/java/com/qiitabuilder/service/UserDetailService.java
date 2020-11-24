package com.qiitabuilder.service;

import com.qiitabuilder.domain.User;
import com.qiitabuilder.mapper.UserMapper;
import com.qiitabuilder.security.SimpleLoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class UserDetailService {
    @Autowired
    private UserMapper userMapper;

    /**
     * ユーザー名、写真URL、Qiita投稿済み記事件数、フィードバック数、総Qiita投稿推薦数、タグ名と使用回数を持ったタグリスト
     * 　上記を持ったユーザーを取得するメソッド
     *
     * @param userId
     * @return　取得したいユーザー情報
     */
    public User fetchUserDetails(Integer userId) {
        User user = userMapper.fetchUserDetails(userId);
        if (Objects.isNull(user)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUid().equals(loginUser.getUser().getUid())) {
            user.setIsLoginUser(true);
        } else {
            user.setIsLoginUser(false);
        }
        return user;
    }
}
