package com.qiitabuilder.service;

import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.mapper.FeedbackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;

    public Feedback postFeedback(Feedback feedback){
        feedbackMapper.insert(feedback);
        return null;
    }

    public Feedback updateFeedback(Feedback feedback){
        return null;
    }

}
