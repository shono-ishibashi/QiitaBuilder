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
     * @param recommendUserId
     * @return
     */
    public Recommend fetchRecommend(Integer articleId, Integer recommendUserId) {
        Recommend result = recommendMapper.findByArticleIdAndRecommendUserId(articleId, recommendUserId);
        // Qiita推薦済みでない場合はNotFoundを返す
        if (Objects.isNull(result)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
        Article article = articleMapper.load(recommend.getArticleId());
        // 記事IDが存在しない場合はBadRequestを返す
        try {
            recommend.setPostedUserId(article.getPostedUser().getUserId());
        } catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //// エラーハンドリング
        // 入力値エラーの場合はBadRequestを返す
        if (!String.valueOf(recommend.getArticleId()).matches("^\\d+$")) {
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
    public void deleteRecommend(String recommendId) {
        // 入力値エラーの場合はBadRequestを返す
        if (!recommendId.matches("^\\d+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // レコードの削除がなかった場合はConflictを投げる
        if (!recommendMapper.delete(Integer.parseInt(recommendId))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
}
