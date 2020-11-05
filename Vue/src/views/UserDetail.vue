<template>
  <v-container>
    <v-row style="height: 150px;" justify="center">
      <v-col cols=5>{{ userDetail.photoUrl }}</v-col>
      <v-col cols=5>
        <v-data-table
            :headers="tagsHeaders"
            :items="userDetail.usedTags"
            style="background-color: lightgray"
            hide-default-footer
            disable-pagination></v-data-table>
      </v-col>

      <v-col cols=5>{{ userDetail.displayName }}</v-col>
      <v-col cols=4>
        <v-btn color="secondary" elevation="2">Qiita連携</v-btn>
      </v-col>

      <v-col cols=4>
        <v-card>
          <v-layout justify-center>
            Qiita投稿数 / Builder投稿数<br></v-layout>
          <v-layout justify-center>
            {{ userDetail.postedArticleCount }} / {{ postedArticles.length }}
          </v-layout>
        </v-card>
      </v-col>
      <v-col cols=4>
        <v-card>
          <v-layout justify-center>FB数</v-layout>
          <v-layout justify-center>
            {{ userDetail.feedbackCount }}
          </v-layout>
        </v-card>
      </v-col>
      <v-col cols=4>
        <v-card>
          <v-layout justify-center>総獲得推奨数</v-layout>
          <v-layout justify-center>
            {{ userDetail.qiitaRecommendedAllCount }}
          </v-layout>
        </v-card>
      </v-col>

      <v-col cols="2">
        <v-btn color="secondary">投稿記事</v-btn>
      </v-col>
      <v-col cols="2">
        <v-btn color="secondary">FB記事</v-btn>
      </v-col>
      <v-col cols="2">
        <v-btn color="secondary">My記事</v-btn>
      </v-col>
      <v-col cols="6"></v-col>

      <v-col cols="10">
        <v-card>

        </v-card>
      </v-col>

    </v-row>

  </v-container>
</template>

<script>
import {mapState, mapActions} from "vuex"

export default {
  name: "userDetail",
  components: {},
  data() {
    return {
      tagsHeaders: [
        {text: "タグ名", value: "tagName"},
        {text: "使用回数", value: "usedTagCount"}
      ],
      articles:[],
    };
  },
  computed: {
    ...mapState("user", ["userDetail", "postedArticles", "feedbackArticles", "myArticles"])
  },
  methods: {
    ...mapActions("user", ["fetchUserDetail", "fetchPostedArticles", "fetchFeedbackArticles", "fetchMyArticles"])
  },
  created() {
    this.fetchUserDetail(this.$route.query.userId);
    this.fetchPostedArticles(this.$route.query.userId);
    this.fetchFeedbackArticles(this.$route.query.userId);
    this.fetchMyArticles(this.$route.query.userId);
    this.articles=this.postedArticles;
  }
}
</script>