package com.qiitabuilder.service;

import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.mapper.ArticleMapper;
import com.qiitabuilder.mapper.FeedbackMapper;
import com.qiitabuilder.security.SimpleLoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Transactional
public class FeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;
    @Autowired
    private ArticleMapper articleMapper;

    public Feedback postFeedback(Feedback feedback) {
        // ログイン中のユーザ情報をセット
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User();
        user.setUserId(loginUser.getUser().getUserId());
        feedback.setPostedUser(user);

        // 削除フラグが0か1でない場合はBadRequestを返す
        if (!(feedback.getDeleteFlag() == 0 || feedback.getDeleteFlag() == 1)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 記事IDが存在しない場合はConflictを返す
        if (Objects.isNull(articleMapper.load(feedback.getArticleId()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        // 現在時刻をセット
        feedback.setCreatedAt(LocalDateTime.now());

        feedbackMapper.insert(feedback);

        return feedback;
    }

    public Feedback updateFeedback(Feedback feedback) {
        // ログイン中のユーザ情報をセット
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User();
        user.setUserId(loginUser.getUser().getUserId());
        feedback.setPostedUser(user);

        // 作成日時の取得
        Feedback inserted = feedbackMapper.load(feedback.getFeedbackId());

        // 削除フラグが0か1でない場合はBadRequestを返す
        if (!(feedback.getDeleteFlag() == 0 || feedback.getDeleteFlag() == 1)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // フィードバックID, 記事IDが存在しない場合はConflictを返す
        if (Objects.isNull(inserted)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } else if (Objects.isNull(articleMapper.load(feedback.getArticleId()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        feedback.setCreatedAt(inserted.getCreatedAt());

        // 現在時刻をセット
        feedback.setUpdatedAt(LocalDateTime.now());

        feedbackMapper.update(feedback);

        return feedback;
    }

}
