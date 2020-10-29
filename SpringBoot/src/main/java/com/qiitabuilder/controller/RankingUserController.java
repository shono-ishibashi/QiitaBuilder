package com.qiitabuilder.controller;

import com.qiitabuilder.domain.RankingUser;
import com.qiitabuilder.service.RankingUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user/ranking")
public class RankingUserController {

    @Autowired
    private RankingUserService rankingUserService;

    @GetMapping("/FBCount")
    public List<RankingUser> fetchFBCountRank(){
        List<RankingUser> rankList = rankingUserService.fetchFBCountRank();
        return rankList;
    }

    @GetMapping("/articleCount")
    public List<RankingUser> fetchArticleCountRank(){
        List<RankingUser> rankList = rankingUserService.fetchArticleCountRank();
        return rankList;
    }

    @GetMapping("/qiitaRecommendedCount")
    public List<RankingUser> fetchQiitaCountRank(){
        List<RankingUser> rankList = rankingUserService.fetchQiitaCountRank();
        return rankList;
    }
}
