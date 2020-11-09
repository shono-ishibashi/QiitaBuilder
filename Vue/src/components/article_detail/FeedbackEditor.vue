<template>
  <v-card outlined class="sticky">
    <!-- ヘッダー -->
    <v-toolbar elevation="0">
      <v-icon>mdi-message-text-outline</v-icon>
      <v-spacer></v-spacer>
      <v-toolbar-title>コメントを投稿する</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn color="" icon>
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-toolbar>
    <v-card-title>
      <v-row>
        <v-col cols="1">
          <v-avatar size="30px">
            <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
          </v-avatar>
        </v-col>
        <v-col cols="">
          <small>@login user's name</small>
        </v-col>
      </v-row>
    </v-card-title>
    <!-- タブ -->
    <v-tabs v-model="tab" background-color="transparent" color="basil" grow>
      <v-tab v-for="(tab, index) in tabs" :key="index">
        {{ tab }}
      </v-tab>
    </v-tabs>
    <!-- 入力欄/プレビュー -->
    <v-card>
      <v-form v-show="tab == 0">
        <v-container>
          <v-row>
            <v-col cols="12" md="12">
              <v-textarea
                v-model="feedback"
                label="テキストを入力"
              ></v-textarea>
            </v-col>
          </v-row>
        </v-container>
      </v-form>
      <v-container v-show="tab == 1">
        <v-row>
          <v-col cols="12" md="12">
            {{ preview }}
          </v-col>
        </v-row>
      </v-container>
      <!-- 投稿ボタン -->
      <v-card-actions>
        <v-btn outlined text>
          投稿する
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-card>
</template>

<script>
export default {
  data() {
    return {
      login_user: null,
      feedback: "",
      tab: null,
      tabs: ["編集", "プレビュー"],
    };
  },
  created() {
    this.login_user = this.$store.state.auth.login_user;
  },
  computed: {
    preview() {
      return "コメント: " + this.feedback + " をプレビュー";
    },
  },
};
</script>

<style scoped>
.sticky {
  position: sticky;
  top: 5%;
}
</style>
