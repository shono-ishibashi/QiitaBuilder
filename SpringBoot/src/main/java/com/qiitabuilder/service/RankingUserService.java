package com.qiitabuilder.service;

import com.qiitabuilder.domain.RankingUser;
import com.qiitabuilder.mapper.ArticleMapper;
import com.qiitabuilder.mapper.FeedbackMapper;
import com.qiitabuilder.mapper.RecommendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RankingUserService {

    @Autowired
    private FeedbackMapper feedbackMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RecommendMapper recommendMapper;

    public List<RankingUser> fetchFBCountRank(){
        return null;
    }

    public List<RankingUser> fetchArticleCountRank(){
        return null;
    }

    public List<RankingUser> fetchQiitaCountRank(){
        return null;
    }
}
