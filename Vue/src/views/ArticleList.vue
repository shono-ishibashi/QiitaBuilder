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
                    v-model="toggleSearchWord"
                    row
                >
                  <v-radio
                      label="記事検索"
                      value="0"
                      color="success"
                  ></v-radio>
                  <v-radio
                      label="ユーザー検索"
                      value="1"
                      color="success"
                  ></v-radio>
                </v-radio-group>
              </v-row>
              <v-row>
                <v-col>
                    <v-text-field
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
            <v-tab v-for="period in periodList" :key="period.key" @click="changePeriod(period.key)">
              {{ period.state }}
            </v-tab>
          </v-tabs>
          <v-spacer></v-spacer>
          <v-select
              :items="sortList"
              item-value="key"
              item-text="state"
              item-color="green"
              label="並び替え"
              color="#5bc8ac"
              v-model="searchCriteria.sortNum">
          </v-select>
          <v-select
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
        <v-container
            v-if="articles.length===0"
            class="no-article-field"
        >
          記事が見つかりませんでした<br>
          再度検索してください
        </v-container>
        <ArticleCard v-for="(article,index) in articles" :key="article.articleId" :article="article"
                     :index="index"></ArticleCard>
      </v-list>
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

    <v-dialog v-model="errorDialog" width="400">
      <v-card>
        <v-card-title>
          警告
        </v-card-title>
        <v-card-text>
          記事に対する権限がないか、記事が存在しません。
        </v-card-text>
        <v-card-actions>
          <v-btn @click="toggleErrorTransitionDialog">OK</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import ArticleCard from "../components/ArticleCard"
import {mapState, mapActions} from "vuex"

export default {
  name: "ArticleList",
  data() {
    return {
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
      sortList: [
        {key: 0, state: "新着順"},
        {key: 1, state: "更新順"},
        {key: 2, state: "Qiita推奨数順"},
        {key: 3, state: "My記事登録順"}
      ],
      periodList: [
        {key: null, state: "全て"},
        {key: 0, state: "週間"},
        {key: 1, state: "月間"},
      ],
      nonValidToken: false,
      can_submit_search: false,
      title_limit_length: value => value.length <= 100 || "100文字以内で入力してください",
      user_limit_length: value => value.length <= 30 || "30文字以内で入力してください",
      tags_limit_length: value => value.length <= 5 || "6個以上入力しないでください",
    }
  },
  watch: {
    ['searchCriteria.sortNum']() {
      this.searchCriteria.currentPage = 1;
      this.fetchArticles(this.searchCriteria).catch(error => {
        this.errorHandle(error)
      })
    },
    ['searchCriteria.period']() {
      this.searchCriteria.currentPage = 1;
      this.fetchArticles(this.searchCriteria).catch(error => {
        this.errorHandle(error)
      })
    },
    ['searchCriteria.pageSize']() {
      this.searchCriteria.currentPage = 1;
      this.fetchArticles(this.searchCriteria).catch(error => {
        this.errorHandle(error)
      })
      this.scrollTop();
    },
    ['searchCriteria.currentPage']() {
      this.fetchArticles(this.searchCriteria).catch(error => {
        this.errorHandle(error)
      })
      setTimeout(() => {
        this.scrollTop();
      }, 50)
    },
    apiToken() {
      this.fetchArticles(this.searchCriteria).catch(error => {
        this.errorHandle(error)
      })
      this.fetchTags();
    }
  },
  created() {
  },
  computed: {
    ...mapState("article", ["processFailure"]),
    ...mapState("articles", ["articles", "tags", "totalPage", "searchCriteria", "errorTransistionDialog"]),
    apiToken() {
      return this.$store.getters["auth/apiToken"];
    },
    errorDialog: {
      get() {
        return this.errorTransistionDialog
      },
      set() {
        this.toggleErrorTransitionDialog()
      }
    },
    toggleSearchWord: {
      get() {
        return this.searchCriteria.toggleSearchWord
      },
      set(value) {
        console.log(value)
        this.setToggleSearchWord(value)
        console.log(this.searchCriteria.toggleSearchWord)
      }
    }
  },
  components: {
    ArticleCard
  },
  methods: {
    ...mapActions("article", ["toggleProcessFailure"]),
    ...mapActions("articles", ["fetchArticles", "fetchTags", "toggleErrorTransitionDialog", "setToggleSearchWord"]),
    changePeriod(key) {
      this.searchCriteria.period = key
    },
    enable_submit() {
      this.can_submit_search = true;
    },
    submit() {
      if (!this.can_submit_search) return
      if (this.$refs.search_form.validate()) {
        this.searchCriteria.currentPage = 1
        this.fetchArticles(this.searchCriteria).catch(error => {
          this.errorHandle(error)
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
    reset() {
      this.searchCriteria.searchWord = ""
      this.searchCriteria.searchTag = []
      this.searchCriteria.currentPage = 1
      this.fetchArticles(this.searchCriteria).catch(error => {
        this.errorHandle(error)
      })
    },
    errorHandle(error) {
      const status = error.response.status;
      if (status === 404) {
        this.$router.push({name: "404"});
      } else if (status === 401) {
        this.nonValidToken = true;
      } else {
        this.toggleProcessFailure()
      }
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

</style>
