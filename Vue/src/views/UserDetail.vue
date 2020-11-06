<template>
  <v-container>
    <v-row style="height: 150px;" justify="center">
      <v-col cols=5>{{ userDetail.photoUrl }}</v-col>
      <v-col cols=5>
        <v-data-table
            :headers="tagsHeaders"
            :items="userDetail.usedTags"
            style="background-color: lightgray"
            hide-default-footer
            disable-pagination></v-data-table>
      </v-col>

      <v-col cols=5>{{ userDetail.displayName }}</v-col>
      <v-col cols=4>
        <v-btn color="secondary" elevation="2">Qiita連携</v-btn>
      </v-col>

      <v-col cols=4>
        <v-card>
          <v-layout justify-center>
            Qiita投稿数 / Builder投稿数<br></v-layout>
          <v-layout justify-center>
            {{ userDetail.postedArticleCount }} / {{ postedArticles.length }}
          </v-layout>
        </v-card>
      </v-col>
      <v-col cols=4>
        <v-card>
          <v-layout justify-center>FB数</v-layout>
          <v-layout justify-center>
            {{ userDetail.feedbackCount }}
          </v-layout>
        </v-card>
      </v-col>
      <v-col cols=4>
        <v-card>
          <v-layout justify-center>総獲得推奨数</v-layout>
          <v-layout justify-center>
            {{ userDetail.qiitaRecommendedAllCount }}
          </v-layout>
        </v-card>
      </v-col>

      <v-col cols="2">
        <v-btn color="secondary" @click="changeList(1)">投稿記事</v-btn>
      </v-col>
      <v-col cols="2">
        <v-btn color="secondary" @click="changeList(2)">FB記事</v-btn>
      </v-col>
      <v-col cols="2">
        <v-btn color="secondary" @click="changeList(3)">My記事</v-btn>
      </v-col>
      <v-col cols="6"></v-col>

      <v-col cols="10">
        <ArticleCard v-for="(article,index) in articles" :key="article.articleId" :article="article"
                     :is="component" :index="index">
        </ArticleCard>
      </v-col>

    </v-row>

  </v-container>
</template>

<script>
import {mapState, mapActions, mapGetters} from "vuex"
import ArticleCard from "../components/ArticleCard";

export default {
  name: "userDetail",
  components: {ArticleCard},
  data() {
    return {
      tagsHeaders: [
        {text: "タグ名", value: "tagName"},
        {text: "使用回数", value: "usedTagCount"}
      ],
      articles: [],
      component: null,
    };
  },
  computed: {
    ...mapGetters("user", ["userId"]),
    ...mapState("user", ["userDetail", "postedArticles", "feedbackArticles", "myArticles"])
  },
  methods: {
    /**
     * 表示したい記事に対応する数値を渡して、表示する記事一覧を変更する
     * @param listNum (1:投稿記事), (2:FB記事), (3:My記事)
     */
    changeList(listNum) {
      if (listNum == 1) {
        this.articles = [];
        for (var counter = 0; counter < this.postedArticles.length; counter++) {
          this.$set(this.articles, counter, this.postedArticles[counter]);
        }
      } else if (listNum == 2) {
        this.articles = [];
        for (counter = 0; counter < this.feedbackArticles.length; counter++) {
          this.$set(this.articles, counter, this.feedbackArticles[counter]);
        }
      } else if (listNum == 3) {
        this.articles = [];
        for (counter = 0; counter < this.myArticles.length; counter++) {
          this.$set(this.articles, counter, this.myArticles[counter]);
        }
      } else {
        this.articles = [];
        for (counter = 0; counter < this.postedArticles.length; counter++) {
          this.$set(this.articles, counter, this.postedArticles[counter]);
        }
      }
    },
    ...mapActions("user", ["fetchUserDetail", "fetchPostedArticles", "fetchFeedbackArticles", "fetchMyArticles"])
  },
  async created() {
    await this.fetchUserDetail(this.$route.query.userId);
    //DBに存在しないユーザーIDが渡された場合記事一覧に戻る
    if (isNaN(this.userId)) {
      this.$router.push({path: 'articleList'})
    }

    this.fetchFeedbackArticles(this.$route.query.userId);
    this.fetchMyArticles(this.$route.query.userId);

    await this.fetchPostedArticles(this.$route.query.userId);
    //投稿記事が0件じゃない場合ArticleCardコンポーネント表示
    if (this.postedArticles.length != 0) {
      for (var counter = 0; counter < this.postedArticles.length; counter++) {
        this.$set(this.articles, counter, this.postedArticles[counter]);
      }
      this.component = ArticleCard;
    }
    //else で記事が無いとき投稿画面に遷移などのボタン用コンポーネント用意してもいいかも
  },
  beforeRouteEnter(to, from, next) {
    //URLのparam(userId)に数値以外が入力された際に記事一覧に戻る
    if (!isNaN(to.query.userId)) {
      next();
    } else {
      next({path: 'articleList'})
    }
  }
}
</script>