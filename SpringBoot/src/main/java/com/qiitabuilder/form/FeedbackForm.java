package com.qiitabuilder.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackForm {

    @Min(0)
    private Integer feedbackId;

    @NotNull
    @Min(0)
    private Integer articleId;

    @NotBlank
    private String content;

    @NotNull
    @Min(0)
    @Max(1)
    private Integer deleteFlag;
}
