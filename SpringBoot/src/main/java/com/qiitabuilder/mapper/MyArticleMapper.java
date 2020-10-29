package com.qiitabuilder.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    List<Map<String,String>> selectTest();
}
