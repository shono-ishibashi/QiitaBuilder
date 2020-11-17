package com.qiitabuilder.controller;

import com.qiitabuilder.domain.QiitaConfiguration;
import com.qiitabuilder.security.SimpleLoginUser;
import com.qiitabuilder.service.QiitaAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/qiita")
public class QiitaAPIRestController {

    @Autowired
    private QiitaAPIService qiitaAPIService;

    //
    @RequestMapping(value = "/check-qiita-api-authentication", method = RequestMethod.POST)
    public void checkQiitaAPIAuthentication(@RequestBody QiitaConfiguration qiitaConfiguration) {
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        qiitaConfiguration.setUserId(loginUser.getUser().getUserId());

        if(qiitaAPIService.isAuthenticated(qiitaConfiguration)){
            qiitaAPIService.saveToken();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/to-qiita-api-authentication", method = RequestMethod.GET)
    public String generateQiitaAPIAuthenticationURL() {

        return qiitaAPIService.generateQiitaAPIAuthenticationURL();
    }

    @RequestMapping(value = "/save-article-to-qiita/{articleId}",method = RequestMethod.POST)
    public void saveArticleToQiita(@PathVariable("articleId") String articleId) {
        Integer integerArticleId;

        try {
            integerArticleId = Integer.parseInt(articleId);
        } catch (NumberFormatException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        qiitaAPIService.saveArticleToQiita(integerArticleId);
    }

    @RequestMapping(value = "/is-linked-to-qiita" ,method = RequestMethod.GET)
    public boolean isLinkedToQiita(){
        return qiitaAPIService.isLinkedToQiita();
    }
}
