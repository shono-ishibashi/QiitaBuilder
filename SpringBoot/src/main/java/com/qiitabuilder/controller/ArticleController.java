package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.form.SearchArticleForm;
import com.qiitabuilder.service.ArticleService;
import com.qiitabuilder.mapper.TagMapper;
import com.qiitabuilder.service.ArticleService;
import com.qiitabuilder.service.QiitaAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /////////////////////////////
    //// GET
    /////////////////////////////

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Article> fetchArticle(@ModelAttribute SearchArticleForm searchArticleForm) {
        return articleService.fetchArticle(searchArticleForm);
    }

    @RequestMapping(value = "/totalPage", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Integer totalPage(@ModelAttribute SearchArticleForm searchArticleForm) {
        System.out.println(articleService.getTotalPage(searchArticleForm));
        return articleService.getTotalPage(searchArticleForm);
    }

    @GetMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public Article getArticle(@PathVariable("articleId") Integer articleId) {
        return articleService.getArticle(articleId);
    }

    /////////////////////////////
    //// POST
    /////////////////////////////

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Article postArticle(@RequestBody Article article) {
        articleService.saveArticle(article);
        return null;
    }

    /////////////////////////////
    //// PUT
    /////////////////////////////


    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Article editArticle(@RequestBody Article article) {
        articleService.saveArticle(article);
        return article;
    }

    /////////////////////////////
    //// DELETE
    /////////////////////////////


    @Autowired
    private QiitaAPIService qiitaAPIService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test() {
        qiitaAPIService.restTemplateTest();
    }

}
