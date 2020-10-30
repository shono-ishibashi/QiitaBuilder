package com.qiitabuilder.service;

import com.qiitabuilder.domain.User;
import com.qiitabuilder.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService {
    @Autowired
    private UserMapper userMapper;

    public User fetchUserDetails(Integer userId) {
        return userMapper.fetchUserDetails(userId);
    }
}
