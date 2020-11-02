package com.qiitabuilder.service;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.form.SearchArticleForm;
import com.qiitabuilder.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 条件に合った記事一覧を取得するメソッド
     *
     * @param searchArticleForm
     * @return
     */
    public List<Article> fetchArticle(SearchArticleForm searchArticleForm){

//        sortNumの値ごとに並び替える条件を設定
        if(searchArticleForm.getSortNum()==0){
            searchArticleForm.setSort("createdAt");
        }else if(searchArticleForm.getSortNum()==1){
            searchArticleForm.setSort("updatedAt");
        }else if(searchArticleForm.getSortNum()==2){
            searchArticleForm.setSort("recommendCnt");
        }else if(searchArticleForm.getSortNum()==3){
            searchArticleForm.setSort("myCnt");
        }
//        表示ページ数、現在ページ数を元にoffsetの値を定義
        if(searchArticleForm.getCurrentPage()==1){
            searchArticleForm.setOffset(0);
        }else{
            Integer offset=(searchArticleForm.getPageSize()*(searchArticleForm.getCurrentPage()-1)+1);
            searchArticleForm.setOffset(offset);
        }

        return articleMapper.searchArticles(searchArticleForm);
    }

    /**
     *　総ページ数を取得する
     *
     * @param searchArticleForm
     * @return
     */
    public Integer getTotalPage(SearchArticleForm searchArticleForm){
        return articleMapper.getTotalPage(searchArticleForm);
    }
}
