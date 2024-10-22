package com.qiitabuilder.mapper;

import com.qiitabuilder.domain.RankingUser;
import com.qiitabuilder.domain.Recommend;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RecommendMapper {

    /**
     * Qiita推薦累計数ランキング順でユーザー情報を取得する.
     *
     * @return ランキングユーザー一覧
     */
    List<RankingUser> getQiitaRecommendedRank();

    /**
     * 最もQiita推薦数の多い記事IDを取得する.
     *
     * @param userId ユーザーID
     * @param rankItemId 　1:FBした数ランキングの場合、2:投稿数及びQiita推薦累計数ランキングの場合
     * @return 記事ID一覧
     */
    List<Integer> getMostRecommendedArticleId(Integer userId, Integer rankItemId);

    /**
     * 検索条件に一致するレコードを取得する
     *
     * @param articleId       記事ID
     * @param recommendUserId Qiita推薦したユーザーID
     * @return 検索条件に一致したレコード
     */
    Recommend findByArticleIdAndRecommendUserId(Integer articleId, Integer recommendUserId);

    /**
     * loadメソッド
     * @param recommendId
     * @return
     */
    Recommend load(Integer recommendId);

    /**
     * Recommendを新規作成する
     *
     * @param recommend Qiita推薦情報
     */
    void insert(Recommend recommend);

    /**
     * 自動採番されたプライマリーキーの値を取得する
     *
     * @return 自動採番の値
     */
    Integer getAutoIncrementKey();

    /**
     * Recommendを削除する
     *
     * @param recommendId Qiita推薦情報
     * @return テーブルに１レコード以上の削除があった場合はtrue、１件も無かった場合はfalseを返す
     */
    boolean delete(Integer recommendId);
}
