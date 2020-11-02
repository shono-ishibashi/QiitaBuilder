package com.qiitabuilder.service;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Tag;
import com.qiitabuilder.mapper.ArticleMapper;
import com.qiitabuilder.mapper.TagMapper;
import com.qiitabuilder.security.SimpleLoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;


@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagMapper tagMapper;

    /**
     * 記事をDBに更新もしくは追加するメソッド
     *
     * @param article 更新もしくは追加するデータ
     */
    public void saveArticle(Article article) {

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
            //入力された記事を更新
            articleMapper.updateArticle(article);

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
                    .filter(tag -> tagIdsInDB.contains(tag.getTagId()))
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
    }
}
