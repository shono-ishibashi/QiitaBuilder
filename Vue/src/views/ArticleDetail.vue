<template>
  <v-app class="grey lighten-3 area">
    <v-snackbar v-model="nonValidUser" timeout="2000">
      自分の記事にQiita推薦はできません
    </v-snackbar>
    <v-snackbar v-model="nonValidToken" timeout="5000">
      トークンが失効しました。ログインからやり直してください。
    </v-snackbar>
    <v-snackbar v-model="processFailure" timeout="5000">
      処理に失敗しました。ページを再読み込みしてください。
    </v-snackbar>
    <v-snackbar v-model="postFeedbackSuccess" timeout="5000">
      フィードバックを投稿しました。
    </v-snackbar>
    <v-snackbar v-model="updateFeedbackSuccess" timeout="5000">
      フィードバックを更新しました。
    </v-snackbar>
    <v-snackbar v-model="deleteFeedbackSuccess" timeout="5000">
      フィードバックを削除しました。
    </v-snackbar>
    <v-row v-show="isLoading" justify="center">
      <v-col cols="6">
        <v-progress-linear
            color="green"
            indeterminate
            rounded
            height="10"
        ></v-progress-linear>
      </v-col>
    </v-row>
    <v-row v-show="!isLoading">
      <v-col class="hidden-xs-only hidden-sm-only" :md="mdPlacement.buttons">
        <v-row
            v-if="article.stateFlag !== 0 && article.stateFlag !== 9"
            id="qiita_btn"
        >
          <!-- Qiitaボタン -->
          <v-col
              cols="12"
              style="text-align: center; padding: 0"
              class="green--text"
          >
            {{ article.qiitaRecommendPoint }}
          </v-col>
          <v-col cols="12" style="text-align: center;">
            <!-- 登録済み -->
            <v-tooltip top v-if="recommendId">
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                    v-bind="attrs"
                    v-on="on"
                    class="mx-2"
                    fab
                    dark
                    color="green"
                    @click="toggleRecommend"
                >
                  <v-icon large dark>
                    mdi-thumb-up
                  </v-icon>
                </v-btn>
              </template>
              <span>Qiita推薦</span>
            </v-tooltip>
            <v-tooltip top v-if="!recommendId">
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                    v-bind="attrs"
                    v-on="on"
                    class="mx-2"
                    fab
                    outlined
                    color="green"
                    @click="toggleRecommend"
                >
                  <v-icon large color="green">
                    mdi-thumb-up
                  </v-icon>
                </v-btn>
              </template>
              <span>Qiita推薦</span>
            </v-tooltip>
          </v-col>

          <!-- お気に入りボタン -->
          <v-col cols="12" style="text-align: center;">
            <!-- 登録済み -->
            <v-tooltip top v-if="myArticleId">
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                    v-bind="attrs"
                    v-on="on"
                    class="mx-2"
                    fab
                    dark
                    color="pink"
                    @click="toggleMyArticle"
                >
                  <v-icon large dark>
                    mdi-heart
                  </v-icon>
                </v-btn>
              </template>
              <span>お気に入り</span>
            </v-tooltip>
            <!-- 未登録 -->
            <v-tooltip top v-if="!myArticleId">
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                    v-bind="attrs"
                    v-on="on"
                    class="mx-2"
                    fab
                    @click="toggleMyArticle"
                >
                  <v-icon large color="blue-grey">
                    mdi-heart
                  </v-icon>
                </v-btn>
              </template>
              <span>お気に入り</span>
            </v-tooltip>
          </v-col>
        </v-row>
      </v-col>
      <v-col cols="12" sm="12" :md="mdPlacement.article">
        <v-sheet min-height="70vh" rounded="lg">
          <Article
              :article="article"
              :myArticleId="myArticleId"
              :recommendId="recommendId"
              @toggleMyArticle="toggleMyArticle"
              @toggleRecommend="toggleRecommend"
              @deleteArticle="deleteArticle"
          />
          <Feedbacks
              :feedbacks="feedbacks"
              @editFeedback="editFeedback"
              @deleteFeedback="deleteFeedback"
              v-if="article.stateFlag !== 0 && article.stateFlag !== 9"
          />
        </v-sheet>
      </v-col>
      <v-col
          v-if="article.stateFlag !== 0 && article.stateFlag !== 9"
          cols="12"
          sm="12"
          :md="mdPlacement.editor"
      >
        <span v-show="EditorIsOpen">
          <FeedbackEditor
              class="sticky"
              @closeEditor="closeEditor"
              @postFeedback="postFeedback"
              :feedback="propsFeedback"
          />
        </span>
        <span v-show="!EditorIsOpen">
          <v-tooltip top>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                  v-bind="attrs"
                  v-on="on"
                  icon
                  large
                  @click="openNewEditor"
                  class="toggle_editor_btn"
              >
                <v-icon large>mdi-comment-plus</v-icon>
              </v-btn>
            </template>
            <span>フィードバックを入力する</span>
          </v-tooltip>
        </span>
      </v-col>
    </v-row>
  </v-app>
</template>

<script>
import {mapActions, mapMutations} from "vuex";
import Article from "../components/article_detail/Article";
import Feedbacks from "../components/article_detail/Feedbacks";
import FeedbackEditor from "../components/article_detail/FeedbackEditor";

export default {
  components: {
    Article,
    Feedbacks,
    FeedbackEditor,
  },
  data() {
    return {
      isLoading: false,
      EditorIsOpen: false,
      // 状態保存のためのForUpdateとForNewPost
      feedbackForUpdate: {
        feedbackId: null,
        content: "",
      },
      feedbackForNewPost: {
        articleId: null,
        content: "",
        deleteFlag: 0,
      },
      propsFeedback: {},
      nonValidUser: false,
      nonValidToken: false,
      postFeedbackSuccess: false,
      updateFeedbackSuccess: false,
      deleteFeedbackSuccess: false,
    };
  },
  computed: {
    slug() {
      return this.$route.params.articleId;
    },
    loginUser() {
      return this.$store.state.auth.loginUser;
    },
    article() {
      return this.$store.state.article.article;
    },
    feedbacks() {
      return this.$store.state.article.article.feedbacks;
    },
    mdPlacement() {
      if (this.EditorIsOpen) return {buttons: 1, article: 7, editor: 4};
      return {buttons: 2, article: 8, editor: 2};
    },
    apiToken() {
      return this.$store.getters["auth/apiToken"];
    },
    myArticleId() {
      return this.$store.state.article.myArticleId;
    },
    recommendId() {
      return this.$store.state.article.recommendId;
    },
    processFailure() {
      return this.$store.state.article.processFailure;
    },
  },
  watch: {
    apiToken: async function () {
      if (this.apiToken) {
        this.isLoading = true;
        const a = this.fetchArticle(this.slug).catch((error) => {
          this.articleErrorHandle(error);
        });
        this.fetchMyArticle(this.slug).catch((error) => {
          this.errorHandle(error);
        });
        this.fetchRecommend(this.slug).catch((error) => {
          this.errorHandle(error);
        });
        const d = this.$store
            .dispatch("auth/checkIsLinkedToQiita")
            .catch((error) => {
              this.errorHandle(error);
            });
        if (this.$route.query.isPostedArticleToQiita) {
          this.isPostedArticleToQiita = true;
        }
        await Promise.all([a, d]);
        this.isLoading = false;
      }
    },
  },
  created() {
    this.isLoading = true;
    this.propsFeedback = this.feedbackForNewPost;
    this.scrollTop();
  },
  beforeDestroy() {
    this.resetArticle()
  },
  methods: {
    //// 画面描画の調整
    scrollTop() {
      window.scrollTo({
        top: 0,
        behavior: "auto",
      });
    },
    //// エラー処理
    // 汎用的なエラー処理
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
    // 記事取得時のエラー処理
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

    //// 通常処理
    deleteArticle() {
      // 削除前のstateFlag
      const beforeStateFlag = this.article.stateFlag;
      const item = this.article;
      item.stateFlag = 9;
      this.$store
          .dispatch("article/saveArticle", item)
          .then(() => {
            this.$router.push({name: "articleList"});
          })
          .catch((error) => {
            this.article.stateFlag = beforeStateFlag;
            this.errorHandle(error);
          });
    },
    closeEditor() {
      if (!this.propsFeedback.feedbackId) {
        this.feedbackForNewPost = this.propsFeedback;
      } else {
        this.feedbackForUpdate = this.propsFeedback;
      }

      this.EditorIsOpen = !this.EditorIsOpen;
    },
    openNewEditor() {
      this.propsFeedback = this.feedbackForNewPost;
      this.EditorIsOpen = true;
    },
    postFeedback() {
      if (!this.propsFeedback.feedbackId) {
        this.propsFeedback.articleId = this.article.articleId;
        this.$store
            .dispatch("article/postFeedback", this.propsFeedback)
            .then(() => {
              this.postFeedbackSuccess = true;
            })
            .catch((error) => {
              this.errorHandle(error);
            });
      } else {
        this.$store
            .dispatch("article/updateFeedback", this.propsFeedback)
            .then(() => {
              this.updateFeedbackSuccess = true;
            })
            .catch((error) => {
              this.errorHandle(error);
            });
      }
      this.closeEditor();
    },
    async editFeedback(feedback) {
      this.feedbackForUpdate = feedback;
      // プロパティのみ代入し引数feedbackとのリアクティブを解除
      this.propsFeedback = {
        articleId: feedback.articleId,
        content: feedback.content,
        createdAt: feedback.createdAt,
        deleteFlag: feedback.deleteFlag,
        feedbackId: feedback.feedbackId,
        feedbackVersion: feedback.feedbackVersion,
        postedUser: feedback.postedUser,
        updatedAt: feedback.updatedAt,
      };
      this.EditorIsOpen = true;
    },
    async deleteFeedback(feedback) {
      this.$store
          .dispatch("article/deleteFeedback", feedback)
          .then(() => {
            this.deleteFeedbackSuccess = true;
          })
          .catch((error) => {
            this.errorHandle(error);
          });
    },
    toggleMyArticle() {
      if (this.myArticleId) {
        this.$store
            .dispatch("article/deleteMyArticle", this.myArticleId)
            .catch((error) => {
              this.errorHandle(error);
            });
      } else {
        this.$store
            .dispatch("article/registerMyArticle", this.article.articleId)
            .catch((error) => {
              this.errorHandle(error);
            });
      }
    },
    toggleRecommend() {
      if (this.loginUser.uid === this.article.postedUser.uid) {
        this.nonValidUser = true;
        return;
      }
      if (this.recommendId) {
        this.$store
            .dispatch("article/deleteRecommend", this.recommendId)
            .catch((error) => {
              this.errorHandle(error);
            });
      } else {
        this.$store
            .dispatch("article/registerRecommend", this.article.articleId)
            .catch((error) => {
              this.errorHandle(error);
            });
      }
    },
    ...mapActions("article", [
      "fetchArticle",
      "fetchMyArticle",
      "fetchRecommend",
      "toggleProcessFailure",
    ]),
    ...mapMutations('article', ['resetArticle'])
  },
};
</script>

<style scoped>
.sticky {
  position: sticky;
  top: 5%;
}

.toggle_editor_btn {
  position: sticky;
  top: 30%;
}

.area {
  padding: 20px;
}

#qiita_btn {
  position: sticky;
  top: 30%;
}
</style>
