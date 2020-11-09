<template>
  <v-card outlined>
    <!-- ヘッダー -->
    <v-toolbar elevation="0">
      <v-icon>mdi-message-text-outline</v-icon>
      <v-spacer></v-spacer>
      <v-toolbar-title>コメントを投稿する</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn color="" icon @click="closeEditor">
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-toolbar>
    <v-card-title>
      <v-row>
        <v-col cols="2">
          <v-avatar size="36px" color="green">
            <img
              v-if="loginUser.photoURL"
              :src="loginUser.photoURL"
              alt="user-icon"
            />
            <v-icon v-else dark size="36px">
              mdi-account-circle
            </v-icon>
          </v-avatar>
        </v-col>
        <v-col cols="">
          <small>@{{ loginUser.displayName }}</small>
        </v-col>
      </v-row>
    </v-card-title>
    <!-- タブ -->
    <v-tabs v-model="tab" background-color="transparent" color="green" grow>
      <v-tab v-for="(tab, index) in tabs" :key="index">
        {{ tab }}
      </v-tab>
    </v-tabs>
    <!-- 入力欄/プレビュー -->
    <v-card>
      <v-form v-show="tab == 0">
        <v-container>
          <v-textarea
            background-color="grey lighten-2"
            color="green darken-2"
            solo
            v-model="feedback"
            label="テキストを入力"
          ></v-textarea>
        </v-container>
      </v-form>
      <v-container v-show="tab == 1">
        <v-row>
          <v-col cols="12" md="12">
            <span v-html="compiledFeedback"></span>
          </v-col>
        </v-row>
      </v-container>
      <!-- 投稿ボタン -->
      <v-card-actions>
        <v-btn dark color="green" @click="postFeedback">
          投稿する
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-card>
</template>

<script>
import marked from "marked";

export default {
  data() {
    return {
      loginUser: null,
      feedback: "",
      tab: 0,
      tabs: ["編集", "プレビュー"],
    };
  },
  created() {
    this.loginUser = this.$store.state.auth.loginUser;
  },
  computed: {
    compiledFeedback() {
      return marked(this.feedback);
    },
  },
  methods: {
    closeEditor() {
      this.$emit("toggleEditor");
    },
    postFeedback() {
      console.log("postFeedback");
    },
  },
};
</script>
