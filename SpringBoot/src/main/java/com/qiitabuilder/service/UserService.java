package com.qiitabuilder.service;

import com.qiitabuilder.domain.User;
import com.qiitabuilder.mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@MapperScan(basePackages = {"com.qiitabuilder.mapper"})
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findByUserId(Integer userId) {
        return null;
    }

    public User findByUid(String uid) {
        return null;
    }

    public void insertUser(User user) {
    }

    public void test() {
        System.out.println(userMapper.selectTest());
    }
}
