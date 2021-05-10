package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface UserMapper {

    Optional<User> findByUid(String uid);

    void insert(User user);

    User fetchUserDetails(@Param("userId") Integer userId);

    Integer findUserIdByUid(@Param("uid") String uid);
}
