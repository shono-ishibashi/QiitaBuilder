package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
@Repository
public interface UserMapper {
    List<Map<String, String>> selectTest();

    Optional<User> findByUid(String uid);

    Optional<User> findByUserId(Integer userId);

    void insert(User user);

    User fetchUserDetails(@Param("userId") Integer userId);
}
