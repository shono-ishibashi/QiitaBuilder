package com.qiitabuilder.service;

import com.qiitabuilder.domain.User;
import com.qiitabuilder.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService {
    @Autowired
    private UserMapper userMapper;

    /**
     * ユーザー名、写真URL、Qiita投稿済み記事件数、フィードバック数、総Qiita投稿推薦数、タグ名と使用回数を持ったタグリスト
     *　上記を持ったユーザーを取得するメソッド
     * @param userId　
     * @return　取得したいユーザー情報
     */
    public User fetchUserDetails(Integer userId) {
        return userMapper.fetchUserDetails(userId);
    }
}
