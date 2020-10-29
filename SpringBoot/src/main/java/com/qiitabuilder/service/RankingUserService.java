package com.qiitabuilder.service;

import com.qiitabuilder.domain.RankingUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RankingUserService {

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
