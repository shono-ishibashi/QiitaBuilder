package com.qiitabuilder.service;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.mapper.FeedbackMapper;
import com.qiitabuilder.mapper.MyArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ArticleService {
    @Autowired
    private FeedbackMapper feedbackMapper;
    @Autowired
    private MyArticleMapper myArticleMapper;

    public void saveArticle(Article article) {

        // articleId で 更新、追加を分岐
        if ( isNull(article.getArticleId()) ){

        } else {

        }

        //入力されたTagがDBに存在しなければ、DBへ追加


    }

    /**
     * ユーザーがFBした記事の一覧を取得するメソッド
     *各記事は記事ID、タイトル、作成・更新日時、状態、各カウント、タグリスト、記事作成者のID・名前・写真URL　を持つ
     *
     * @param userId　取得したいユーザーID
     * @return　フィードバックした記事の一覧
     */
    public List<Article> getFeedbackedArticlesByUserId(Integer userId) {
        return feedbackMapper.getFeedbackedArticlesByUserId(userId);
    }

    /**
     * My記事登録した記事の一覧を取得するメソッド
     * 各記事は記事ID、タイトル、作成・更新日時、状態、各カウント、タグリスト、記事作成者のID・名前・写真URL　を持つ
     *
     * @param userId　取得したいユーザーID
     * @return　My記事登録した記事の一覧
     */
    public List<Article> getMyArticlesByUserId(Integer userId){
        return myArticleMapper.getMyArticlesByUserId(userId);
    }
}
