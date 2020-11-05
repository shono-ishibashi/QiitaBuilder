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

    public Feedback fetchFeedback(Integer feedbackId) {
        return feedbackMapper.load(feedbackId);
    }

    public Feedback postFeedback(Feedback feedback) {
        // ログイン中のユーザ情報をセット
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        feedback.setPostedUser(loginUser.getUser());

        // 現在時刻をセット
        feedback.setCreatedAt(LocalDateTime.now());

        feedbackMapper.insert(feedback);

        return fetchFeedback(feedback.getFeedbackId());
    }

    public Feedback updateFeedback(Feedback feedback) {
        // ログイン中のユーザ情報をセット
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        feedback.setPostedUser(loginUser.getUser());

        // 現在時刻をセット
        feedback.setUpdatedAt(LocalDateTime.now());

        feedbackMapper.update(feedback);

        return fetchFeedback(feedback.getFeedbackId());
    }

}
