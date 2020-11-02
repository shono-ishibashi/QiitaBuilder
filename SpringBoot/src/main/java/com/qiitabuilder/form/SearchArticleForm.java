package com.qiitabuilder.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchArticleForm {
    private Integer sortNum;
    private Integer period;
    private String searchWord;
    private List<Integer> searchTag;
    private Integer pageSize;
    private Integer currentPage;
    private Integer offset;
    private String sort;
}
