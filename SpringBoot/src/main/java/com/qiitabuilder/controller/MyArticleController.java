package com.qiitabuilder.controller;

import com.qiitabuilder.domain.MyArticle;
import com.qiitabuilder.service.MyArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/my-article")
public class MyArticleController {

    @Autowired
    private MyArticleService myArticleService;

    /////////////////////////////
    //// GET
    /////////////////////////////
    /**
     * My記事登録情報を取得する
     * NotFound My記事登録済みでない場合
     *
     * @param articleId
     * @return
     */
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public MyArticle fetchMyArticle(Integer articleId) {
        return myArticleService.fetchMyArticle(articleId);
    }

    /////////////////////////////
    //// POST
    /////////////////////////////

    /**
     * My記事登録処理を行う
     * BadRequest 入力値エラーの場合&記事IDが存在しない場合, Conflict DBに登録済みの場合
     *
     * @param myArticle
     * @return
     */
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
    public void deleteMyArticle(@PathVariable("myArticleId") String myArticleId) {
        myArticleService.deleteMyArticle(myArticleId);
    }
}
