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
    @Min(0)
    @Max(100)
    private String searchWord;
    @NotNull
    @Min(1)
    private Integer toggleSearchWord;
    @Min(0)
    @Max(5)
    private List<Integer> searchTag;
    @NotNull
    private Integer pageSize;
    @NotNull
    private Integer currentPage;
    private Integer offset;
    private String sort;
    private List<Integer> articlesIdList;
    private List<Integer> stateFlagList;
}
