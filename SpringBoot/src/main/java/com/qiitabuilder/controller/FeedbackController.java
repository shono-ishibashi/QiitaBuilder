package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /////////////////////////////
    //// POST
    /////////////////////////////

    /**
     * フィードバックを登録する
     * Conflict 記事IDが存在しない場合, BadRequest 削除フラグが0か1でない場合
     *
     * @param feedback
     * @return
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Feedback postFeedback(@RequestBody Feedback feedback) {
        return feedbackService.postFeedback(feedback);
    }


    /////////////////////////////
    //// PUT
    /////////////////////////////

    /**
     * フィードバックを更新する
     * Conflict フィードバックIDが存在しない場合&記事IDが存在しない場合, BadRequest 削除フラグが0か1でない場合
     *
     * @param feedback
     * @return
     */
    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Feedback updateFeedback(@RequestBody Feedback feedback) {
        return feedbackService.updateFeedback(feedback);
    }

}
