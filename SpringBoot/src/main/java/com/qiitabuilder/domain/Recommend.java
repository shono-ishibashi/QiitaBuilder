package com.qiitabuilder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recommend {
    private Integer recommendId;
    private Integer articleId;
    private Integer postedUserId;
    private Integer recommendUserId;
}
