package com.qiitabuilder.service;

import com.qiitabuilder.domain.Article;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class ArticleService {

    public void saveArticle(Article article) {

        // articleId で 更新、追加を分岐
        if ( isNull(article.getArticleId()) ){

        } else {

        }

        //入力されたTagがDBに存在しなければ、DBへ追加


    }
}
