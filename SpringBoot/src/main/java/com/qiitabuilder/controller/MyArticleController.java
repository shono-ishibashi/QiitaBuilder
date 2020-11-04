package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.MyArticle;
import com.qiitabuilder.service.MyArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/my-article")
public class MyArticleController {

    @Autowired
    private MyArticleService myArticleService;

    /////////////////////////////
    //// GET
    /////////////////////////////
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public MyArticle fetchMyArticle(Integer articleId, Integer registerUserId) {
        return myArticleService.fetchMyArticle(articleId, registerUserId);
    }

    /////////////////////////////
    //// POST
    /////////////////////////////
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public MyArticle postMyArticle(@RequestBody MyArticle myArticle) {
        return myArticleService.postMyArticle(myArticle);
    }
    /////////////////////////////
    //// PUT
    /////////////////////////////


    /////////////////////////////
    //// DELETE
    /////////////////////////////
    @DeleteMapping("/{myArticleId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMyArticle(@PathVariable("myArticleId") Integer myArticleId) {
        myArticleService.deleteMyArticle(myArticleId);
    }
}
