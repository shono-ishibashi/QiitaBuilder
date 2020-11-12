package com.qiitabuilder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QiitaConfiguration {
    /**
     * Qiita設定ID
     */
    private Integer qiitaConfigurationId;

    /**
     * DB UserId
     */
    private Integer userId;

    /**
     * OAuth認証時の本人確認UID
     */
    private String state;

    /**
     * Qiita API連携用コード
     */
    private String code;
}
