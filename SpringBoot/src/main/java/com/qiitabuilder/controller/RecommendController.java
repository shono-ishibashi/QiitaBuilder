package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Recommend;
import com.qiitabuilder.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

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
     * <p>
     * Httpステータス
     * OK
     * NoContent Qiita推薦登録済みでない場合
     * BadRequest 入力値エラーの場合
     *
     * @param articleId
     * @return
     */
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Recommend fetchRecommend(Integer articleId) {
        if (Objects.isNull(articleId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return recommendService.fetchRecommend(articleId);
    }

    /////////////////////////////
    //// POST
    /////////////////////////////

    /**
     * Qiita推薦への登録処理を行う
     * <p>
     * Httpステータス
     * OK
     * BadRequest 入力値エラーの場合 & 記事IDが存在しない場合
     * Conflict DBに登録済みの場合
     *
     * @param recommend
     * @return
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Recommend postRecommend(@RequestBody Recommend recommend) {
        //// エラーハンドリング
        // 入力値エラーの場合はBadRequestを返す
        if (Objects.isNull(recommend.getArticleId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

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
     * <p>
     * Httpステータス
     * OK
     * BadRequest 入力値エラーの場合
     * Forbidden 自分以外のデータの場合
     * Conflict DBに登録されていない場合
     *
     * @param recommendId
     */
    @DeleteMapping("/{recommendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRecommend(@PathVariable("recommendId") String recommendId) {
        // 入力値エラーの場合はBadRequestを返す
        if (!recommendId.matches("^\\d+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        recommendService.deleteRecommend(Integer.parseInt(recommendId));
    }
}
