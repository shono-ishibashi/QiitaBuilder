package com.qiitabuilder.service;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Recommend;
import com.qiitabuilder.mapper.ArticleMapper;
import com.qiitabuilder.mapper.RecommendMapper;
import com.qiitabuilder.security.SimpleLoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class RecommendService {

    @Autowired
    private RecommendMapper recommendMapper;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 引数に一致するレコードを取得する
     *
     * @param articleId
     * @return
     */
    public Recommend fetchRecommend(Integer articleId) {
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer recommendUserId = loginUser.getUser().getUserId();

        Recommend result = recommendMapper.findByArticleIdAndRecommendUserId(articleId, recommendUserId);
        // Qiita推薦済みでない場合はNoContentを返す
        if (Objects.isNull(result)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return result;
    }

    /**
     * 記事IDとQiita推薦ユーザーIDを基にQiita推薦登録を行う
     *
     * @param recommend
     * @return
     */
    public Recommend postRecommend(Recommend recommend) {
        // ログイン中のユーザ情報をrecommendUserIdにセット
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recommend.setRecommendUserId(loginUser.getUser().getUserId());

        // 記事の投稿者をpostedUserIdにセット
        Article article = articleMapper.getArticleAndFeedback(recommend.getArticleId());
        // 記事IDが存在しない場合はBadRequestを返す
        try {
            recommend.setPostedUserId(article.getPostedUser().getUserId());
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // すでにDBに登録されている場合はConflictを投げる
        Recommend insertedRecommend = recommendMapper.findByArticleIdAndRecommendUserId(recommend.getArticleId(), recommend.getRecommendUserId());
        if (Objects.nonNull(insertedRecommend)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        recommendMapper.insert(recommend);

        Integer recommendId = recommendMapper.getAutoIncrementKey();
        recommend.setRecommendId(recommendId);

        return recommend;
    }

    /**
     * My記事を削除する
     *
     * @param recommendId
     */
    public void deleteRecommend(Integer recommendId) {
        // 自分以外のMy記事情報を削除しようとする場合はステータスコード403を返す
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Recommend recommend = recommendMapper.load(recommendId);
        if (Objects.nonNull(recommend) && !Objects.equals(recommend.getRecommendUserId(),loginUser.getUser().getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // レコードの削除がなかった場合はConflictを投げる
        if (!recommendMapper.delete(recommendId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
}
