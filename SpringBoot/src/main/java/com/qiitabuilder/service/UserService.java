package com.qiitabuilder.service;

import com.qiitabuilder.domain.User;
import com.qiitabuilder.mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByUserId(Integer userId) {
        return null;
    }

    public User findByUid(String uid) {
        return null;
    }

    public void insertUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
    }

    public void test() {
        System.out.println(userMapper.selectTest());
    }
}
