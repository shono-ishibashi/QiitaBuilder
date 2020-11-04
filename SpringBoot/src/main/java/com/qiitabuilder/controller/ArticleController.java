package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.form.SearchArticleForm;
import com.qiitabuilder.service.ArticleService;
import com.qiitabuilder.service.QiitaAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;


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
    public List<Article> searchArticles(@Validated @ModelAttribute SearchArticleForm searchArticleForm, BindingResult result) {
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
            return articleService.searchArticles(searchArticleForm);
    }

    @RequestMapping(value = "/totalPage", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Integer totalPage(@ModelAttribute SearchArticleForm searchArticleForm) {
        System.out.println(articleService.getTotalPage(searchArticleForm));
        return articleService.getTotalPage(searchArticleForm);
    }

    @GetMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public Article fetchArticle(@PathVariable("articleId") String articleId) {
        Integer parsedArticleId;
        // 入力値が正しくない場合はBadRequestExceptionを投げる
        try {
            parsedArticleId = Integer.parseInt(articleId);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Article result = articleService.getArticle(parsedArticleId);

        // 検索結果がない場合はNotFoundExceptionを投げる
        if (Objects.isNull(result)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return result;

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
