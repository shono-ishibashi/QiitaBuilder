package com.qiitabuilder.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class SearchArticleForm {
    @NotNull
    @Min(0)
    @Max(3)
    private Integer sortNum;
    @Min(0)
    @Max(1)
    private Integer period;
    private String searchWord;
    private List<Integer> searchTag;
    @NotNull
    private Integer pageSize;
    @NotNull
    private Integer currentPage;
    private Integer offset;
    private String sort;
}
