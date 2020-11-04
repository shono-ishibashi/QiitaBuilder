package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.exception.BadRequestException;
import com.qiitabuilder.exception.NotFoundException;
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
import java.util.Map;
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
    public Article getArticle(@PathVariable("articleId") String articleId) {
        Integer parsedArticleId;
        // 入力値が正しくない場合はBadRequestExceptionを投げる
        try {
            parsedArticleId = Integer.parseInt(articleId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("");
        }

        Article result = articleService.getArticle(parsedArticleId);

        // 検索結果がない場合はNotFoundExceptionを投げる
        if (Objects.isNull(result)) {
            throw new NotFoundException("not found");
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

    /**
     * ユーザーがFBした記事の一覧を取得するメソッド
     *
     * @param userId　取得したいユーザーID
     * @return　フィードバックした記事の一覧
     */
    @RequestMapping(value = "/feedbacked",  method = RequestMethod.GET)
    public List<Article> getFeedbackedArticlesByUserId(Integer userId) {
        return articleService.getFeedbackedArticlesByUserId(userId);
    }

    /**
     * My記事登録した記事の一覧を取得するメソッド
     * 各記事は記事ID、タイトル、作成・更新日時、状態、各カウント、タグリスト、記事作成者のID・名前・写真URL　を持つ
     *
     * @param userId 取得したいユーザーID
     * @return　My記事登録した記事の一覧
     */
    @RequestMapping(value = "/my-articles", method = RequestMethod.GET)
    public List<Article> getMyArticlesByUserId(Integer userId){
        return articleService.getMyArticlesByUserId(userId);
    }


    @Autowired
    private QiitaAPIService qiitaAPIService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test() {
        qiitaAPIService.restTemplateTest();
    }

}
