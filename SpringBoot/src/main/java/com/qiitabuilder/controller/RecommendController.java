package com.qiitabuilder.controller;

import com.qiitabuilder.domain.MyArticle;
import com.qiitabuilder.domain.Recommend;
import com.qiitabuilder.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    /////////////////////////////
    //// GET
    /////////////////////////////
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Recommend fetchRecommend(Integer articleId, Integer recommendUserId) {
        return recommendService.fetchRecommend(articleId, recommendUserId);
    }

    /////////////////////////////
    //// POST
    /////////////////////////////
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
}
