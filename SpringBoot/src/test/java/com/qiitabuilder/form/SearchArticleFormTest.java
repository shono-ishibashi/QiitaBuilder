package com.qiitabuilder.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SearchArticleFormTest {

    @Autowired
    private Validator validator;

    private SearchArticleForm searchArticleForm = new SearchArticleForm();

    @BeforeEach
    public void setUpForTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        searchArticleForm.setSortNum(1);
        searchArticleForm.setPeriod(1);
        searchArticleForm.setToggleSearchWord(1);
        searchArticleForm.setPageSize(1);
        searchArticleForm.setCurrentPage(1);
    }

    @Test
    void success() {
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(0, violations.size());
    }

    //sortNum
    @Test
    void sortNum_異常系_notnull() {
        searchArticleForm.setSortNum(null);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof NotNull);
        }
    }

    @Test
    void sortNum_正常系_min_境界値() {
        searchArticleForm.setSortNum(1);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(0, violations.size());
    }

    @Test
    void sortNum_正常系_max_境界値() {
        searchArticleForm.setSortNum(3);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(0, violations.size());
    }

    @Test
    void sortNum_異常系_min_境界値() {
        searchArticleForm.setSortNum(-1);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    @Test
    void sortNum_異常系_min() {
        searchArticleForm.setSortNum(-10);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    @Test
    void sortNum_異常系_max_境界値() {
        searchArticleForm.setSortNum(4);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Max);
        }
    }

    @Test
    void sortNum_異常系_max() {
        searchArticleForm.setSortNum(10);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Max);
        }
    }

    //period
    @Test
    void period_正常系() {
        searchArticleForm.setPeriod(0);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(0, violations.size());
    }

    @Test
    void period_異常系_min_境界値() {
        searchArticleForm.setPeriod(-1);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    @Test
    void period_異常系_min() {
        searchArticleForm.setPeriod(-10);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    @Test
    void period_異常系_max_境界値() {
        searchArticleForm.setPeriod(2);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Max);
        }
    }

    @Test
    void period_異常系_max() {
        searchArticleForm.setPeriod(5);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Max);
        }
    }

    //toggleSearchWord
    @Test
    void toggleSearchWord_異常系_notnull() {
        searchArticleForm.setToggleSearchWord(null);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof NotNull);
        }
    }

    @Test
    void toggleSearchWord_正常系() {
        searchArticleForm.setToggleSearchWord(0);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(0, violations.size());
    }

    @Test
    void toggleSearchWord_異常系_min_境界値() {
        searchArticleForm.setToggleSearchWord(-1);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    @Test
    void toggleSearchWord_異常系_min() {
        searchArticleForm.setToggleSearchWord(-10);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Min);
        }
    }

    @Test
    void toggleSearchWord_異常系_max_境界値() {
        searchArticleForm.setToggleSearchWord(2);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Max);
        }
    }

    @Test
    void toggleSearchWord_異常系_max() {
        searchArticleForm.setToggleSearchWord(5);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof Max);
        }
    }

    //pageSize
    @Test
    void pageSize_異常系_notnull() {
        searchArticleForm.setPageSize(null);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof NotNull);
        }
    }

    //currentPage
    @Test
    void currentPage_異常系_notnull() {
        searchArticleForm.setCurrentPage(null);
        Set<ConstraintViolation<SearchArticleForm>> violations = validator.validate(searchArticleForm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<SearchArticleForm> v : violations) {
            assertTrue(v.getConstraintDescriptor().getAnnotation() instanceof NotNull);
        }
    }
}