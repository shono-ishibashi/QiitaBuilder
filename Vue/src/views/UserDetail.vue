<template>
  <v-container :class="{'d-flex':windowWidthClass}" fluid>
    <v-row>
      <v-col cols="12" sm="12" md="6">
        <UserInfo v-show="!isLoading"></UserInfo>
        <v-progress-linear
            v-show="isLoading"
            color="green"
            indeterminate
            rounded
            height="10"
        ></v-progress-linear>
      </v-col>

      <v-col cols="12" sm="12" md="6">
        <v-container>
          <v-tabs v-model="displayListNum" v-if="userDetail.isLoginUser" color="#5bc8ac" data-test-id="login-tabs">
            <v-tab v-for="(tab, index) of loginListTabs" :key="tab.id" @click="changeList(tab.id)"
                   :data-test-id="'login-tab'+index">{{ tab.name }}
            </v-tab>
          </v-tabs>
          <v-tabs v-model="displayListNum" v-if="!(userDetail.isLoginUser)" color="#5bc8ac"
                  data-test-id="not-login-tabs">
            <v-tab v-for="(tab, index) of notLoginListTabs" :key="tab.id" @click="changeList(tab.id)"
                   :data-test-id="'not-login-tab'+index">{{ tab.name }}
            </v-tab>
          </v-tabs>

          <v-card outlined>
            <v-container fluid>
              <v-row>
                <v-col cols="4" style="padding: 0">
                  <v-select
                      :items="stateList"
                      item-text="name"
                      item-value="id"
                      item-color="green"
                      color="#5bc8ac"
                      @change="changeListState"
                      v-model="displayListState"
                      label="絞り込み"
                      data-test-id="list-state-selector"
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
                      data-test-id="sort-selector"
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
                          <v-form ref="search_form" @submit.prevent="">
                            <v-text-field
                                v-model="conditions.title"
                                label="記事タイトルを入力"
                                :rules="[title_limit_length]"
                                color="#5bc8ac"
                                data-test-id="search-title"
                                @keydown.enter.exact.prevent
                                @keyup.enter="searchWithConditions"
                            ></v-text-field>
                          </v-form>
                        </v-col>
                        <v-col cols="4" style="padding: 0">
                          <v-form ref="search_form" @submit.prevent="">
                            <v-autocomplete
                                v-model="conditions.conditionTags"
                                @keyup.enter="searchWithConditions"
                                @keydown.enter.exact.prevent
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
                                data-test-id="search-tag-form"
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
                                data-test-id="search-button"
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
                                   v-show="!isLoading&&sortedArticles.length!==0" :index="index"
                                   @thisUserPage="resetPage" :data-test-id="'article-card'+index"
                                   style="margin: 0; padding: 0;">
                      </ArticleCard>
                      <v-progress-linear
                          v-show="isLoading"
                          color="green"
                          indeterminate
                          rounded
                          height="10"
                      ></v-progress-linear>
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
                        v-show="sortedArticles.length===0"
                        class="contentWrap"
                        data-test-id="no-articles-alert"
                    >
                      該当する記事がありません
                    </v-alert>
                  </v-row>
                  <v-row justify="center" align-content="center">
                    <v-pagination
                        v-model="paging.now"
                        :length="length"
                        color="#5bc8ac"
                        circle
                        data-test-id="article-pagination"
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

export default {
  name: "userDetail",
  components: {ArticleCard, UserInfo},
  data() {
    return {
      sortList: [
        {key: 0, state: "新着順"},
        {key: 1, state: "更新順"},
        {key: 2, state: "Qiita推奨数順"},
        {key: 3, state: "お気に入り登録数順"}
      ],//並び替え選択用リスト
      sortNum: 0,//現在のソートkey
      paging: {
        now: 1,//現在のページ
        pageSize: 5,//1pあたりの記事数
      },//paging処理用オブジェクト
      displayListNum: 0,//0:notDraft, 1:feedback, 2:my, 3:draft
      displayListState: {id: 10, name: '全記事'},//10:all, 1:notPostedQiita, 2:postedQiita
      conditions: {
        title: "",
        conditionTags: []
      },//検索条件
      windowWidth: window.innerWidth,//画面横幅
      windowWidthClass: false,//画面横幅に応じて付与するクラスの切り替え用boolean
      loginListTabs: [
        {id: 0, name: '投稿記事'},
        {id: 1, name: 'FBした記事'},
        {id: 2, name: 'お気に入り記事'},
        {id: 3, name: '下書き記事'}
      ],//記事タブ表示用リスト(login user用)
      notLoginListTabs: [
        {id: 0, name: '公開中の投稿記事'},
        {id: 1, name: '公開中のFBした記事'},
        {id: 2, name: '公開中のお気に入り記事'},
      ],//記事タブ表示用リスト(not login user用)
      stateList: [
        {id: 10, name: '全記事'},
        {id: 1, name: 'Qiita未投稿記事'},
        {id: 2, name: 'Qiita投稿済み記事'},
      ],//記事state select-box表示用リスト
      isLoading: false,//loading処理表示切替用のboolean
      title_limit_length: value => value.length <= 100 || "100文字以内で入力してください",//記事title検索用のvalidation
      tags_limit_length: value => value.length <= 5 || "6個以上入力しないでください",//記事tag検索用のvalidation
    };
  },
  computed: {
    apiToken() {
      return this.$store.getters["auth/apiToken"];
    },
    length: {
      get() {
        return Math.ceil(this.displayArticles.length / this.paging.pageSize);
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
        return art.slice(this.paging.pageSize * (this.paging.now - 1), this.paging.pageSize * (this.paging.now));
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
      "usedTags",]),
    ...mapState("user", ["userDetail",]),
    ...mapGetters("auth", ["loginUser"]),
  },
  watch: {
    //storeのpostedArticlesにDBからの情報をsetしたときに最初画面遷移時に表示する記事種に切り替える
    postedArticles() {
      const th = this;
      let query;
      query = Object.assign({}, th.$route.query);
      try {
        query = query["defaultList"].toString();
      } catch (err) {
        query = 0
      }
      const change = async function () {
        if (query === 'DraftArticles' && th.userDetail.isLoginUser) {
          await th.changeList(3);//下書き一覧遷移
        } else if (query === 'MyArticles') {
          await th.changeList(2);//My記事一覧遷移
        } else {
          await th.changeList(0);//マイページ遷移もしくはその他のdefaultListが入力されたとき
        }
      }
      const processAll = async function () {
        await change();
      }
      processAll();
    },
    sortNum() {
      this.paging.now = 1;//sort変更時computedによる並び替え変更が行われるのでページが変更されないため、ここで1pに変えている
    },
    'paging.now'() {
      setTimeout(() => {
        if (this.windowWidthClass) {
          window.scrollTo({
            top: 0,
            behavior: "smooth"
          });
        } else {
          window.scrollTo({
            top: 650,
            behavior: "smooth"
          });
        }
      }, 100)
    },
    apiToken: function () {
      if (this.apiToken) {
        const paramUserId = this.$route.params['userId'];
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
        const processAll = async function () {
          await fetch();
          th.isLoading = false;
        }
        processAll();
      }
    },
    '$route': async function (to) {
      const th = this
      this.isLoading = true
      const paramUserId = to.params['userId']
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
      this.isLoading = false
    }
  },
  methods: {
    /**
     * 表示したい記事に対応する数値を渡して、表示する記事一覧とオートコンプリート用タグリストを変更する
     * @param listNum (0:投稿記事), (1:FB記事), (2:お気に入り記事), (3:下書き記事)
     */
    async changeList(listNum) {
      this.conditions.title = "";
      this.conditions.conditionTags = [];
      this.displayListNum = listNum;
      this.displayListState = {id: 10, name: '全記事'};
      this.sortNum = 0;
      let articlesFromVuex = [];

      if (listNum === 0) articlesFromVuex = this.notDraftArticles;
      if (listNum === 1) articlesFromVuex = this.feedbackArticles;
      if (listNum === 2) articlesFromVuex = this.myArticles;
      if (listNum === 3) articlesFromVuex = this.draftArticles;
      await this.setArticlesAndTags(articlesFromVuex);
      if (articlesFromVuex.length === 0) {
        this.sortedArticles.splice(0);
      }
      this.paging.now = 1;
      this.length = Math.ceil(this.displayArticles.length / this.paging.pageSize);
    }
    ,
    /**
     * 表示したい記事に対応する数値を渡して、表示する記事一覧を変更する
     * @param listState (10:全記事), (1:Qiita未投稿記事), (2:Qiita投稿済み記事)
     */
    async changeListState(listState) {
      this.conditions.title = "";
      this.conditions.conditionTags = [];
      this.displayListState = this.stateList.find(({id}) => id === listState);
      this.sortNum = 0;

      let articlesFromVuex = [];
      if (listState === 10) {//全記事表示に切り替える
        if (this.displayListNum === 0) articlesFromVuex = this.notDraftArticles;
        if (this.displayListNum === 1) articlesFromVuex = this.feedbackArticles;
        if (this.displayListNum === 2) articlesFromVuex = this.myArticles;
      }
      if (listState !== 10) {//全記事以外の表示に切り替えるためにstateFlagでfilterをかけて絞り込む
        if (this.displayListNum === 0 && this.notDraftArticles.length !== 0) {
          articlesFromVuex = this.notDraftArticles.filter((art) => {
            return art.stateFlag === listState
          })
        }
        if (this.displayListNum === 1 && this.feedbackArticles.length !== 0) {
          articlesFromVuex = this.feedbackArticles.filter((art) => {
            return art.stateFlag === listState
          })
        }
        if (this.displayListNum === 2 && this.myArticles.length !== 0) {
          articlesFromVuex = this.myArticles.filter((art) => {
            return art.stateFlag === listState
          })
        }
      }
      await this.setArticlesAndTags(articlesFromVuex);
      if (articlesFromVuex.length === 0) {
        this.sortedArticles.length = 0;
      }
      this.paging.now = 1;
      this.length = Math.ceil(this.displayArticles.length / this.paging.pageSize);
    }
    ,
    /**
     * 入力された検索条件とタグ条件に応じた記事を検索する
     */
    searchWithConditions() {
      if (this.$refs.search_form.validate()) {
        let articlesFromVuex = [];
        if (this.displayListNum === 0) articlesFromVuex = this.notDraftArticles;
        if (this.displayListNum === 1) articlesFromVuex = this.feedbackArticles;
        if (this.displayListNum === 2) articlesFromVuex = this.myArticles;
        if (this.displayListNum === 3) articlesFromVuex = this.draftArticles;

        if (this.displayListState.id !== 10) {//全記事表示以外の場合絞り込む
          if (this.displayListNum === 0) articlesFromVuex = articlesFromVuex.filter((art) => {
            return art.stateFlag === this.displayListState.id
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
        this.paging.now = 1;
        this.length = Math.ceil(articlesFromVuex.length / this.paging.pageSize);
      }
    }
    ,
    resetConditions() {
      this.conditions.title = "";
      this.conditions.conditionTags.splice(0);
      this.searchWithConditions();
    }
    ,
    resetPage() {
      this.displayListNum = 0;
      this.displayListState = {id: 10, name: '全記事'};
      this.changeList(0)
      window.scrollTo({
        top: 0,
        behavior: "smooth"
      });
    }
    ,
    ...mapActions("user", [
      "setArticlesAndTags",
      "setArticles",
      "fetchUserDetail",
      "fetchPostedArticles",
      "fetchFeedbackArticles",
      "fetchMyArticles",
      "findUserIdByUid",
      "clearState"
    ]),
  }
  ,
  created() {
    //画面横幅が960px以上であればwindowWidthClassをtrueに変え画面を記事一覧を横に配置
    (this.windowWidth >= 960) ? this.windowWidthClass = true : this.windowWidthClass = false
    this.isLoading = true;
  }
  ,
  mounted() {
    //画面の横幅が変わるが度に960px以上かを判定
    window.onresize = () => {
      this.windowWidth = window.innerWidth;
      (this.windowWidth >= 960) ? this.windowWidthClass = true : this.windowWidthClass = false;
    }
  }
  ,
  beforeDestroy() {
    this.clearState()//遷移前にstoreを空にしないと次にユーザー詳細画面来たとき前回のユーザーが表示されてしまう
  }
  ,
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
</style>
