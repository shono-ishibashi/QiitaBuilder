package com.qiitabuilder.controller;

import com.qiitabuilder.domain.RankingUser;
import com.qiitabuilder.service.RankingUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user/ranking")
public class RankingUserController {

    @Autowired
    private RankingUserService rankingUserService;

    /**
     * FBした数ランキングを取得する.
     *
     * @return ランキングユーザー一覧
     */
    @GetMapping("/FBCount")
    @ResponseStatus(HttpStatus.OK)
    public List<RankingUser> fetchFBCountRank() {
        return rankingUserService.fetchFBCountRank();
    }

    /**
     * 記事投稿数ランキングを取得する.
     *
     * @return ランキングユーザー一覧
     */
    @GetMapping("/articleCount")
    @ResponseStatus(HttpStatus.OK)
    public List<RankingUser> fetchArticleCountRank() {
        return rankingUserService.fetchArticleCountRank();
    }

    /**
     * Qiita推薦累計数ランキングを取得する.
     *
     * @return ランキングユーザー一覧
     */
    @GetMapping("/qiitaRecommendedCount")
    @ResponseStatus(HttpStatus.OK)
    public List<RankingUser> fetchQiitaCountRank() {
        return rankingUserService.fetchQiitaCountRank();
    }
}
