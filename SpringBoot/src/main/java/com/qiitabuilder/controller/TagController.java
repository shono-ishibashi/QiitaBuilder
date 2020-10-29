package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/tag")
public class TagController {

    public List<Tag> fetchTag(){
        return null;
    }

    public void postTag(){

    }
}
