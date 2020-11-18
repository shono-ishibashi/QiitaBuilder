package com.qiitabuilder.service;

import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.mapper.ArticleMapper;
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
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * FBIDを基にFeedback情報を取得するメソッド
     *
     * @param feedbackId
     * @return
     */
    public Feedback fetchFeedback(Integer feedbackId) {
        return feedbackMapper.load(feedbackId);
    }

    /**
     * ログイン中のユーザー情報を取得し、FBを投稿するメソッド
     *
     * @param feedback
     * @return 生成したFeedbackId付きのフィードバック情報を返す
     */
    public Feedback postFeedback(Feedback feedback) {
        // ログイン中のユーザ情報をセット
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        feedback.setPostedUser(loginUser.getUser());

        // 現在時刻をセット
        feedback.setCreatedAt(LocalDateTime.now());

        feedbackMapper.insert(feedback);

        return fetchFeedback(feedback.getFeedbackId());
    }

    /**
     * ログイン中のユーザー情報を取得し、ユーザーが投稿済みのFBの更新を行うメソッド
     *
     * @param feedback
     * @return
     */
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
