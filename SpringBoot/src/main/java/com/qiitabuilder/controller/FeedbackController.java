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

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Feedback postFeedback(@RequestBody Feedback feedback) {
        return feedbackService.postFeedback(feedback);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Feedback updateFeedback(@RequestBody Feedback feedback) {
        return feedbackService.updateFeedback(feedback);
    }

    public void deleteFeedback(Feedback feedback) {

    }
}
