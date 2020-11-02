package com.qiitabuilder.service;

import com.qiitabuilder.domain.MyArticle;
import com.qiitabuilder.domain.Recommend;
import com.qiitabuilder.mapper.RecommendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommendService {

    @Autowired
    private RecommendMapper recommendMapper;

    /**
     * 引数に一致するレコードを取得する
     *
     * @param articleId
     * @param recommendUserId
     * @return
     */
    public Recommend fetchRecommend(Integer articleId, Integer recommendUserId) {
        return recommendMapper.findByArticleIdAndRecommendUserId(articleId, recommendUserId);
    }

    /**
     * 記事IDとQiita推薦ユーザーIDを基にQiita推薦登録を行う
     *
     * @param recommend
     * @return
     */
    public Recommend postRecommend(Recommend recommend) {
        recommendMapper.insert(recommend);
        Integer recommendId = recommendMapper.getAutoIncrementKey();
        recommend.setRecommendId(recommendId);
        return recommend;
    }
}
