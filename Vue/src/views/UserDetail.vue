<template>
  <v-container>
    <v-row justify="center">
      <v-col cols=5><img :src="userDetail.photoUrl" alt=""/></v-col>
      <v-col cols=5>
        <Pie class="chart" :chart-data="chartDisplay" :options="chartOptions"/>
      </v-col>

      <v-col cols=5>{{ userDetail.displayName }}</v-col>
      <v-col cols=4>
        <v-btn @click="toQiitaAPIAuthentication" color="#5bc8ac" elevation="2">Qiita連携</v-btn>
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

      <v-col cols="4">
        <v-btn color="#5bc8ac" @click="changeList(1)">投稿記事</v-btn>
      </v-col>
      <v-col cols="4">
        <v-btn color="#5bc8ac" @click="changeList(2)">FB記事</v-btn>
      </v-col>
      <v-col cols="4">
        <v-btn color="#5bc8ac" @click="changeList(3)">My記事</v-btn>
      </v-col>

      <v-col cols="12" v-if="displayListNum===1||displayListNum===11||displayListNum===12||displayListNum===13">
        <v-btn color="#5bc8ac" @click="changeList(1)">全記事</v-btn>
        <v-btn color="#5bc8ac" @click="changeList(11)">Qiita 未投稿記事</v-btn>
        <v-btn color="#5bc8ac" @click="changeList(12)">Qiita 投稿済み記事</v-btn>
        <v-btn
            v-if="userDetail.isLoginUser"
            color="#5bc8ac"
            @click="changeList(13)">
          下書き記事
        </v-btn>
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
        <v-btn @click="searchWithConditions" color="#5bc8ac" elevation="2">検索</v-btn>
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
                     :is="articleCardDisplay" :index="index">
        </ArticleCard>
        <div v-if="displayArticles.length===0">
          該当する記事がありません
        </div>
        <v-pagination
            v-model="page"
            :length="length"
            prev-icon="mdi-menu-left"
            next-icon="mdi-menu-right"
            dark
        ></v-pagination>

      </v-col>
    </v-row>

  </v-container>
</template>

<script>
import {mapState, mapActions, mapGetters} from "vuex";
import ArticleCard from "../components/ArticleCard";
import axios from "axios";
import Pie from "@/components/user_detail/Pie";
import * as palette from "google-palette";

export default {
  name: "userDetail",
  components: {ArticleCard, Pie},
  data() {
    return {
      chartData: {
        labels: [],
        datasets: [
          {
            data: [],
            backgroundColor: [],
          },
        ]
      },
      chartOptions: {
        responsive: true,
        legend: {
          position: 'right'
        }
      },
      //chartDisplayData: null,//コンポーネント表示切替用真偽値
      sortList: [
        {key: 0, state: "新着順"},
        {key: 1, state: "更新順"},
        {key: 2, state: "Qiita推奨数順"},
        {key: 3, state: "My記事登録数順"}
      ],//並び替え選択用リスト
      sortNum: 0,//現在のソートkey
      page: 1,//現在のページ
      pageSize: 10,//ページ当たりの記事数
      articles: [],//データ整理用list
      //articleCardDisplay: ArticleCard,//コンポーネント表示切替用真偽値
      displayListNum: 1,//1:posted, 2:feedback, 3:my, 11:notPostedQiita, 12:postedQiita, 13:draft
      conditions: {
        title: "",
        conditionTags: []
      },//検索条件
    };
  },
  computed: {
    apiToken() {
      return this.$store.getters["auth/apiToken"];
    },
    length: {
      get() {
        return Math.ceil(this.displayArticles.length / this.pageSize);
      },
      set() {
      },
    },
    sortedArticles: {
      get() {
        let art = this.displayArticles;
        if (this.sortNum === 0) {
          art = art.sort((a, b) => {
            return (a.createdAt < b.createdAt) ? 1 : (a.createdAt > b.createdAt) ? -1 : 0;
          })
        }
        if (this.sortNum === 1) {
          art = art.sort((a, b) => {
            return (a.updatedAt < b.updatedAt) ? 1 : (a.updatedAt > b.updatedAt) ? -1 : 0;
          })
        }
        if (this.sortNum === 2) {
          art = art.sort((a, b) => {
            return (a.qiitaRecommendPoint < b.qiitaRecommendPoint) ? 1 : (a.qiitaRecommendPoint > b.qiitaRecommendPoint) ? -1 : 0;
          })
        }
        if (this.sortNum === 3) {
          art = art.sort((a, b) => {
            return (a.registeredMyArticleCount < b.registeredMyArticleCount) ? 1 : (a.registeredMyArticleCount > b.registeredMyArticleCount) ? -1 : 0;
          })
        }
        return art.slice(this.pageSize * (this.page - 1), this.pageSize * (this.page));
      },
      set() {
      },
    },
    ...mapGetters("user", [
      "userId",
      "notPostedQiitaArticles",
      "postedQiitaArticles",
      "draftArticles",
      "displayArticles", "" +
      "usedTags",
      "articleCardDisplay",
      "chartDisplay",]),
    ...mapState("user", ["userDetail", "postedArticles", "feedbackArticles", "myArticles"])
  },
  watch: {
    sortNum() {
      this.page = 1;//sort変更時computedによる並び替え変更が行われるのでページが変更されないため、ここで1pに変えている
    },
    apiToken: async function () {
      await this.fetchUserDetail(this.$route.params['userId']);
      //DBに存在しないユーザーIDが渡された場合記事一覧に戻る
      if (this.userId === 0) {
        await this.$router.push({path: '/article'})
      }

      await this.fetchFeedbackArticles(this.$route.params['userId']);
      await this.fetchMyArticles(this.$route.params['userId']);

      await this.fetchPostedArticles(this.$route.params['userId']);
      this.changeList(1);
      this.setArticleCardDisplay(ArticleCard);

      await this.userDetail.usedTags.forEach(function (tag) {
        this.chartData.labels.push(tag.tagName);
        this.chartData.datasets[0].data.push(tag.usedTagCount);
      }, this);
      this.chartData.datasets[0].backgroundColor = palette('cb-YlGn', this.userDetail.usedTags.length).map(
          function (hex) {
            return '#' + hex
          }
      )
      this.setChartDisplay(this.chartData);
    },
  },
  methods: {
    /**
     * 表示したい記事に対応する数値を渡して、表示する記事一覧とオートコンプリート用タグリストを変更する
     * @param listNum (1:投稿記事), (2:FB記事), (3:My記事)
     */
    changeList(listNum) {
      this.setArticlesAndTags([]);
      //this.usedTags.length = 0;
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
      } else if (listNum === 13 && this.userDetail.isLoginUser) {
        articlesFromVuex = this.draftArticles;
      } else {
        console.log("予期せぬ値 listNum:" + listNum);
      }
      this.setArticlesAndTags(articlesFromVuex);
      if (articlesFromVuex.length === 0) {
        this.sortedArticles.length = 0;
      }
      this.page = 1;
      this.length = Math.ceil(this.displayArticles.length / this.pageSize);
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
      if (this.displayListNum === 13 && this.userDetail.isLoginUser) {
        this.articles = this.draftArticles
      }
      this.articles = this.articles.filter(article => {
        return article.title.includes(this.conditions.title)
      })
      for (let conTag of this.conditions.conditionTags) {
        this.articles = this.articles.filter(function (value) {
          return value.tags.find(artTag => artTag.tagId === conTag)
        })
      }
      this.setArticlesAndTags(this.articles);
      this.page = 1;
      this.length = Math.ceil(this.displayArticles.length / this.pageSize);
    },


    /**
     *
     * Qiitaの認証画面を表示
     *
     */
    toQiitaAPIAuthentication() {
      axios.get(this.$store.getters.API_URL + 'qiita/to-qiita-api-authentication', {
        headers: {
          Authorization: this.$store.getters["auth/apiToken"]
        }
      }).then((response) => {
        location.href = response.data;
      })
    },
    ...mapActions("user", [
      "setArticlesAndTags",
      "setArticleCardDisplay",
      "setChartDisplay",
      "fetchUserDetail",
      "fetchPostedArticles",
      "fetchFeedbackArticles",
      "fetchMyArticles"]),


  },
  created() {
  },
  beforeRouteEnter(to, from, next) {
    //URLのparam(userId)に数値以外が入力された際に記事一覧に戻る
    if (!isNaN(to.params['userId'])) {
      next();
    } else {
      next({path: '/article'})
    }
  }
}
</script>
