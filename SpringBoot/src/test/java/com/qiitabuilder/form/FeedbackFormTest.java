package com.qiitabuilder.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FeedbackFormTest {

    @Autowired
    private Validator validator;

    private FeedbackForm feedbackForm = new FeedbackForm();

    @BeforeEach
    public void setUpForTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        feedbackForm.setFeedbackId(1);
        feedbackForm.setArticleId(1);
        feedbackForm.setContent("content");
        feedbackForm.setFeedbackVersion(1);
        feedbackForm.setDeleteFlag(0);
    }

    @Test
    void success() {
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(0, violations.size());
    }

    //feedbackId
    @Test
    void feedbackId_正常系_min_境界値() {
        feedbackForm.setFeedbackId(0);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(0, violations.size());
    }

    @Test
    void feedbackId_異常系_min_境界値() {
        feedbackForm.setFeedbackId(-1);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    @Test
    void feedbackId_異常系_min() {
        feedbackForm.setFeedbackId(-10);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    //articleId
    @Test
    void articleId_正常系_min_境界値() {
        feedbackForm.setArticleId(0);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(0, violations.size());
    }

    @Test
    void articleId_異常系_notnull() {
        feedbackForm.setArticleId(null);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof NotNull);
        }
    }

    @Test
    void articleId_異常系_min_境界値() {
        feedbackForm.setArticleId(-1);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    @Test
    void articleId_異常系_min() {
        feedbackForm.setArticleId(-10);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    //content
    @Test
    void content_正常系_size_境界値() {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < 4000; i++) {
            str.append("あいうえお");
        }
        feedbackForm.setContent(str.toString());
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(0, violations.size());
    }

    @Test
    void content_正常系_notblank_境界値() {
        feedbackForm.setContent("あ");
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(0, violations.size());
    }

    @Test
    void content_異常系_notblank() {
        feedbackForm.setContent("");
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof NotBlank);
        }
    }

    @Test
    void content_異常系_size_境界値() {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < 4000; i++) {
            str.append("あいうえお");
        }
        str.append("か");
        feedbackForm.setContent(str.toString());
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Size);
        }
    }

    @Test
    void content_異常系_size() {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < 4000; i++) {
            str.append("あいうえお");
        }
        str.append("かきくけこ");
        feedbackForm.setContent(str.toString());
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Size);
        }
    }

    //deleteFlag
    @Test
    void deleteFlag_正常系_max_境界値() {
        feedbackForm.setDeleteFlag(1);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(0, violations.size());
    }

    @Test
    void deleteFlag_異常系_max_境界値() {
        feedbackForm.setDeleteFlag(2);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Max);
        }
    }

    @Test
    void deleteFlag_異常系_max() {
        feedbackForm.setDeleteFlag(10);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Max);
        }
    }

    @Test
    void deleteFlag_異常系_min_境界値() {
        feedbackForm.setDeleteFlag(-1);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    @Test
    void deleteFlag_異常系_min() {
        feedbackForm.setDeleteFlag(-10);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    // feedbackVersion
    @Test
    void feedbackVersion_正常系_min_境界値() {
        feedbackForm.setFeedbackVersion(1);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(0, violations.size());
    }

    @Test
    void feedbackVersion_異常系_notnull() {
        feedbackForm.setFeedbackVersion(null);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof NotNull);
        }
    }

    @Test
    void feedbackVersion_異常系_min_境界値() {
        feedbackForm.setFeedbackVersion(0);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    @Test
    void feedbackVersion_異常系_min() {
        feedbackForm.setFeedbackVersion(-10);
        Set<ConstraintViolation<FeedbackForm>> violations = validator.validate(feedbackForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<FeedbackForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }}