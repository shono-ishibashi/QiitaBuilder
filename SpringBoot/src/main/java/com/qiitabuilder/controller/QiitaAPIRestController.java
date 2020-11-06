package com.qiitabuilder.controller;

import com.qiitabuilder.domain.QiitaConfiguration;
import com.qiitabuilder.security.SimpleLoginUser;
import com.qiitabuilder.service.QiitaAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/qiita")
public class QiitaAPIRestController {

    @Autowired
    private QiitaAPIService qiitaAPIService;

    //
    @RequestMapping(value = "/check-qiita-api-authentication", method = RequestMethod.POST)
    public String checkQiitaAPIAuthentication(@RequestBody QiitaConfiguration qiitaConfiguration) {
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        qiitaConfiguration.setUserId(loginUser.getUser().getUserId());

        if(qiitaAPIService.isAuthenticated(qiitaConfiguration)){
            return "aaa";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/to-qiita-api-authentication", method = RequestMethod.GET)
    public String toQiitaAPIAuthentication() {

        return qiitaAPIService.generateQiitaAPIAuthenticationURL();
    }

}
