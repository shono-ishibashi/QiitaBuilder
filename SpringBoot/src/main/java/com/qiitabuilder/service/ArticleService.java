package com.qiitabuilder.service;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.mapper.FeedbackMapper;
import com.qiitabuilder.mapper.MyArticleMapper;
import com.qiitabuilder.form.SearchArticleForm;
import com.qiitabuilder.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.mapper.TagMapper;
import com.qiitabuilder.security.SimpleLoginUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import java.util.Objects;
import java.util.stream.Collectors;


import static java.util.Objects.isNull;


@Service
@Transactional
public class ArticleService {
    @Autowired
    private FeedbackMapper feedbackMapper;
    @Autowired
    private MyArticleMapper myArticleMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagMapper tagMapper;

    /**
     * 条件に合った記事一覧を取得するメソッド
     *
     * @param searchArticleForm
     * @return
     */
    public List<Article> searchArticles(SearchArticleForm searchArticleForm) {

        SearchArticleForm processedSearchArticleForm = searchCriteriaProcessing(searchArticleForm);

//        検索条件に一致する記事をlistで取得
        List<Integer> articlesIdList = articleMapper.searchArticlesId(processedSearchArticleForm);
        if (articlesIdList.size() != 0) {
            searchArticleForm.setArticlesIdList(articlesIdList);
        } else {
            return null;
        }

        List<Article> articles = articleMapper.searchArticles(searchArticleForm);

//        qiita推奨数、my記事登録数がnullのものを0に置き換える
        articles.stream().filter(article -> article.getQiitaRecommendPoint() == null).forEach(article -> article.setQiitaRecommendPoint(0));
        articles.stream().filter(article -> article.getRegisteredMyArticleCount() == null).forEach(article -> article.setRegisteredMyArticleCount(0));
        articles.stream().filter(article -> article.getFeedbackCount() == null).forEach(article -> article.setFeedbackCount(0));

        return articles;
    }

    /**
     * 総ページ数を取得する
     *
     * @param searchArticleForm
     * @return
     */
    public Integer getTotalPage(SearchArticleForm searchArticleForm) {
        SearchArticleForm processedSearchArticleForm = searchCriteriaProcessing(searchArticleForm);
        Integer pageSize = searchArticleForm.getPageSize();
        processedSearchArticleForm.setPageSize(0);
        List<Integer> articlesIdList = articleMapper.searchArticlesId(processedSearchArticleForm);
        Integer articleNumber = articlesIdList.size();

//        総ページ数を計算する
        int totalPage = articleNumber / pageSize;
        if ((articleNumber % pageSize) != 0) {
            totalPage += 1;
        } else if (totalPage == 0) {
            totalPage = 1;
        }
        return totalPage;
    }

    /**
     * 検索条件(sortNum->sort,currentPage->offset)を加工するメソッド
     *
     * @param searchArticleForm
     * @return
     */
    public SearchArticleForm searchCriteriaProcessing(SearchArticleForm searchArticleForm) {
//        sortNumの値ごとに並び替える条件を設定
        if (searchArticleForm.getSortNum() == 0) {
            searchArticleForm.setSort("createdAt");
        } else if (searchArticleForm.getSortNum() == 1) {
            searchArticleForm.setSort("updatedAt");
        } else if (searchArticleForm.getSortNum() == 2) {
            searchArticleForm.setSort("recommendCnt");
        } else if (searchArticleForm.getSortNum() == 3) {
            searchArticleForm.setSort("myCnt");
        }

//        表示ページ数、現在ページ数を元にoffsetの値を定義
//        pageSizeを0にすると全件取得
        if (searchArticleForm.getCurrentPage() == 1) {
            searchArticleForm.setOffset(0);
        } else {
            Integer offset = (searchArticleForm.getPageSize() * (searchArticleForm.getCurrentPage() - 1));
            searchArticleForm.setOffset(offset);
        }
        if (Objects.nonNull(searchArticleForm.getSearchTag())) {
            searchArticleForm.setTagLength(searchArticleForm.getSearchTag().size());
        }
        return searchArticleForm;
    }

    /**
     * 記事をDBに更新もしくは追加するメソッド
     *
     * @param article 更新もしくは追加するデータ
     */
    @Transactional
    public Article saveArticle(Article article) {

        //ログイン中のユーザ情報をセット
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setPostedUser(loginUser.getUser());

        //idがないtagをインサートして、自動裁判されたIdを取得しセット
        article.getTags().stream()
                .filter(tag -> isNull(tag.getTagId()))
                .forEach(tag -> tagMapper.insertTag(tag));


        // articleId で 更新、追加を分岐
        if (isNull(article.getArticleId())) {
            //裁判されたarticleIdをセット
            articleMapper.insertArticle(article);

            //articles_tags_relationsに情報をInsert
            article.getTags().forEach(tag -> {
                tagMapper.insertArticleTag(
                        loginUser.getUser().getUserId(),
                        tag.getTagId(),
                        article.getArticleId()
                );
            });

        } else {

            article.setQiitaArticleId(articleMapper.getQiitaArticleId(article.getArticleId()));

            //現在の記事
            Article currentArticle = articleMapper.load(article.getArticleId());
            System.out.println(currentArticle);

            // versionが異なる場合(排他制御)はConflictを返す
            System.out.println(currentArticle.getArticleVersion());
            System.out.println(article.getArticleVersion());
            if(!Objects.equals(currentArticle.getArticleVersion(),article.getArticleVersion())){
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }

            //入力された記事を更新
            //更新前の状態が下書き記事かどうか
            if (Objects.equals(currentArticle.getStateFlag(), 0)) {
                articleMapper.updateDraftArticle(article);
            } else {
                articleMapper.updateArticle(article);
            }

            //入力されたtagのIDのList
            List<Integer> tagIdsInPostedArticle
                    = article.getTags().stream().map(Tag::getTagId).collect(Collectors.toList());

            //DBに存在するarticleが持つtagsのtag_idのlist
            List<Integer> tagIdsInDB
                    = tagMapper.findAllArticleTag(loginUser.getUser().getUserId(), article.getArticleId());


            // -------------------insert-------------------t
            //DBに存在しない記事が持つタグを追加
            article.getTags()
                    .stream()
                    //DBに存在しているかを判別
                    .filter(tag -> !tagIdsInDB.contains(tag.getTagId()))
                    .map(Tag::getTagId)
                    .forEach(tagId ->
                            tagMapper.insertArticleTag(
                                    loginUser.getUser().getUserId(),
                                    tagId,
                                    article.getArticleId()
                            )
                    );

            // -------------------delete-------------------t
            //DBに存在するが、入力されたタグに存在しないtagのList
            //Deleteするために使用
            tagIdsInDB.stream()
                    .filter(tagId -> !tagIdsInPostedArticle.contains(tagId))
                    .forEach(tagId ->
                            tagMapper.deleteArticleTag(
                                    loginUser.getUser().getUserId(),
                                    tagId,
                                    article.getArticleId()
                            )
                    );
        }
        return article;
    }

    public Article getArticle(Integer articleId) {
        Article article = articleMapper.getArticleAndFeedback(articleId);
        if (Objects.nonNull(article) && Objects.isNull(article.getQiitaRecommendPoint())) {
            article.setQiitaRecommendPoint(0);
        }
        return article;
    }

    /**
     * articleidとuserIdで記事が存在するかを確かめるメソッド
     *
     * @param articleId
     * @param userId
     * @return
     */

    public Integer findByArticleIdAndUserId(Integer articleId, Integer userId) {
        Integer findArticleId = articleMapper.findByArticleIdAndUserId(articleId, userId);
        if (Objects.nonNull(findArticleId)) {
            return findArticleId;
        } else {
            return null;
        }
    }

    /**
     * ユーザーがFBした記事の一覧を取得するメソッド
     * 各記事は記事ID、タイトル、作成・更新日時、状態、各カウント、タグリスト、記事作成者のID・名前・写真URL　を持つ
     *
     * @param userId 　取得したいユーザーID
     * @return　フィードバックした記事の一覧
     */
    public List<Article> getFeedbackedArticlesByUserId(Integer userId) {
        return feedbackMapper.getFeedbackedArticlesByUserId(userId);
    }

    /**
     * My記事登録した記事の一覧を取得するメソッド
     * 各記事は記事ID、タイトル、作成・更新日時、状態、各カウント、タグリスト、記事作成者のID・名前・写真URL　を持つ
     *
     * @param userId 　取得したいユーザーID
     * @return　My記事登録した記事の一覧
     */
    public List<Article> getMyArticlesByUserId(Integer userId) {
        return myArticleMapper.getMyArticlesByUserId(userId);
    }

    /**
     * userIdからそのユーザーが作成した記事一覧を取得する
     *
     * @param userId
     * @return List<Article> (ユーザーが投稿した記事一覧)
     */
    public List<Article> getArticlesByUserId(Integer userId) {
        return articleMapper.getArticlesByUserId(userId);
    }
}
