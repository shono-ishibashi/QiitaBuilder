package com.qiitabuilder.service;

import com.qiitabuilder.domain.MyArticle;
import com.qiitabuilder.mapper.MyArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyArticleService {

    @Autowired
    private MyArticleMapper myArticleMapper;

    /**
     * 引数に一致するレコードのmyArticleIdを取得する
     *
     * @param articleId
     * @param registerUserId
     * @return
     */
    public MyArticle fetchMyArticle(Integer articleId, Integer registerUserId) {
        MyArticle result = myArticleMapper.findByArticleIdAndRegisterUserId(articleId, registerUserId);
        return myArticleMapper.findByArticleIdAndRegisterUserId(articleId, registerUserId);
    }

    /**
     * 記事IDとMy記事登録ユーザーIDを基にMy記事登録を行う
     *
     * @param myArticle
     * @return
     */
    public MyArticle postMyArticle(MyArticle myArticle) {
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
        myArticleMapper.delete(myArticleId);
    }

}
