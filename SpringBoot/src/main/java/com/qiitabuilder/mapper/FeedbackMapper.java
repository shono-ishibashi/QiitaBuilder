package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.Article;
import com.qiitabuilder.domain.Feedback;
import com.qiitabuilder.domain.RankingUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FeedbackMapper {

    /**
     * フィードバックを投稿する.
     * ※feedbackIdプロパティに自動生成キーがセットされる.
     *
     * @param feedback 記事情報
     * @return 挿入件数
     */
    Integer insert(Feedback feedback);

    /**
     * フィードバックを編集する.
     *
     * @param feedback 記事情報
     * @return update件数
     */
    Integer update(Feedback feedback);

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
    List<Integer> getArticleIdListByUserId(Integer userId);

    /**
     * FBIDが一致するフィードバック情報を返す
     *
     * @param feedbackId FBID
     * @return フィードバック情報
     */
    Feedback load(Integer feedbackId);

    /**
     * FBした記事一覧を取得する
     *
     * @param userId ユーザーID
     * @return FBした記事一覧
     */
    List<Article> getFeedbackedArticlesByUserId(@Param("userId") Integer userId);
}
