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

      <v-col cols="12" v-if="displayListNum===1||displayListNum===11||displayListNum===12">
        <v-btn color="secondary" @click="changeList(1)">全記事</v-btn>
        <v-btn color="secondary" @click="changeList(11)">Qiita 未投稿記事</v-btn>
        <v-btn color="secondary" @click="changeList(12)">Qiita 投稿済み記事</v-btn>
      </v-col>

      <v-col cols="5">
        <v-text-field v-model="conditions.title"
                      label="記事タイトルを入力"></v-text-field>
      </v-col>
      <v-col cols="5">
        <v-autocomplete
            v-model="conditions.conditionTags"
            :items="usedTags"
            item-value="tagId"
            item-text="tagName"
            label="タグを選択"
            chips
            deletable-chips
            multiple
            small-chips
        >
        </v-autocomplete>
      </v-col>
      <v-col cols="2">
        <v-btn @click="searchWithConditions" color="secondary" elevation="2">検索</v-btn>
      </v-col>
      <v-col cols="4">
        <v-select
            :items="sortList"
            item-value="key"
            item-text="state"
            item-color="green"
            color="#5bc8ac"
            v-model="sortNum"
        >
        </v-select>
      </v-col>
      <v-col cols="10">
        <ArticleCard v-for="(article,index) in sortedArticles" :key="article.articleId" :article="article"
                     :is="component" :index="index">
        </ArticleCard>
        <v-container
            v-if="articles.length===0"
            class="no-article-field"
        >
          該当する記事がありません
        </v-container>
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
      sortList: [
        {key: 0, state: "新着順"},
        {key: 1, state: "更新順"},
        {key: 2, state: "Qiita推奨数順"},
        {key: 3, state: "My記事登録数順"}
      ],
      sortNum: 0,
      articles: [],//画面表示用記事リスト
      usedTags: [],//タグ検索オートコンプリート用リスト
      component: null,
      displayListNum: 1,//1:posted, 2:feedback, 3:my, 11:notPostedQiita, 12:postedQiita
      conditions: {
        title: "",
        conditionTags: []
      },
    };
  },
  computed: {
    sortedArticles: function () {
      var art = this.articles;
      if (this.sortNum === 0) {
        return art.sort((a, b) => {
          return (a.createdAt < b.createdAt) ? 1 : (a.createdAt > b.createdAt) ? -1 : 0;
        })
      }
      if (this.sortNum === 1) {
        return art.sort((a, b) => {
          return (a.updatedAt < b.updatedAt) ? 1 : (a.updatedAt > b.updatedAt) ? -1 : 0;
        })
      }
      if (this.sortNum === 2) {
        return art.sort((a, b) => {
          return (a.qiitaRecommendPoint < b.qiitaRecommendPoint) ? 1 : (a.qiitaRecommendPoint > b.qiitaRecommendPoint) ? -1 : 0;
        })
      }
      if (this.sortNum === 3) {
        return art.sort((a, b) => {
          return (a.registeredMyArticleCount < b.registeredMyArticleCount) ? 1 : (a.registeredMyArticleCount > b.registeredMyArticleCount) ? -1 : 0;
        })
      }
      return 0;
    },
    ...mapGetters("user", ["userId", "notPostedQiitaArticles", "postedQiitaArticles"]),
    ...mapState("user", ["userDetail", "postedArticles", "feedbackArticles", "myArticles"])
  },
  methods: {
    /**
     * 表示したい記事に対応する数値を渡して、表示する記事一覧とオートコンプリート用タグリストを変更する
     * @param listNum (1:投稿記事), (2:FB記事), (3:My記事)
     */
    changeList(listNum) {
      this.articles.length = 0;
      this.usedTags.length = 0;
      this.conditions.title = "";
      this.conditions.conditionTags = [];
      this.displayListNum = listNum;
      this.sortNum = 0;
      let articlesFromVuex = [];

      if (listNum === 1) {
        articlesFromVuex = this.postedArticles;
      } else if (listNum === 2) {
        articlesFromVuex = this.feedbackArticles;
      } else if (listNum === 3) {
        articlesFromVuex = this.myArticles;
      } else if (listNum === 11) {
        articlesFromVuex = this.notPostedQiitaArticles;
      } else if (listNum === 12) {
        articlesFromVuex = this.postedQiitaArticles;
      } else {
        console.log("予期せぬ値 listNum:" + listNum);
      }
      articlesFromVuex.forEach(function (value) {
        this.articles.push(value);
        value.tags.forEach(function (tag) {
          this.usedTags.push(tag);
        }, this)
      }, this)
    },
    searchWithConditions() {
      if (this.displayListNum === 1) {
        this.articles = this.postedArticles
      }
      if (this.displayListNum === 2) {
        this.articles = this.feedbackArticles
      }
      if (this.displayListNum === 3) {
        this.articles = this.myArticles
      }
      if (this.displayListNum === 11) {
        this.articles = this.notPostedQiitaArticles
      }
      if (this.displayListNum === 12) {
        this.articles = this.postedQiitaArticles
      }
      this.articles = this.articles.filter(article => {
        return article.title.includes(this.conditions.title)
      })
      for (let conTag of this.conditions.conditionTags) {
        this.articles = this.articles.filter(function (value) {
          return value.tags.find(artTag => artTag.tagId === conTag)
        })
      }
    },
    ...mapActions("user", ["fetchUserDetail", "fetchPostedArticles", "fetchFeedbackArticles", "fetchMyArticles"]),
  },
  async created() {
    await this.fetchUserDetail(this.$route.query.userId);
    //DBに存在しないユーザーIDが渡された場合記事一覧に戻る
    if (this.userId === 0) {
      await this.$router.push({path: '/articleList'})
    }

    this.fetchFeedbackArticles(this.$route.query.userId);
    this.fetchMyArticles(this.$route.query.userId);

    await this.fetchPostedArticles(this.$route.query.userId);
    //投稿記事が0件じゃない場合ArticleCardコンポーネント表示
    if (this.postedArticles.length !== 0) {
      this.changeList(1);
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