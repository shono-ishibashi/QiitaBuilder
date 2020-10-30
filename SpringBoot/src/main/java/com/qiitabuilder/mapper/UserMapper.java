package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserMapper {
    List<Map<String, String>> selectTest();
    User fetchUserDetails(Integer userId);
}
