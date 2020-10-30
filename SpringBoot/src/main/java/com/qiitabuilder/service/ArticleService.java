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
                .forEach(tag -> tag.setTagId(tagMapper.insertTag(tag)));

        // articleId で 更新、追加を分岐
        if (isNull(article.getArticleId())) {
            //裁判されたarticleIdをセット
            article.setArticleId(articleMapper.insertArticle(article));

            //articles_tags_relationsに情報をInsert
            tagMapper.insertArticleTag(article);

        } else {
            //入力された記事を更新
            articleMapper.updateArticle(article);

            //DBに存在するarticleが持つtagsのtag_idのlist
            List<Integer> tagIdsInDB = tagMapper.findAll().stream().map(Tag::getTagId).collect(Collectors.toList());

            //DBに存在しないIDのtagのList
            List<Tag> tagsNotExistInDB = article
                    .getTags()
                    .stream()
                    .filter(tag -> tagIdsInDB.contains(tag.getTagId()))
                    .collect(Collectors.toList());

            //DBに存在するが、入力されたタグに存在しないtagのList
            List<Tag> tagForDelete = article
                    .getTags()
                    .stream()
                    .filter(tag -> !tagIdsInDB.contains(tag.getTagId()))
                    .collect(Collectors.toList());



        }
    }
}
