package com.qiitabuilder.controller;

import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    public Feedback postFeedback(Feedback feedback){
        return null;
    }

    public Feedback updateFeedback(Feedback feedback){
        return null;
    }

    public void deleteFeedback(Feedback feedback){

    }
}
