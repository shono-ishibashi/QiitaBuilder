<template>
  <v-app class="grey lighten-3 area">
    <v-snackbar v-model="isPostedArticleToQiita">
      Qiitaへ記事を投稿しました!!!
    </v-snackbar>
    <v-snackbar v-model="nonValidUser" timeout="2000">
      自分の記事にQiita推薦はできません
    </v-snackbar>
    <v-snackbar v-model="nonValidToken" timeout="5000">
      トークンが失効しました。ログインからやり直してください。
    </v-snackbar>
    <v-snackbar v-model="processFailure" timeout="5000">
      処理に失敗しました。ページを再読み込みしてください。
    </v-snackbar>
    <v-row>
      <v-col class="hidden-xs-only hidden-sm-only" :md="mdPlacement.buttons">
        <v-row id="qiita_btn">
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

          <!-- My記事ボタン -->
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
              <span>My記事登録</span>
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
              <span>My記事登録</span>
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
          />
          <Feedbacks :feedbacks="feedbacks" @editFeedback="editFeedback" />
        </v-sheet>
      </v-col>
      <v-col cols="12" sm="12" :md="mdPlacement.editor">
        <span v-show="EditorIsOpen">
          <FeedbackEditor
            class="sticky"
            @closeEditor="closeEditor"
            @postFeedback="postFeedback"
            :feedback="propsFeedback"
          />
        </span>
        <span v-show="!EditorIsOpen">
          <v-btn
            color="gray"
            icon
            large
            @click="openNewEditor"
            class="toggle_editor_btn"
          >
            <v-icon>mdi-comment-plus</v-icon>
          </v-btn>
        </span>
      </v-col>
    </v-row>
  </v-app>
</template>

<script>
import { mapActions } from "vuex";
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
      //snackbarに使用するメソッド
      isPostedArticleToQiita: false,
      nonValidUser: false,
      nonValidToken: false,
      processFailure: false,
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
      if (this.EditorIsOpen) return { buttons: 1, article: 7, editor: 4 };
      return { buttons: 2, article: 8, editor: 2 };
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
  },
  watch: {
    apiToken: function() {
      this.fetchArticle(this.slug).catch((error) => {
        this.errorHandle(error);
      });
      this.fetchMyArticle(this.slug).catch((error) => {
        this.errorHandle(error);
      });
      this.fetchRecommend(this.slug).catch((error) => {
        this.errorHandle(error);
      });
      this.$store.dispatch("auth/checkIsLinkedToQiita").catch((error) => {
        this.errorHandle(error);
      });
      if (this.$route.query.isPostedArticleToQiita) {
        this.isPostedArticleToQiita = true;
      }
    },
  },
  created() {
    this.propsFeedback = this.feedbackForNewPost;
    this.scrollTop();
  },
  methods: {
    scrollTop() {
      window.scrollTo({
        top: 0,
        behavior: "auto",
      });
    },
    errorHandle(error) {
      const status = error.response.status;
      if (status == 404) {
        this.$router.push({ name: "404" });
      } else if (status == 401) {
        this.nonValidToken = true;
      } else {
        this.processFailure = true;
      }
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
          .catch((error) => {
            this.errorHandle(error);
          });
      } else {
        this.$store
          .dispatch("article/updateFeedback", this.propsFeedback)
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
        postedUser: feedback.postedUser,
        updatedAt: feedback.updatedAt,
      };
      this.EditorIsOpen = true;
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
    ]),
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
