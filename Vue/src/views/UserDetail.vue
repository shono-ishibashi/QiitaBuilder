<template>
  <v-container :class="{'d-flex':windowWidthClass}" fluid>
    <v-row>
      <v-col cols="12" sm="12" md="6">
        <UserInfo></UserInfo>
      </v-col>

      <v-col cols="12" sm="12" md="6">
        <v-container>
          <v-tabs v-model="activeListTab" v-if="userDetail.isLoginUser" color="#5bc8ac">
            <v-tab v-for="tab of loginListTabs" :key="tab.id" @click="changeList(tab.id)">{{ tab.name }}</v-tab>
          </v-tabs>
          <v-tabs v-model="activeListTab" v-if="!(userDetail.isLoginUser)" color="#5bc8ac">
            <v-tab v-for="tab of notLoginListTabs" :key="tab.id" @click="changeList(tab.id)">{{ tab.name }}</v-tab>
          </v-tabs>

          <v-card outlined>
            <v-container fluid>
              <v-row>
                <v-col cols="4" style="padding: 0">
                  <v-select
                      :items="stateTabs"
                      item-text="name"
                      item-value="id"
                      item-color="green"
                      color="#5bc8ac"
                      @change="changeListState"
                      v-model="activeStateTab"
                      label="絞り込み"
                  ></v-select>
                </v-col>
                <v-col cols="1" style="padding: 0"></v-col>
                <v-col cols="2" style="padding: 0">
                  <v-select
                      :items="sortList"
                      item-value="key"
                      item-text="state"
                      item-color="green"
                      color="#5bc8ac"
                      v-model="sortNum"
                      style="padding-top: 9px"
                      label="並び順"
                  >
                  </v-select>
                </v-col>
                <v-col cols="4" style="padding: 0"></v-col>

              </v-row>
              <v-row justify="center" align-content="center">
                <v-col cols="12">
                  <v-card class="contentWrap" outline-color="#008b8b" outlined>
                    <v-card-text style="padding: 0 2px">
                      <v-row justify="center" align-content="center" style="padding: 0 30px">

                        <v-col cols="1" align-self="center" style="padding: 0;">
                          <v-icon>mdi-magnify</v-icon>
                        </v-col>
                        <v-col cols="4" style="padding: 0">
                          <v-form ref="search_form">
                            <v-text-field
                                v-model="conditions.title"
                                label="記事タイトルを入力"
                                :rules="[title_limit_length]"
                                color="#5bc8ac"
                            ></v-text-field>
                          </v-form>
                        </v-col>
                        <v-col cols="4" style="padding: 0">
                          <v-form ref="search_form">
                            <v-autocomplete
                                v-model="conditions.conditionTags"
                                :items="usedTags"
                                :rules="[tags_limit_length]"
                                item-value="tagId"
                                item-text="tagName"
                                item-color="green"
                                label="タグを選択"
                                color="#5bc8ac"
                                chips
                                deletable-chips
                                multiple
                                small-chips
                                style="margin-top: 8px; margin-left: 2px"
                            >
                            </v-autocomplete>
                          </v-form>
                        </v-col>
                        <v-col cols="3">
                          <v-card-actions>
                            <v-btn
                                @click="searchWithConditions"
                                color="#5bc8ac"
                                elevation="2"
                                small
                                dark
                            >
                              検索
                            </v-btn>
                            <v-btn
                                @click="resetConditions"
                                color="#ff6347"
                                elevation="2"
                                small
                                dark
                            >
                              リセット
                            </v-btn>
                          </v-card-actions>
                        </v-col>

                      </v-row>
                    </v-card-text>
                  </v-card>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="12">
                  <v-row align-content="center" justify="center">
                    <v-col cols="12">
                      <ArticleCard v-for="(article,index) in sortedArticles" :key="article.articleId" :article="article"
                                   :is="articleCardDisplay" :index="index" style="margin: 0; padding: 0;"
                                   v-show="sortedArticles.length!==0">
                      </ArticleCard>
                    </v-col>
                  </v-row>
                  <v-row justify="center" align-content="center">
                    <v-alert
                        width="60%"
                        text
                        dense
                        color="teal"
                        icon="mdi-emoticon-confused"
                        border="left"
                        v-if="sortedArticles.length===0"
                        class="contentWrap"
                    >
                      該当する記事がありません
                    </v-alert>
                  </v-row>
                  <v-row justify="center" align-content="center">
                    <v-pagination
                        v-model="page"
                        :length="length"
                        color="#5bc8ac"
                        circle
                    ></v-pagination>
                  </v-row>
                </v-col>
              </v-row>
            </v-container>
          </v-card>
        </v-container>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import {mapState, mapActions, mapGetters} from "vuex";
import ArticleCard from "../components/ArticleCard";
import UserInfo from "@/components/user_detail/UserInfo";
import * as palette from "google-palette";

export default {
  name: "userDetail",
  components: {ArticleCard, UserInfo},
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
      pageSize: 5,//ページ当たりの記事数
      displayListNum: 1,//1:posted, 2:feedback, 3:my, 0:draft
      displayListState: 10,//10:all, 1:notPostedQiita, 2:postedQiita
      conditions: {
        title: "",
        conditionTags: []
      },//検索条件
      windowWidth: window.innerWidth,//画面横幅
      windowWidthClass: false,//画面横幅に応じて付与するクラスの切り替え用boolean
      activeListTab: 1,//記事Tabの選択されているタブのインデックス
      activeStateTab: {id: 10, name: '全記事'},//記事StateTabの選択されているタブのインデックス
      loginListTabs: [
        {id: 1, name: '公開中の投稿記事'},
        {id: 2, name: '公開中のFBした記事'},
        {id: 3, name: '公開中のMy記事'},
        {id: 4, name: '下書き記事'}
      ],//記事タブ表示用リスト
      notLoginListTabs: [
        {id: 1, name: '公開中の投稿記事'},
        {id: 2, name: '公開中のFBした記事'},
        {id: 3, name: '公開中のMy記事'},
      ],
      stateTabs: [
        {id: 10, name: '全記事'},
        {id: 1, name: 'Qiita未投稿記事'},
        {id: 2, name: 'Qiita投稿済み記事'},
      ],//記事stateタブ表示用リスト
      articleCardDisplay: "",
      title_limit_length: value => value.length <= 100 || "100文字以内で入力してください",
      tags_limit_length: value => value.length <= 5 || "6個以上入力しないでください",
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
      "notDraftArticles",
      "draftArticles",
      "feedbackArticles",
      "myArticles",
      "userId",
      "displayArticles",
      "usedTags",
      "chartDisplay",]),
    ...mapState("user", ["userDetail",]),
    ...mapGetters("auth", ["loginUser"]),
  },
  watch: {
    postedArticles() {
      const th = this;
      let query;
      query = Object.assign({}, th.$route.query);
      try {
        query = query["defaultList"].toString();
      } catch (err) {
        query = 1
      }
      const change = async function () {
        if (query === '4' && th.userDetail.isLoginUser) {
          await th.changeList(4);
        } else if (query === '3') {
          await th.changeList(3);
        } else {
          await th.changeList(1);
        }
        th.setArticleCardDisplay(ArticleCard);
        th.articleCardDisplay = "ArticleCard"
      }
      const chart = async function () {
        th.userDetail.usedTags.forEach(function (tag) {
          th.chartData.labels.push(tag.tagName);
          th.chartData.datasets[0].data.push(tag.usedTagCount);
        }, th);
        th.chartData.datasets[0].backgroundColor = palette('cb-YlGn', th.userDetail.usedTags.length).map(
            function (hex) {
              return '#' + hex
            }
        )
        await th.setChartDisplay(th.chartData);
      }
      const processAll = async function () {
        await change();
        await chart();
      }
      processAll();
    },
    sortNum() {
      this.page = 1;//sort変更時computedによる並び替え変更が行われるのでページが変更されないため、ここで1pに変えている
    },
    page() {
      setTimeout(() => {
        window.scrollTo({
          top: 0,
          behavior: "smooth"
        });
      }, 100)
    },
    apiToken: function () {
      const paramUserId = this.$route.params['userId'];
      //let paramUserId = Object.assign({}, this.$route.params);
      //paramUserId = paramUserId['userId'].toString();
      const th = this;
      const fetch = async function () {
        if (paramUserId === '0') {
          if (!th.loginUser.uid) await th.$store.dispatch("window/setInternalServerError", true);
          await th.findUserIdByUid(th.loginUser.uid);
          await th.fetchUserDetail(th.userId);
          //ユーザーが見つからない場合はこれ以降は実行されずwindow componentに切り替わる
          await th.fetchFeedbackArticles(th.userId);
          await th.fetchMyArticles(th.userId);
          await th.fetchPostedArticles(th.userId);
        } else {
          await th.fetchUserDetail(paramUserId);
          await th.fetchFeedbackArticles(paramUserId);
          await th.fetchMyArticles(paramUserId);
          await th.fetchPostedArticles(paramUserId);
        }
      }
      /*const change = async function () {
        if (th.$route.query.defaultList === '4' && th.userDetail.isLoginUser) {
          await th.changeList(4);
        } else if (th.$route.query.defaultList === '3') {
          await th.changeList(3);
        } else {
          await th.changeList(1);
        }
        th.setArticleCardDisplay(ArticleCard);
        th.articleCardDisplay = "ArticleCard"
      }
      const chart = async function () {
        th.userDetail.usedTags.forEach(function (tag) {
          th.chartData.labels.push(tag.tagName);
          th.chartData.datasets[0].data.push(tag.usedTagCount);
        }, th);
        th.chartData.datasets[0].backgroundColor = palette('cb-YlGn', th.userDetail.usedTags.length).map(
            function (hex) {
              return '#' + hex
            }
        )
        await th.setChartDisplay(th.chartData);
      }*/
      const processAll = async function () {
        await fetch();
        //await change();
        //await chart();
      }
      processAll();

    },
  },
  methods: {
    /**
     * 表示したい記事に対応する数値を渡して、表示する記事一覧とオートコンプリート用タグリストを変更する
     * @param listNum (1:投稿記事), (2:FB記事), (3:My記事), (0:下書き記事)
     */
    async changeList(listNum) {
      this.conditions.title = "";
      this.conditions.conditionTags = [];
      this.displayListNum = listNum;
      this.displayListState = 10;
      this.sortNum = 0;
      this.activeListTab = listNum - 1;
      this.activeStateTab = {id: 10, name: '全記事'};
      let articlesFromVuex = [];

      if (listNum === 1) articlesFromVuex = this.postedArticles;
      if (listNum === 2) articlesFromVuex = this.feedbackArticles;
      if (listNum === 3) articlesFromVuex = this.myArticles;
      if (listNum === 4) articlesFromVuex = this.draftArticles;
      await this.setArticlesAndTags(articlesFromVuex);
      if (articlesFromVuex.length === 0) {
        this.sortedArticles.length = 0;
      }
      this.page = 1;
      this.length = Math.ceil(this.displayArticles.length / this.pageSize);
    },
    /**
     * 表示したい記事に対応する数値を渡して、表示する記事一覧を変更する
     * @param listState (10:全記事), (1:Qiita未投稿記事), (2:Qiita投稿済み記事)
     */
    async changeListState(listState) {
      this.conditions.title = "";
      this.conditions.conditionTags = [];
      this.displayListState = listState;
      this.sortNum = 0;

      let articlesFromVuex = [];
      if (listState === 10) {
        if (this.displayListNum === 1) articlesFromVuex = this.postedArticles;
        if (this.displayListNum === 2) articlesFromVuex = this.feedbackArticles;
        if (this.displayListNum === 3) articlesFromVuex = this.myArticles;
      }
      if (listState !== 10) {
        if (this.displayListNum === 1 && this.postedArticles.length !== 0) {
          articlesFromVuex = this.postedArticles.filter((art) => {
            return art.stateFlag === listState
          })
        }
        if (this.displayListNum === 2 && this.feedbackArticles.length !== 0) {
          articlesFromVuex = this.feedbackArticles.filter((art) => {
            return art.stateFlag === listState
          })
        }
        if (this.displayListNum === 3 && this.myArticles.length !== 0) {
          articlesFromVuex = this.myArticles.filter((art) => {
            return art.stateFlag === listState
          })
        }
      }
      await this.setArticlesAndTags(articlesFromVuex);
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
      if (this.$refs.search_form.validate()) {
        let articlesFromVuex = [];
        if (this.displayListState === 10) {
          if (this.displayListNum === 1) articlesFromVuex = this.postedArticles;
          if (this.displayListNum === 2) articlesFromVuex = this.feedbackArticles;
          if (this.displayListNum === 3) articlesFromVuex = this.myArticles;
          if (this.displayListNum === 0) articlesFromVuex = this.draftArticles;
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
      }
    },
    resetConditions() {
      this.conditions.title = "";
      this.conditions.conditionTags.splice(0);
      this.searchWithConditions();
    },
    ...mapActions("user", [
      "setArticlesAndTags",
      "setArticles",
      "setArticleCardDisplay",
      "setChartDisplay",
      "fetchUserDetail",
      "fetchPostedArticles",
      "fetchFeedbackArticles",
      "fetchMyArticles",
      "findUserIdByUid",
      "clearState"
    ]),
  },
  created() {
    //画面横幅が960px以上であればwindowWidthClassをtrueに変え画面を記事一覧を横に配置
    (this.windowWidth >= 960) ? this.windowWidthClass = true : this.windowWidthClass = false
  },
  mounted() {
    //画面の横幅が変わるが度に960px以上かを判定
    window.onresize = () => {
      this.windowWidth = window.innerWidth;
      (this.windowWidth >= 960) ? this.windowWidthClass = true : this.windowWidthClass = false;
    }
  },
  beforeDestroy() {
    this.clearState()//遷移前にstoreを空にしないと次にユーザー詳細画面来たとき前回のユーザーが表示されてしまう
  },
  beforeRouteEnter(to, from, next) {
    //URLのparam(userId)に数値以外が入力された際に記事一覧に戻る
    if (!isNaN(to.params['userId'])) {
      next();
    } else {
      next({path: '/404'})
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

.count {
  font-size: x-large;
  font-weight: bold;
}

.box16 {
  padding: 0.5em 1em;
  margin: 2em 0;
  background: -webkit-repeating-linear-gradient(-45deg, #f0f8ff, #f0f8ff 3px, #e9f4ff 3px, #e9f4ff 7px);
  background: repeating-linear-gradient(-45deg, #f0f8ff, #f0f8ff 3px, #e9f4ff 3px, #e9f4ff 7px);
}

.box6 {
  padding: 0.5em 1em;
  margin: 2em 0.25em;
  background: #f0f7ff;
  border: dashed 2px #5bc8ac; /*点線*/
  border-radius: 10px;
}
</style>
