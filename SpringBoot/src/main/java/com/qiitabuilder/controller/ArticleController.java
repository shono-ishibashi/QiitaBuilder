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

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Article postArticle(@RequestBody Article article) {
        return null;
    }

    /////////////////////////////
    //// PUT
    /////////////////////////////
    

    /////////////////////////////
    //// DELETE
    /////////////////////////////
}
