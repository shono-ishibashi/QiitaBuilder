package com.qiitabuilder.service;

import com.qiitabuilder.domain.Article;
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

    /*
        sortId =
        1 : FBした数ランキング
        2 : 記事投稿数ランキング、Qiita推薦累計数ランキング
     */


    /**
     * FBした数ランキングを取得する.
     *
     * @return ランキングユーザー一覧
     */
    public List<RankingUser> fetchFBCountRank() {

        List<RankingUser> rankingUserList = feedbackMapper.getFBRank();
        rankingUserList = setRelationArticle(rankingUserList, 1);

        return rankingUserList;
    }

    /**
     * 記事投稿数ランキングを取得する.
     *
     * @return ランキングユーザー一覧
     */
    public List<RankingUser> fetchArticleCountRank() {

        List<RankingUser> rankingUserList = articleMapper.getPostedArticleCountRank();
        rankingUserList = setRelationArticle(rankingUserList, 2);

        return rankingUserList;
    }

    /**
     * Qiita推薦累計数ランキングを取得する.
     *
     * @return ランキングユーザー一覧
     */
    public List<RankingUser> fetchQiitaCountRank() {

        List<RankingUser> rankingUserList = recommendMapper.getQiitaRecommendedRank();
        rankingUserList = setRelationArticle(rankingUserList, 2);

        return rankingUserList;
    }

    /**
     * 上位20名において、関連記事を取得する.
     *
     * @param rankingUserList ランキングユーザー一覧
     * @param sortId          　並び替え項目
     *                        1 : FBした数ランキング
     *                        2 : 記事投稿数ランキング、Qiita推薦累計数ランキング
     * @return ランキングユーザー一覧
     */
    public List<RankingUser> setRelationArticle(List<RankingUser> rankingUserList, Integer sortId) {
        rankingUserList.stream().limit(20)
                .forEach(rankUser -> {
                    Integer userId = rankUser.getUser().getUserId();
                    List<Integer> relationArticleId = recommendMapper.getMostRecommendedArticleId(userId, sortId);
                    if (relationArticleId.isEmpty()) {
                        //並び替え項目が増えた時に備え、if文ではなくswitch文で記述.
                        switch (sortId) {
                            case 1:
                                relationArticleId = feedbackMapper.getArticleIdByUserId(userId);
                                break;
                            default:
                                relationArticleId = articleMapper.getArticleIdListByUserId(userId);
                                break;
                        }
                    }
                    Article relationArticle = articleMapper.findArticleById(relationArticleId.get(0)).get(0);
                    rankUser.setRelationArticle(relationArticle);
                });
        return rankingUserList;
    }
}
