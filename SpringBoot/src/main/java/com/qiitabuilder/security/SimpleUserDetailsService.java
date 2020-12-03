package com.qiitabuilder.security;

import com.qiitabuilder.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Userエンティティ
 */
public class SimpleUserDetailsService implements UserDetailsService {

    private UserMapper userMapper;

    public SimpleUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String uid) {
        return userMapper.findByUid(uid)
                .map(SimpleLoginUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}
