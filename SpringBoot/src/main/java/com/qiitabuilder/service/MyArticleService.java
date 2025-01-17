package com.qiitabuilder.service;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.MyArticle;
import com.qiitabuilder.mapper.ArticleMapper;
import com.qiitabuilder.mapper.MyArticleMapper;
import com.qiitabuilder.security.SimpleLoginUser;
import com.qiitabuilder.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@Transactional
public class MyArticleService {

    @Autowired
    private MyArticleMapper myArticleMapper;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 引数に一致するレコードを取得する
     *
     * @param articleId
     * @return
     */
    public MyArticle fetchMyArticle(Integer articleId) {
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer registerUserId = loginUser.getUser().getUserId();

        MyArticle result = myArticleMapper.findByArticleIdAndRegisterUserId(articleId, registerUserId);
        // My記事登録済みでない場合はNoContentを返す
        if (Objects.isNull(result)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return result;
    }

    /**
     * 記事IDとMy記事登録ユーザーIDを基にMy記事登録を行う
     *
     * @param myArticle
     * @return
     */
    public MyArticle postMyArticle(MyArticle myArticle) {
        // ログイン中のユーザ情報をregisterUserIdにセット
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        myArticle.setRegisterUserId(loginUser.getUser().getUserId());

        // 記事の投稿者をpostedUserIdにセット
        Article article = articleMapper.getArticleAndFeedback(myArticle.getArticleId());
        // 記事IDが存在しない場合はBadRequestを返す
        try {
            myArticle.setPostedUserId(article.getPostedUser().getUserId());
        } catch (NullPointerException e) {
            LogUtils.error("MyArticleService#postMyArticle",e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // すでにDBに登録されている場合はConflictを投げる
        MyArticle insertedMyArticle = myArticleMapper.findByArticleIdAndRegisterUserId(myArticle.getArticleId(), myArticle.getRegisterUserId());
        if (Objects.nonNull(insertedMyArticle)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        myArticleMapper.insert(myArticle);

        Integer myArticleId = myArticleMapper.getAutoIncrementKey();
        myArticle.setMyArticleId(myArticleId);

        return myArticle;
    }

    /**
     * My記事を削除する
     *
     * @param myArticleId
     */
    public void deleteMyArticle(Integer myArticleId) {
        // 自分以外のMy記事情報を削除しようとする場合はステータスコード403を返す
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyArticle myArticle = myArticleMapper.load(myArticleId);
        if (Objects.nonNull(myArticle) && !Objects.equals(myArticle.getRegisterUserId(),loginUser.getUser().getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // レコードの削除がなかった場合はConflictを投げる
        if (!myArticleMapper.delete(myArticleId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

}
