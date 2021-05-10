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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExistArticleFormTest {

    @Autowired
    private Validator validator;

    private ExistArticleForm existArticleForm = new ExistArticleForm();

    @BeforeEach
    public void setUpForTest(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        existArticleForm.setArticleId(1);
        existArticleForm.setUserId(1);
    }

    @Test
    void success(){
        Set<ConstraintViolation<ExistArticleForm>> violations = validator.validate(existArticleForm);
        assertEquals(0, violations.size());
    }

    //articleId
    @Test
    void articleId_異常系_notnull(){
        existArticleForm.setArticleId(null);
        Set<ConstraintViolation<ExistArticleForm>> violations = validator.validate(existArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<ExistArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof NotNull);
        }
    }

    @Test
    void articleId_正常系_min_境界値(){
        existArticleForm.setArticleId(0);
        Set<ConstraintViolation<ExistArticleForm>> violations = validator.validate(existArticleForm);
        assertEquals(0, violations.size());
    }

    @Test
    void articleId_異常系_min_境界値(){
        existArticleForm.setArticleId(-1);
        Set<ConstraintViolation<ExistArticleForm>> violations = validator.validate(existArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<ExistArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    @Test
    void articleId_異常系_min(){
        existArticleForm.setArticleId(-10);
        Set<ConstraintViolation<ExistArticleForm>> violations = validator.validate(existArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<ExistArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    //userId
    @Test
    void userId_異常系_notnull(){
        existArticleForm.setUserId(null);
        Set<ConstraintViolation<ExistArticleForm>> violations = validator.validate(existArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<ExistArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof NotNull);
        }
    }

    @Test
    void userId_正常系_min_境界値(){
        existArticleForm.setUserId(0);
        Set<ConstraintViolation<ExistArticleForm>> violations = validator.validate(existArticleForm);
        assertEquals(0, violations.size());
    }

    @Test
    void userId_異常系_min_境界値(){
        existArticleForm.setUserId(-1);
        Set<ConstraintViolation<ExistArticleForm>> violations = validator.validate(existArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<ExistArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    @Test
    void userId_異常系_min(){
        existArticleForm.setUserId(-10);
        Set<ConstraintViolation<ExistArticleForm>> violations = validator.validate(existArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<ExistArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }
}