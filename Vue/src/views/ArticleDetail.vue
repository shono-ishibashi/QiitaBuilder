<template>
  <v-app class="grey lighten-3 area">
    <v-row>
      <v-col class="hidden-xs-only hidden-sm-only" md="1">
            <v-btn elevation="2" id="qiita_btn" style="text-transform: none"
              >Qiita</v-btn
            >
            <v-btn elevation="2" id="my_btn" style="text-transform: none"
              >My記事登録</v-btn
            >
          </v-col>
      <v-col cols="12" sm="12" md="7">
        <v-sheet min-height="70vh" rounded="lg">
          <Article :article="article" />
          <Feedbacks :feedbacks="feedbacks" />
        </v-sheet>
      </v-col>
      <v-col cols="12" sm="12" md="4">
        <FeedbackEditor />
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
.area{
  padding: 20px;
}
#qiita_btn {
  position: sticky;
  top: 35%;
}
#my_btn {
  position: sticky;
  top: 45%;
}
</style>
