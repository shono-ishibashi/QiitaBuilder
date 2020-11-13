<template>
  <v-container :class="{'d-flex':windowWidthClass}">
    <v-row justify="center" align-content="center">
      <v-col cols=6 class="contentWrap">
        <v-avatar size="80%">
          <img :src="userDetail.photoUrl" alt=""/></v-avatar>
      </v-col>
      <v-col cols=6 align-self="center">
        <Pie class="chart" :chart-data="chartDisplay" :options="chartOptions" v-if="userDetail.usedTags.length!==0"/>
        <v-card v-if="userDetail.usedTags.length===0" height="80%" class="contentWrap">
          タグの使用履歴がありません
        </v-card>
      </v-col>
      <v-col cols="6" style="font-size: large" class="contentWrap"><span
          style="font-weight: bold">{{ userDetail.displayName }}</span></v-col>
      <v-col cols="6" class="contentWrap">
        <v-btn @click="toQiitaAPIAuthentication" color="#5bc8ac" elevation="2" style="font-weight: bold">Qiita連携</v-btn>
      </v-col>


      <v-col cols=5>
        <v-card>
          <v-layout justify-center>
            Qiita投稿数 / Builder投稿数<br></v-layout>
          <v-layout justify-center>
            {{ userDetail.postedArticleCount }} / {{ postedArticles.length }}
          </v-layout>
        </v-card>
      </v-col>
      <v-col cols=3>
        <v-card>
          <v-layout justify-center>FB数</v-layout>
          <v-layout justify-center>
            {{ userDetail.feedbackCount }}
          </v-layout>
        </v-card>
      </v-col>
      <v-col cols="4">
        <v-card>
          <v-layout justify-center>総獲得推奨数</v-layout>
          <v-layout justify-center>
            {{ userDetail.qiitaRecommendedAllCount }}
          </v-layout>
        </v-card>
      </v-col>
    </v-row>

    <v-container>
      <v-tabs v-model="activeListTab">
        <v-tab v-for="tab of listTabs" :key="tab.id" @click="changeList(tab.id)">{{ tab.name }}</v-tab>
      </v-tabs>

      <v-card outlined>
        <v-container>
          <v-row>
            <v-tabs v-model="activeStateTab">
              <v-tab v-for="tab of stateTabs" :key="tab.id" @click="changeListState(tab.id)">{{ tab.name }}</v-tab>
              <v-tab
                  v-if="userDetail.isLoginUser"
                  @click="changeListState(0)">
                下書き記事
              </v-tab>
            </v-tabs>

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
              <v-btn @click="searchWithConditions" color="#5bc8ac" elevation="2" style="font-weight: bold">検索</v-btn>
            </v-col>

            <v-layout justify-center>
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
            </v-layout>
            <v-col cols="10">
              <ArticleCard v-for="(article,index) in sortedArticles" :key="article.articleId" :article="article"
                           :is="articleCardDisplay" :index="index">
              </ArticleCard>
              <v-card v-if="sortedArticles.length===0" class="contentWrap">
                該当する記事がありません
              </v-card>
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
      </v-card>
    </v-container>
  </v-container>
</template>

<script>
import {mapState, mapActions, mapGetters} from "vuex";
import ArticleCard from "../components/ArticleCard";
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
      },//Pieコンポーネントに渡してグラフを表示するためのデータ。DBからタグ使用率を取り次第dataとcolor指定
      chartOptions: {
        responsive: true,
        legend: {
          position: 'right'
        }
      },//Pieコンポーネントのグラフ表示用オプション
      sortList: [
        {key: 0, state: "新着順"},
        {key: 1, state: "更新順"},
        {key: 2, state: "Qiita推奨数順"},
        {key: 3, state: "My記事登録数順"}
      ],//並び替え選択用リスト
      sortNum: 0,//現在のソートkey
      page: 1,//現在のページ
      pageSize: 10,//ページ当たりの記事数
      displayListNum: 1,//1:posted, 2:feedback, 3:my, 11:notPostedQiita, 12:postedQiita, 13:draft
      displayListState: 10,//10:all, 0:draft, 1:notPostedQiita, 2:postedQiita
      conditions: {
        title: "",
        conditionTags: []
      },//検索条件
      windowWidth: window.innerWidth,//画面横幅
      windowWidthClass: false,//画面横幅に応じて付与するクラスの切り替え用boolean
      activeListTab: 0,//記事Tabの選択されているタブのインデックス
      activeStateTab: 0,//記事StateTabの選択されているタブのインデックス
      listTabs: [
        {id: 1, name: '投稿記事'},
        {id: 2, name: 'FB記事'},
        {id: 3, name: 'My記事'}
      ],//記事タブ表示用リスト
      stateTabs: [
        {id: 10, name: '全記事'},
        {id: 1, name: 'Qiita未投稿記事'},
        {id: 2, name: 'Qiita投稿済み記事'},
      ],//記事stateタブ表示用リスト
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
      "postedArticles",
      "feedbackArticles",
      "myArticles",
      "userId",
      "displayArticles",
      "usedTags",
      "articleCardDisplay",
      "chartDisplay",]),
    ...mapState("user", ["userDetail",])
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
      this.conditions.title = "";
      this.conditions.conditionTags = [];
      this.displayListNum = listNum;
      this.displayListState = 10;
      this.sortNum = 0;
      this.activeListTab = listNum - 1;
      this.activeStateTab = 0;
      let articlesFromVuex = [];

      if (listNum === 1) articlesFromVuex = this.postedArticles;
      if (listNum === 2) articlesFromVuex = this.feedbackArticles;
      if (listNum === 3) articlesFromVuex = this.myArticles;
      this.setArticlesAndTags(articlesFromVuex);
      if (articlesFromVuex.length === 0) {
        this.sortedArticles.length = 0;
      }
      this.page = 1;
      this.length = Math.ceil(this.displayArticles.length / this.pageSize);
    },
    /**
     * 表示したい記事に対応する数値を渡して、表示する記事一覧を変更する
     * @param listSate (10:全記事), (1:Qiita未投稿記事), (2:Qiita投稿済み記事), (3:下書き記事)
     */
    changeListState(listSate) {
      this.conditions.title = "";
      this.conditions.conditionTags = [];
      this.displayListState = listSate;
      this.sortNum = 0;

      let articlesFromVuex = [];
      if (listSate === 10) {
        if (this.displayListNum === 1) articlesFromVuex = this.postedArticles;
        if (this.displayListNum === 2) articlesFromVuex = this.feedbackArticles;
        if (this.displayListNum === 3) articlesFromVuex = this.myArticles;
      }
      if (listSate !== 10) {
        if (this.displayListNum === 1) articlesFromVuex = this.postedArticles.filter((art) => {
          return art.stateFlag === listSate
        });
        if (this.displayListNum === 2) articlesFromVuex = this.feedbackArticles.filter((art) => {
          return art.stateFlag === listSate
        });
        if (this.displayListNum === 3) articlesFromVuex = this.myArticles.filter((art) => {
          return art.stateFlag === listSate
        });
      }
      this.setArticlesAndTags(articlesFromVuex);
      if (articlesFromVuex.length === 0) {
        this.sortedArticles.length = 0;
      }
      this.page = 1;
      this.length = Math.ceil(this.displayArticles.length / this.pageSize);
    },
    /**
     * 入力された検索条件とタグ条件に応じた記事を検索する
     */
    searchWithConditions() {
      let articlesFromVuex = [];
      if (this.displayListState === 10) {
        if (this.displayListNum === 1) articlesFromVuex = this.postedArticles;
        if (this.displayListNum === 2) articlesFromVuex = this.feedbackArticles;
        if (this.displayListNum === 3) articlesFromVuex = this.myArticles;
      }
      if (this.displayListState !== 10) {
        if (this.displayListNum === 1) articlesFromVuex = this.postedArticles.filter((art) => {
          return art.stateFlag === this.displayListState
        });
        if (this.displayListNum === 2) articlesFromVuex = this.feedbackArticles.filter((art) => {
          return art.stateFlag === this.displayListState
        });
        if (this.displayListNum === 3) articlesFromVuex = this.myArticles.filter((art) => {
          return art.stateFlag === this.displayListState
        });
      }

      articlesFromVuex = articlesFromVuex.filter(article => {
        return article.title.includes(this.conditions.title)
      })
      for (let conTag of this.conditions.conditionTags) {
        articlesFromVuex = articlesFromVuex.filter(function (value) {
          return value.tags.find(artTag => artTag.tagId === conTag)
        })
      }
      this.setArticles(articlesFromVuex);
      this.page = 1;
      this.length = Math.ceil(this.displayArticles.length / this.pageSize);
    },

    ...mapActions("user", [
      "setArticlesAndTags",
      "setArticles",
      "setArticleCardDisplay",
      "setChartDisplay",
      "fetchUserDetail",
      "fetchPostedArticles",
      "fetchFeedbackArticles",
      "fetchMyArticles"]),


  },
  created() {
    //画面横幅が960px以上であればwindowWidthClassをtrueに変え画面を記事一覧を横に配置
    (this.windowWidth >= 960) ? this.windowWidthClass = true : this.windowWidthClass = false;
  },
  mounted() {
    //画面の横幅が変わるが度に960px以上かを判定
    window.onresize = () => {
      this.windowWidth = window.innerWidth;
      (this.windowWidth >= 960) ? this.windowWidthClass = true : this.windowWidthClass = false;
    }
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
<style scoped>
.contentWrap {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
