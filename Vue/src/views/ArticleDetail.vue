<template>
  <v-app class="grey lighten-3 area">
    <v-row>
      <v-col class="hidden-xs-only hidden-sm-only" :md="mdPlacement.buttons">
        <v-row id="qiita_btn">
          <v-col cols="12">
            <v-btn elevation="2" style="text-transform: none">Qiita</v-btn>
          </v-col>
          <v-col cols="12">
            <v-btn elevation="2" style="text-transform: none">My記事</v-btn>
          </v-col>
        </v-row>
      </v-col>
      <v-col cols="12" sm="12" :md="mdPlacement.article">
        <v-sheet min-height="70vh" rounded="lg">
          <Article :article="article" />
          <Feedbacks :feedbacks="feedbacks" />
        </v-sheet>
      </v-col>
      <v-col cols="12" sm="12" :md="mdPlacement.editor">
        <span v-show="EditorIsOpen">
          <FeedbackEditor class="sticky" @toggleEditor="toggleEditor" />
        </span>
        <span v-show="!EditorIsOpen">
          <v-btn
            color="gray"
            icon
            large
            @click="toggleEditor"
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
  },
  watch: {
    apiToken: function() {
      this.fetchArticle(this.slug);
    },
  },
  methods: {
    toggleEditor() {
      this.EditorIsOpen = !this.EditorIsOpen;
    },
    ...mapActions("article", ["fetchArticle"]),
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
