package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.form.FeedbackForm;
import com.qiitabuilder.service.ArticleService;
import com.qiitabuilder.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping(value = "/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ArticleService articleService;

    /////////////////////////////
    //// POST
    /////////////////////////////

    /**
     * フィードバックを登録する
     * <p>
     * HTTP ステータス
     * OK
     * BadRequest 入力値エラーの場合
     * Conflict 記事IDが存在しない場合
     *
     * @param form
     * @return
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Feedback postFeedback(@Validated @RequestBody FeedbackForm form, BindingResult result) {
        // 入力値エラーの場合はBadRequestを返す
        if (result.hasErrors() || form.getContent().matches("^[　]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 記事IDが存在しない場合はConflictを返す
        if (Objects.isNull(articleService.getArticle(form.getArticleId()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Feedback feedback = new Feedback();
        feedback.setArticleId(form.getArticleId());
        feedback.setContent(form.getContent());
        feedback.setDeleteFlag(form.getDeleteFlag());

        return feedbackService.postFeedback(feedback);
    }


    /////////////////////////////
    //// PUT
    /////////////////////////////

    /**
     * フィードバックを更新する
     * <p>
     * HTTP ステータス
     * OK
     * BadRequest 入力値エラーの場合
     * Conflict 記事IDが存在しない場合 & フィードバックIDが存在しない場合 & versionが異なる場合(排他制御)
     *
     * @param form
     * @return
     */
    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Feedback updateFeedback(@Validated @RequestBody FeedbackForm form, BindingResult result) {
        // 入力値エラーの場合はBadRequestを返す
        if (result.hasErrors() || Objects.isNull(form.getFeedbackId()) || Objects.isNull(form.getFeedbackVersion()) || form.getContent().matches("^[　]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // フィードバックIDが存在しない場合はConflictを返す
        Feedback inserted = feedbackService.fetchFeedback(form.getFeedbackId());
        if (Objects.isNull(inserted)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        // 記事IDが存在しない場合はConflictを返す
        if (Objects.isNull(articleService.getArticle(form.getArticleId()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Feedback feedback = new Feedback();
        feedback.setFeedbackId(form.getFeedbackId());
        feedback.setArticleId(form.getArticleId());
        feedback.setContent(form.getContent());
        feedback.setFeedbackVersion(form.getFeedbackVersion());
        feedback.setDeleteFlag(form.getDeleteFlag());

        return feedbackService.updateFeedback(feedback);
    }

}
