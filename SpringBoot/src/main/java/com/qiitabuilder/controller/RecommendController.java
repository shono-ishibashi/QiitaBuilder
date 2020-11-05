package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Recommend;
import com.qiitabuilder.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    /////////////////////////////
    //// GET
    /////////////////////////////

    /**
     * Qiita推薦情報を取得する
     * NotFound Qiita推薦済みでない場合
     *
     * @param articleId
     * @return
     */
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Recommend fetchRecommend(Integer articleId) {
        return recommendService.fetchRecommend(articleId);
    }

    /////////////////////////////
    //// POST
    /////////////////////////////

    /**
     * Qiita推薦への登録処理を行う
     * BadRequest 入力値エラーの場合&記事IDが存在しない場合, Conflict DBに登録済みの場合
     *
     * @param recommend
     * @return
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Recommend postRecommend(@RequestBody Recommend recommend) {
        return recommendService.postRecommend(recommend);
    }

    /////////////////////////////
    //// PUT
    /////////////////////////////


    /////////////////////////////
    //// DELETE
    /////////////////////////////

    /**
     * Qiita推薦からの物理削除を行う
     * BadRequest 入力値エラーの場合, Conflict DBに登録されていない場合
     *
     * @param recommendId
     */
    @DeleteMapping("/{recommendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRecommend(@PathVariable("recommendId") String recommendId) {
        recommendService.deleteRecommend(recommendId);
    }
}
