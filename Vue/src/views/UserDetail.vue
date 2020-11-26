<template>
  <v-container :class="{'d-flex':windowWidthClass}" fluid>
    <v-row>
      <v-col cols="12" sm="12" md="6">
        <v-row>
          <v-col cols="6" class="contentWrap"><span
              style="font-weight: bold; font-size: x-large;">@{{ userDetail.displayName }}</span></v-col>
          <v-col cols="6" class="contentWrap">
            <v-btn @click="toQiitaAPIAuthentication" v-if="userDetail.isLoginUser" color="#5bc8ac" elevation="2"
                   style="font-weight: bold" dark>Qiita連携
            </v-btn>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols=6 class="contentWrap">
            <v-avatar size="140">
              <img :src="userDetail.photoUrl" alt=""/></v-avatar>
          </v-col>
          <v-col cols=6 align-self="center">
            <Pie class="chart" :chart-data="chartDisplay" :options="chartOptions"
                 v-if="userDetail.usedTags.length!==0"/>
            <v-alert
                v-if="userDetail.usedTags.length===0"
                text
                dense
                color="teal"
                border="left"
                class="contentWrap"
            >
              タグの使用履歴がありません
            </v-alert>
          </v-col>
        </v-row>

        <v-row align-content="center" justify="center" class="box16">
          <v-col cols="4" class="box6">
            <v-row align-content="center" justify="center">
              Qiita投稿数 / Builder投稿数
            </v-row>
            <v-row align-content="center" justify="center" class="count">
              {{ userDetail.postedArticleCount }} / {{ notDraftArticles.length }}
            </v-row>
          </v-col>
          <v-col cols="3" class="box6">
            <v-row align-content="center" justify="center">
              FB数
            </v-row>
            <v-row align-content="center" justify="center" class="count">
              {{ userDetail.feedbackCount }}
            </v-row>
          </v-col>
          <v-col cols="3" class="box6">
            <v-row align-content="center" justify="center">
              総獲得推奨数
            </v-row>
            <v-row align-content="center" justify="center" class="count">
              {{ userDetail.qiitaRecommendedAllCount }}
            </v-row>
          </v-col>
        </v-row>
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
                <v-col cols="9" style="padding: 0">
                  <v-tabs v-model="activeStateTab" v-show="displayListNum!==0" color="#5bc8ac">
                    <v-tab v-for="tab of stateTabs" :key="tab.id" @click="changeListState(tab.id)">
                      {{ tab.name }}
                    </v-tab>
                  </v-tabs>
                </v-col>
                <v-col cols="2" style="padding: 0">
                  <v-select
                      :items="sortList"
                      item-value="key"
                      item-text="state"
                      item-color="green"
                      color="#5bc8ac"
                      v-model="sortNum"
                      style="padding: 0"
                  >
                  </v-select>
                </v-col>
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
                                   :is="articleCardDisplay" :index="index" style="margin: 0; padding: 0;">
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
      pageSize: 5,//ページ当たりの記事数
      displayListNum: 1,//1:posted, 2:feedback, 3:my, 0:draft
      displayListState: 10,//10:all, 1:notPostedQiita, 2:postedQiita
      conditions: {
        title: "",
        conditionTags: []
      },//検索条件
      windowWidth: window.innerWidth,//画面横幅
      windowWidthClass: false,//画面横幅に応じて付与するクラスの切り替え用boolean
      activeListTab: 0,//記事Tabの選択されているタブのインデックス
      activeStateTab: 0,//記事StateTabの選択されているタブのインデックス
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
      "articleCardDisplay",
      "chartDisplay",]),
    ...mapState("user", ["userDetail",]),
    ...mapGetters("auth", ["loginUser"]),
  },
  watch: {
    sortNum() {
      this.page = 1;//sort変更時computedによる並び替え変更が行われるのでページが変更されないため、ここで1pに変えている
    },
    page(){
      setTimeout(() => {
        window.scrollTo({
          top: 0,
          behavior: "smooth"
        });

      }, 100)
    },
    apiToken: async function () {
      if (this.$route.params['userId'] === '0') {
        if (!this.loginUser.uid) await this.$router.push({name: "articleList"});
        await this.findUserIdByUid(this.loginUser.uid);
        //DBに存在しないユーザーIDが渡された場合記事一覧に戻る
        /*if (this.userId === 0 || this.userId) {
          await this.$router.push({path: '/article'})
        }*/
        await this.fetchUserDetail(this.userId);
        await this.fetchFeedbackArticles(this.userId);
        await this.fetchMyArticles(this.userId);
        await this.fetchPostedArticles(this.userId);
      } else {
        await this.fetchUserDetail(this.$route.params['userId']);
        //DBに存在しないユーザーIDが渡された場合記事一覧に戻る
        if (this.userId === 0) {
          await this.$router.push({name: "articleList"})
        }
        await this.fetchFeedbackArticles(this.$route.params['userId']);
        await this.fetchMyArticles(this.$route.params['userId']);
        await this.fetchPostedArticles(this.$route.params['userId']);
      }

      if (this.$route.query.defaultList === '4' && this.userDetail.isLoginUser) {
        this.changeList(4);
      } else if (this.$route.query.defaultList === '3') {
        this.changeList(3);
      } else {
        this.changeList(1);
      }
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
     * @param listNum (1:投稿記事), (2:FB記事), (3:My記事), (0:下書き記事)
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
      if (listNum === 4) articlesFromVuex = this.draftArticles;
      this.setArticlesAndTags(articlesFromVuex);
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
    changeListState(listState) {
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
      "findUserIdByUid"
    ]),


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
