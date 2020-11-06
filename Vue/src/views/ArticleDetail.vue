<template>
  <v-app>
    <v-main class="grey lighten-3">
      <v-container>
        <v-row>
          <v-col class="hidden-xs-only hidden-sm-only" md="2">
            <v-btn elevation="2" id="qiita_btn" style="text-transform: none"
              >Qiita</v-btn
            >
            <v-btn elevation="2" id="my_btn" style="text-transform: none"
              >My記事登録</v-btn
            >
          </v-col>
          <v-col>
            <v-sheet min-height="70vh" rounded="lg" sm="12" md="10">
              <Article :article="article" />
              <Feedback :feedbacks="feedbacks" />
            </v-sheet>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
    <FeedbackEditor />
  </v-app>
</template>

<script>
import { mapActions } from "vuex";
import Article from "../components/article_detail/Article";
import Feedback from "../components/article_detail/Feedback";
import FeedbackEditor from "../components/article_detail/FeedbackEditor";

export default {
  components: {
    Article,
    Feedback,
    FeedbackEditor,
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
  },
  created() {
    this.fetchArticle(this.slug);
  },
  methods: {
    ...mapActions("article", ["fetchArticle"]),
  },
};
</script>

<style scoped>
#qiita_btn {
  position: sticky;
  top: 35%;
}
#my_btn {
  position: sticky;
  top: 45%;
}
</style>
