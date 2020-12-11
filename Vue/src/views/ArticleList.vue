<template>
  <div class="articleList">
    <v-snackbar v-model="nonValidToken" timeout="5000">
      トークンが失効しました。ログインからやり直してください。
    </v-snackbar>
    <v-snackbar v-model="processFailure" timeout="5000">
      処理に失敗しました。ページを再読み込みしてください。
    </v-snackbar>
    <v-row justify="center" align-content="center">
      <v-col cols="6">
        <v-card class="searchForm" outline-color="#008b8b">
          <v-form ref="search_form">
            <v-card-text>
              <v-row justify="center">
                <v-card-title style="padding-left: 40px">
                  <v-icon>mdi-magnify</v-icon>
                  検索フォーム
                </v-card-title>
              </v-row>
              <v-row>
                <v-radio-group
                    data-test-id="toggle-search"
                    v-model="toggleSearchWord"
                    row
                >
                  <v-radio
                      data-test-id="toggle-search0"
                      label="記事検索"
                      value="0"
                      color="success"
                  ></v-radio>
                  <v-radio
                      data-test-id="toggle-search1"
                      label="ユーザー検索"
                      value="1"
                      color="success"
                  ></v-radio>
                </v-radio-group>
              </v-row>
              <v-row>
                <v-col>
                  <v-text-field
                      data-test-id="search-article"
                      v-if="this.toggleSearchWord==0"
                      label="記事を検索"
                      color="#5bc8ac"
                      v-model="searchCriteria.searchWord"
                      :rules="[title_limit_length]"
                      counter="100"
                      @keypress.prevent.enter.exact="enable_submit"
                      @keyup.prevent.enter.exact="submit"
                  >
                  </v-text-field>
                  <v-text-field
                      data-test-id="search-user"
                      v-if="this.toggleSearchWord==1"
                      label="ユーザーを検索"
                      color="#5bc8ac"
                      v-model="searchCriteria.searchWord"
                      :rules="[user_limit_length]"
                      counter="30"
                      @keypress.prevent.enter.exact="enable_submit"
                      @keyup.prevent.enter.exact="submit"
                  >
                  </v-text-field>
                </v-col>
                <v-col>
                  <v-autocomplete
                      v-model="searchCriteria.searchTag"
                      :items="tags"
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
                      @keypress.prevent.enter.exact="enable_submit"
                      @keyup.prevent.enter.exact="submit"
                  >
                  </v-autocomplete>
                </v-col>
              </v-row>
              <v-row justify="center">
                <v-card-actions>
                  <v-btn @click="clickSubmit" dark color="#5bc8ac">検索</v-btn>
                  <v-btn @click="reset" dark color="#ff6347">リセット</v-btn>
                </v-card-actions>
              </v-row>
            </v-card-text>
          </v-form>
        </v-card>
      </v-col>
    </v-row>

    <!--    <v-row justify="center" align-content="center">-->
    <!--      <v-col cols="8" v-if="articles.length!=0">-->
    <!--        <v-pagination-->
    <!--            v-model="searchCriteria.currentPage"-->
    <!--            :length="totalPage"-->
    <!--            :total-visible="7"-->
    <!--            color="#5bc8ac"-->
    <!--            class="control-margin"-->
    <!--            circle-->
    <!--        ></v-pagination>-->
    <!--      </v-col>-->
    <!--    </v-row>-->

    <v-row aline="center" justify="center">
      <v-list class="list control-margin">
        <v-subheader>
          <v-tabs
              fixed-tabs
              color="#5bc8ac"
          >
            <v-tab
                v-for="(period,index) in periodList"
                :data-test-id="'tab'+index"
                :key="period.key"
                @click="changePeriod(period.key)">
              {{ period.state }}
            </v-tab>
          </v-tabs>
          <v-spacer></v-spacer>
          <v-select
              data-test-id="sort"
              v-model="searchCriteria.sortNum"
              :items="sortList"
              item-value="key"
              item-text="state"
              item-color="green"
              label="並び替え"
              color="#5bc8ac"
          >
          </v-select>
          <v-select
              data-test-id="pageSize"
              :items="displayCountList"
              item-text="text"
              item-value="value"
              v-model="searchCriteria.pageSize"
              label="表示件数"
              color="#5bc8ac"
              item-color="green"
          >
          </v-select>
        </v-subheader>
        <div v-show="isDisplay">
          <!--          記事がない場合の処理: 変数をクッションに挟んで取得後にbooleanで描画を決定-->
          <v-container
              data-test-id="no-article-field"
              v-if="articles.length===0"
              class="no-article-field"
          >
            記事が見つかりませんでした<br>
            再度検索してください
          </v-container>
          <ArticleCard
              v-for="(article,index) in articles"
              :data-test-id="'articleCard'+index"
              :key="article.articleId"
              :article="article"
              :index="index"
          >
          </ArticleCard>
        </div>
      </v-list>
    </v-row>
    <v-row>
      <v-col cols="3"></v-col>
      <v-col
          cols="6"
          :class="{'progress-linear':isLoading}"
      >
        <v-progress-linear
            v-show="isLoading"
            color="green"
            indeterminate
            rounded
            height="10"
        ></v-progress-linear>
      </v-col>
      <v-col cols="3"></v-col>
    </v-row>
    <v-row justify="center" align-content="center">
      <v-col cols="8" v-if="articles.length!==0">
        <v-pagination
            v-model="searchCriteria.currentPage"
            :length="totalPage"
            :total-visible="7"
            color="#5bc8ac"
            class="control-margin"
            circle
        ></v-pagination>
      </v-col>
    </v-row>

  </div>
</template>

<script>
import ArticleCard from "../components/ArticleCard"
import {mapState, mapActions, mapMutations} from "vuex"

export default {
  name: "ArticleList",
  data() {
    return {
      // 表示件数
      displayCountList: [
        {
          text: '10件',
          value: 10
        }, {
          text: '20件',
          value: 20
        }, {
          text: '30件',
          value: 30
        }],
      // 並び順
      sortList: [
        {key: 0, state: "新着順"},
        {key: 1, state: "更新順"},
        {key: 2, state: "Qiita推奨数順"},
        {key: 3, state: "お気に入り登録順"}
      ],
      // 期間
      periodList: [
        {key: null, state: "全て"},
        {key: 0, state: "週間"},
        {key: 1, state: "月間"},
      ],
      // tokenの有無を行うboolean
      nonValidToken: false,
      // submit処理の認証を行うboolean
      can_submit_search: false,
      // 各コンポーネント表示切替用のboolean
      isDisplay: true,
      // loading処理表示切替用のboolean
      isLoading: false,
      // validation条件
      title_limit_length: value => value.length <= 100 || "100文字以内で入力してください",
      user_limit_length: value => value.length <= 30 || "30文字以内で入力してください",
      tags_limit_length: value => value.length <= 5 || "6個以上入力しないでください",
    }
  },
  beforeDestroy() {
    this.resetArticles()
    this.resetSearchCriteria()
  },
  computed: {
    ...mapState("article", ["processFailure"]),
    ...mapState("articles", ["articles", "tags", "totalPage", "searchCriteria", "errorTransistionDialog"]),
    apiToken() {
      return this.$store.getters["auth/apiToken"];
    },
    // 記事検索かユーザー検索か
    toggleSearchWord: {
      get() {
        return this.searchCriteria.toggleSearchWord
      },
      set(value) {
        this.setToggleSearchWord(value)
      }
    }
  },

  watch: {
    async apiToken() {
      if (this.apiToken) {
        this.isLoading = true
        this.isDisplay = false
        await this.fetchArticles(this.searchCriteria)
            .then(async () => {
              await this.sleep(1000)
              this.isLoading = await false
              this.isDisplay = await true
            })
            .catch(error => {
              console.log(2)
              this.articleErrorHandle(error)
            })
        await this.fetchTags()
            .catch(error => {
              this.errorHandle(error)
            });
      }
    },
    // 並び順
    async ['searchCriteria.sortNum']() {
      this.isLoading = true
      this.isDisplay = false
      this.searchCriteria.currentPage = 1;
      await this.fetchArticles(this.searchCriteria)
          .then(async () => {
            await this.sleep(1000)
            this.isLoading = await false
            this.isDisplay = await true
          })
          .catch(error => {
            this.articleErrorHandle(error)
          })
    },
    // 期間
    async ['searchCriteria.period']() {
      this.isLoading = true
      this.isDisplay = false
      this.searchCriteria.currentPage = 1;
      await this.fetchArticles(this.searchCriteria)
          .then(async () => {
            await this.sleep(1000)
            this.isLoading = await false
            this.isDisplay = await true
          })
          .catch(error => {
            this.articleErrorHandle(error)
          })
    },
    // 表示ページ数
    ['searchCriteria.pageSize']() {
      this.searchCriteria.currentPage = 1;
      this.fetchArticles(this.searchCriteria).catch(error => {
        this.articleErrorHandle(error)
      })
      this.scrollTop();
    },
    // 現在のページ
    async ['searchCriteria.currentPage']() {
      await this.fetchArticles(this.searchCriteria).catch(error => {
        this.articleErrorHandle(error)
      })
      await this.sleep(50)
      this.scrollTop();
    }
  },
  components: {
    ArticleCard
  },
  methods: {
    ...mapActions("article", ["toggleProcessFailure"]),
    ...mapActions("articles", ["fetchArticles", "fetchTags", "setToggleSearchWord"]),
    ...mapMutations('articles', ['resetArticles','resetSearchCriteria']),
    // 期間を変更するメソッド
    changePeriod(key) {
      this.searchCriteria.period = key
    },
    // 文字変換でenterを押下した場合はfalseのまま
    enable_submit() {
      this.can_submit_search = true;
    },
    // 検索処理
    async submit() {
      if (!this.can_submit_search) return
      if (this.$refs.search_form.validate()) {
        this.isLoading = true;
        this.isDisplay = false
        this.searchCriteria.currentPage = 1
        await this.fetchArticles(this.searchCriteria)
            .then(async () => {
              await this.sleep(1000)
              this.isLoading = await false
              this.isDisplay = await true
            })
            .catch(error => {
              this.articleErrorHandle(error)
            })
        this.can_submit_search = false;
      }
    },
    clickSubmit() {
      this.can_submit_search = true;
      this.submit()
    },
    scrollTop() {
      window.scrollTo({
        top: 0,
        behavior: "smooth"
      });
    },
    // 検索条件のリセット処理
    async reset() {
      this.isLoading = true
      this.isDisplay = false
      this.searchCriteria.searchWord = ""
      this.searchCriteria.searchTag = []
      this.searchCriteria.currentPage = 1
      await this.fetchArticles(this.searchCriteria)
          .then(async () => {
            await this.sleep(1000)
            this.isLoading = await false
            this.isDisplay = await true
          })
          .catch(error => {
            this.articleErrorHandle(error)
          })
    },
    // エラーハンドリング
    errorHandle(error) {
      const status = error.response.status;
      switch (status) {
        case 401:
          this.nonValidToken = true;
          this.$store.dispatch("auth/logout");
          break;
        case 500:
          this.$store.dispatch("window/setInternalServerError", true);
          break;
        default:
          this.toggleProcessFailure();
      }
    },
    // エラーハンドリング
    articleErrorHandle(error) {
      const status = error.response.status;
      switch (status) {
        case 400:
        case 404:
          this.$store.dispatch("window/setNotFound", true);
          break;
        case 403:
          this.$store.dispatch("window/setForbidden", true);
          break;
      }
      this.errorHandle(error);
    },
    sleep(msec) {
      return new Promise(function (resolve) {
        setTimeout(function () {
          resolve()
        }, msec);
      })
    },
  }
}
</script>

<style scoped>
.list {
  width: 800px;
  margin-top: 100px;
  margin-bottom: 70px;
}

.searchForm {
  margin-top: 20px;
  margin-bottom: 20px;
}

.no-article-field {
  text-align: center;
  height: 200px;
  margin-top: 100px;
  font-weight: bold;
  font-size: 16px;
}

.articleList {
  padding-bottom: 40px;
  /*background-color:#f5f5f5;*/
}

.control-margin {
  margin-top: 10px;
  margin-bottom: 10px;
  padding-top: 10px;
  padding-bottom: 10px;
}

.progress-linear {
  padding-top: 60px;
  height: 150px;
}
</style>
