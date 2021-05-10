package com.qiitabuilder.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
@Builder
public class SearchArticleForm {
    @NotNull
    @Min(0)
    @Max(3)
    private Integer sortNum;

    @Min(0)
    @Max(1)
    private Integer period;

    private String searchWord;

    @NotNull
    @Min(0)
    @Max(1)
    private Integer toggleSearchWord;

    private List<Integer> searchTag;

    @NotNull
    private Integer pageSize;

    @NotNull
    private Integer currentPage;

    private Integer offset;

    private String sort;

    private Integer tagLength;

    private List<Integer> articlesIdList;

    private List<Integer> stateFlagList;
}
