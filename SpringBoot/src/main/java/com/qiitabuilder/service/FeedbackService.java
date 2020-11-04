package com.qiitabuilder.service;

import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.mapper.FeedbackMapper;
import com.qiitabuilder.security.SimpleLoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class FeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;

    public Feedback postFeedback(Feedback feedback) {
        // ログイン中のユーザ情報をセット
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User();
        user.setUserId(loginUser.getUser().getUserId());
        feedback.setPostedUser(user);

        // deleteFlagに0:未削除をセット
        feedback.setDeleteFlag(0);

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
        LocalDateTime createdAt = feedbackMapper.load(feedback.getFeedbackId()).getCreatedAt();
        feedback.setCreatedAt(createdAt);

        // 現在時刻をセット
        feedback.setUpdatedAt(LocalDateTime.now());

        feedbackMapper.update(feedback);

        return feedback;
    }

}
