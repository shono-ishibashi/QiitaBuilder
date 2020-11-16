package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mapper
@Repository
public interface UserMapper {
    List<Map<String,String>> selectTest();

    Optional<User> findByUid(String uid);

    void insert(User user);

    String findQiitaAPICodeByUserId(Integer userId);

    String updateQiitaAPICode(String qiitaAPICode,Integer userId);

    User fetchUserDetails(@Param("userId") Integer userId);

}
