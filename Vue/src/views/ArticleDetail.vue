<template>
  <v-app class="grey lighten-3 area">
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
            <v-btn
              v-if="recommendId"
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
            <!-- 未登録 -->
            <v-btn
              v-if="!recommendId"
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
          </v-col>

          <!-- My記事ボタン -->
          <v-col cols="12" style="text-align: center;">
            <!-- 登録済み -->
            <v-btn
              v-if="myArticleId"
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
            <!-- 未登録 -->
            <v-btn
              v-if="!myArticleId"
              class="mx-2"
              fab
              @click="toggleMyArticle"
            >
              <v-icon large color="blue-grey">
                mdi-heart
              </v-icon>
            </v-btn>
          </v-col>
        </v-row>
      </v-col>
      <v-col cols="12" sm="12" :md="mdPlacement.article">
        <v-sheet min-height="70vh" rounded="lg">
          <Article :article="article" />
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
        feedbackId: null,
        content: "",
        deleteFlag: 0,
      },
      propsFeedback: {},
    };
  },
  computed: {
    slug() {
      return this.$route.params.articleId;
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
      this.fetchArticle(this.slug);
      this.fetchMyArticle(this.slug);
      this.fetchRecommend(this.slug);
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
        behavior: "smooth",
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
        this.$store.dispatch("article/postFeedback", this.propsFeedback);
      } else {
        this.$store.dispatch("article/updateFeedback", this.propsFeedback);
      }
    },
    async editFeedback(feedback) {
      this.feedbackForUpdate = feedback;
      this.propsFeedback = await this.feedbackForUpdate;
      this.EditorIsOpen = true;
    },
    toggleMyArticle() {
      if (this.myArticleId) {
        this.$store.dispatch("article/deleteMyArticle", this.myArticleId);
      } else {
        this.$store.dispatch(
          "article/registerMyArticle",
          this.article.articleId
        );
      }
    },
    toggleRecommend() {
      if (this.recommendId) {
        this.$store.dispatch("article/deleteRecommend", this.recommendId);
      } else {
        this.$store.dispatch(
          "article/registerRecommend",
          this.article.articleId
        );
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
