package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.domain.User;
import com.qiitabuilder.domain.RankingUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FeedbackMapper {
//    void insert(Feedback feedback);
//    Integer getAutoIncrementKey();
//    void update(Feedback feedback);

    /**
     * FBした数ランキング順でユーザー情報を取得する.
     *
     * @return ランキングユーザー一覧
     */
    List<RankingUser> getFBRank();

    /**
     * FBした記事ID一覧を取得する.
     *
     * @param userId ユーザーID
     * @return 記事ID一覧
     */
    List<Integer> getArticleIdByUserId(Integer userId);

//    /**
//     * FBした数を取得する.
//     *
//     * @param userId ユーザーID
//     * @return FBした数
//     */
//    Integer getCount(Integer userId);
    List<Article> getFeedbackedArticlesByUserId(@Param("userId") Integer userId);
}
