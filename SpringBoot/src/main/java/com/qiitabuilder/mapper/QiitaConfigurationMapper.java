package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.QiitaConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QiitaConfigurationMapper{

    String getCode(Integer userId);
    String getState(String state);
    String getStateByUserId(Integer userId);
    void insertQiitaConfiguration(QiitaConfiguration qiitaConfiguration);
    void updateQiitaConfigurationCode(QiitaConfiguration qiitaConfiguration);

    void deleteByUserId(Integer userId);
}
