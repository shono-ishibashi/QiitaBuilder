package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/tag")
public class TagController {

    public List<Tag> fetchTag(){
        return null;
    }

    public void postTag(){

    }
}
