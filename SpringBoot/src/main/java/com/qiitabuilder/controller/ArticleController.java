package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.form.SearchArticleForm;
import com.qiitabuilder.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Article> fetchArticle(@ModelAttribute SearchArticleForm searchArticleForm) {
        /////////////////////////////
        //// GET
        return articleService.fetchArticle(searchArticleForm);
        /////////////////////////////
    }


    @RequestMapping(value = "/totalPage" ,method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Integer totalPage(@ModelAttribute SearchArticleForm searchArticleForm){
        System.out.println(articleService.getTotalPage(searchArticleForm));
        return articleService.getTotalPage(searchArticleForm);
    }

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
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Article editArticle(@RequestBody Article article) {

        System.out.println(article);

        return null;
    }

    /////////////////////////////
    //// DELETE
    /////////////////////////////
}
