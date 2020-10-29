package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagMapper {
    List<Tag> findAll();
    void postTag(Tag tag);
    List<Tag> findByUserId(Integer userId);
}
