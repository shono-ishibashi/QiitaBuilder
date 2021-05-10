package com.qiitabuilder.service;

import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;

    public List<Tag> fetchTag(){
        return tagMapper.findAll();
    }
}
