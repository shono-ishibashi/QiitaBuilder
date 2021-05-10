package com.qiitabuilder.service;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.RankingUser;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.mapper.ArticleMapper;
import com.qiitabuilder.mapper.FeedbackMapper;
import com.qiitabuilder.mapper.RecommendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
        rankingItemId =
        1 : FBした数ランキング
        2 : 記事投稿数ランキング
        3 : Qiita推薦累計数ランキング
     */


    /**
     * FBした数ランキングを取得する.
     *
     * @return ランキングユーザー一覧
     */
    public List<RankingUser> fetchFBCountRank() {

        List<RankingUser> rankingUserList = feedbackMapper.getFBRank();
        if (!rankingUserList.isEmpty()) {
            rankingUserList = setRelationArticle(rankingUserList, 1);
            rankingUserList = setRank(rankingUserList, 1);
        }
        return rankingUserList;
    }

    /**
     * 記事投稿数ランキングを取得する.
     *
     * @return ランキングユーザー一覧
     */
    public List<RankingUser> fetchArticleCountRank() {

        List<RankingUser> rankingUserList = articleMapper.getPostedArticleCountRank();
        if (!rankingUserList.isEmpty()) {
            rankingUserList = setRelationArticle(rankingUserList, 2);
            rankingUserList = setRank(rankingUserList, 2);
        }
        return rankingUserList;
    }

    /**
     * Qiita推薦累計数ランキングを取得する.
     *
     * @return ランキングユーザー一覧
     */
    public List<RankingUser> fetchQiitaCountRank() {

        List<RankingUser> rankingUserList = recommendMapper.getQiitaRecommendedRank();
        if (!rankingUserList.isEmpty()) {
            rankingUserList = setRelationArticle(rankingUserList, 3);
            rankingUserList = setRank(rankingUserList, 3);
        }
        return rankingUserList;
    }

    /**
     * 上位20名において、関連記事を取得する.
     *
     * @param rankingUserList ランキングユーザー一覧
     * @param rankingItemId   　ランキング項目
     *                        1 : FBした数ランキング
     *                        2 : 記事投稿数ランキング
     *                        3 : Qiita推薦累計数ランキング
     * @return ランキングユーザー一覧
     */
    public List<RankingUser> setRelationArticle(List<RankingUser> rankingUserList, Integer rankingItemId) {
        rankingUserList.stream().limit(20)
                .forEach(rankUser -> {
                    Integer userId = rankUser.getUser().getUserId();
                    List<Integer> relationArticleId = recommendMapper.getMostRecommendedArticleId(userId, rankingItemId);

                    // Qiita推薦された関連記事が無い場合
                    if (Objects.isNull(relationArticleId.get(0))) {
                        // 並び替え項目が増えた時に備え、if文ではなくswitch文で記述.
                        List<Integer> articleIdList;
                        switch (rankingItemId) {
                            case 1:
                                // FBした数ランキング
                                // 最新のFBした記事を取得.
                                articleIdList = feedbackMapper.getArticleIdListByUserId(userId);
                                break;
                            default:
                                // 記事投稿数ランキング, Qiita推薦累計数ランキング
                                // 最新の投稿記事を取得.
                                articleIdList = articleMapper.getArticleIdListByUserId(userId);
                                break;
                        }
                        if (!articleIdList.isEmpty()) {
                            relationArticleId.set(0, articleIdList.get(0));
                        }
                    }
                    if (!Objects.isNull(relationArticleId.get(0))) {
                        Article relationArticle = articleMapper.findArticleById(relationArticleId.get(0)).get(0);
                        rankUser.setRelationArticle(relationArticle);
                    }
                });
        return rankingUserList;
    }

    /**
     * ランキングユーザーに同率順位を考慮した順位付けをする.
     *
     * @param rankingUserList ランキングユーザー一覧
     * @param rankingItemId   ランキング項目
     *                        1 : FBした数ランキング
     *                        2 : 記事投稿数ランキング
     *                        3 : Qiita推薦累計数ランキング
     * @return ランキングユーザー一覧
     */
    public List<RankingUser> setRank(List<RankingUser> rankingUserList, Integer rankingItemId) {
        int rank = 1;
        rankingUserList.get(0).setRank(rank);
        for (int i = 1; i < rankingUserList.size(); i++) {
            User rankUser = rankingUserList.get(i).getUser();
            User compareRankUser = rankingUserList.get(i - 1).getUser();

            switch (rankingItemId) {
                case 1:
                    if (!rankUser.getFeedbackCount().equals(compareRankUser.getFeedbackCount())) {
                        rank = i + 1;
                    }
                    break;
                case 2:
                    if (!rankUser.getPostedArticleCount().equals(compareRankUser.getPostedArticleCount())) {
                        rank = i + 1;
                    }
                    break;
                case 3:
                    if (!rankUser.getQiitaRecommendedAllCount().equals(compareRankUser.getQiitaRecommendedAllCount())) {
                        rank = i + 1;
                    }
                    break;
            }
            rankingUserList.get(i).setRank(rank);
        }
        return rankingUserList;
    }
}
