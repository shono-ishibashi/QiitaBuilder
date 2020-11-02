package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/tag")
public class TagController {

    @Autowired
    private TagMapper tagMapper;

    public List<Tag> fetchTag(){
        return tagMapper.findAll();
    }
}
