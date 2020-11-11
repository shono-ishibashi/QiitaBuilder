package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.QiitaConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QiitaConfigurationMapper{

    String getCodeByUserId(Integer userId);
    String getStateByUserId(Integer userId);
    String getTokenByUserId(Integer userId);

    void insertQiitaConfiguration(QiitaConfiguration qiitaConfiguration);

    void deleteByUserId(Integer userId);

    void updateQiitaConfigurationCode(QiitaConfiguration qiitaConfiguration);
    void updateState(QiitaConfiguration qiitaConfiguration);
    void saveToken(String token, Integer userId);
}
