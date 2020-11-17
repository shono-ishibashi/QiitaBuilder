package com.qiitabuilder.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExistArticleForm {

    @NotNull
    @Min(0)
    private Integer articleId;

    @NotNull
    @Min(0)
    private Integer userId;
}
