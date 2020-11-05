package com.qiitabuilder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QiitaConfiguration {
    private Integer qiitaConfigurationId;
    private Integer userId;
    private String state;
    private String code;
}
