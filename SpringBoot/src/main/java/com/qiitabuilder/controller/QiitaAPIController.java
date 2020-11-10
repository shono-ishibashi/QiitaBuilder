package com.qiitabuilder.controller;

import com.qiitabuilder.domain.QiitaConfiguration;
import com.qiitabuilder.service.QiitaAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping(value = "/qiita")
public class QiitaAPIController {

    @Autowired
    private QiitaAPIService qiitaAPIService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String test() {
        return "redirect:https://www.google.com/";
    }
}
