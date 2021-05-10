package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.mapper.TagMapper;
import com.qiitabuilder.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> fetchTag(){
        return tagService.fetchTag();
    }
}
