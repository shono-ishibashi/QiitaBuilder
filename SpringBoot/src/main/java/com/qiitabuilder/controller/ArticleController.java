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

import java.util.*;

import static java.util.Objects.isNull;


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
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (isNull(searchArticleForm.getStateFlagList())) {
            searchArticleForm.setStateFlagList(Arrays.asList(1, 2));
        } else if (searchArticleForm.getStateFlagList().get(0) == 10) {
            searchArticleForm.setStateFlagList(Arrays.asList(0, 1, 2));
        }
        System.out.println(searchArticleForm.getStateFlagList());
        return articleService.searchArticles(searchArticleForm);
    }

    @RequestMapping(value = "/totalPage", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Integer totalPage(@Validated @ModelAttribute SearchArticleForm searchArticleForm, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        searchArticleForm.setStateFlagList(Arrays.asList(1, 2));
        return articleService.getTotalPage(searchArticleForm);
    }

    /**
     * 記事詳細情報を取得する
     *
     * @param articleId
     * @return
     */
    @GetMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public Article fetchArticle(@PathVariable("articleId") String articleId) {
        Integer parsedArticleId;
        // 入力値が正しくない場合はBadRequestExceptionを投げる
        try {
            parsedArticleId = Integer.parseInt(articleId);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "不正なリクエストです。再度お試しください。");
        }

        Article result = articleService.getArticle(parsedArticleId);

        // 検索結果がない場合はNotFoundExceptionを投げる
        if (isNull(result)) {
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
        if (
                article.getTags().size() <= 0 ||
                        article.getTags().size() > 5 ||
                        article.getTitle().isEmpty() ||
                        isNull(article.getTitle()) ||
                        article.getContent().isEmpty() ||
                        isNull(article.getContent())
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        }
        System.out.println("insert");
        System.out.println(article);
        return articleService.saveArticle(article);
    }

    /////////////////////////////
    //// PUT
    /////////////////////////////


    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Article editArticle(@RequestBody Article article) {
        if (
                isNull(article.getArticleId()) ||
                        article.getTags().size() <= 0 ||
                        article.getTags().size() > 5 ||
                        article.getTitle().isEmpty() ||
                        isNull(article.getTitle()) ||
                        article.getContent().isEmpty() ||
                        isNull(article.getContent())
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        }
        return articleService.saveArticle(article);
    }

    /////////////////////////////
    //// DELETE
    /////////////////////////////

    /**
     * ユーザーがFBした記事の一覧を取得するメソッド
     *
     * @param userId 　取得したいユーザーID
     * @return　フィードバックした記事の一覧
     */
    @RequestMapping(value = "/feedbacked", method = RequestMethod.GET)
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
    public List<Article> getMyArticlesByUserId(Integer userId) {
        return articleService.getMyArticlesByUserId(userId);
    }
}
