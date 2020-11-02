package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Recommend;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecommendMapper {

    /**
     * Recommendを新規作成する
     * @param recommend
     */
    void insert(Recommend recommend);

    /**
     * 自動採番されたプライマリーキーの値を取得する
     * @return 自動採番の値
     */
    Integer getAutoIncrementKey();

}
