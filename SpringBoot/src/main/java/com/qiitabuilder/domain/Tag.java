package com.qiitabuilder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag {
    private Integer tagId;
    private String tagName;
    private Integer usedTagCount;
}



