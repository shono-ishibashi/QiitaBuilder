<template>
  <v-container>
    Article Detail
    <Article :article="article"/>
    <Feedback :feedbacks="feedbacks"/>
    <FeedbackEditor />
  </v-container>
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
    feedbacks(){
      return this.$store.state.article.article.feedbacks;
    }
  },
  created() {
    this.fetchArticle(this.slug);
  },
  methods: {
    ...mapActions("article", ["fetchArticle"]),
  },
};
</script>
