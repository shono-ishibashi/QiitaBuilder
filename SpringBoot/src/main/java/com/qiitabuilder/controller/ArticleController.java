package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Article;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/article")
public class ArticleController {

    /////////////////////////////
    //// GET
    /////////////////////////////

    @GetMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public Article getArticle(@PathVariable("articleId") String articleId) {
        return null;
    }

    /////////////////////////////
    //// POST
    /////////////////////////////

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Article postArticle(@RequestBody Article article) {
        return null;
    }

    /////////////////////////////
    //// PUT
    /////////////////////////////
    @RequestMapping(value = "/" , method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Article editArticle(@RequestBody Article article) {

        System.out.println(article);

        return null;
    }

    /////////////////////////////
    //// DELETE
    /////////////////////////////
}
