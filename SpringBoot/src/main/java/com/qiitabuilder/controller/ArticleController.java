package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.mapper.TagMapper;
import com.qiitabuilder.service.ArticleService;
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

        return article;
    }

    /////////////////////////////
    //// DELETE
    /////////////////////////////

    /**
     * ユーザーがFBした記事の一覧を取得するメソッド
     *
     * @param userId　取得したいユーザーID
     * @return　フィードバックした記事の一覧
     */
    @RequestMapping(value = "/feedbacked",  method = RequestMethod.GET)
    public List<Article> getFeedbackedArticleListByUserId(Integer userId) {
        return articleService.getFeedbackedArticleListByUserId(userId);
    }




}
