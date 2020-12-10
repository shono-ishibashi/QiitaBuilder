<template>
  <v-card outlined>
    <!-- ヘッダー -->
    <v-toolbar elevation="0">
      <v-icon>mdi-message-text-outline</v-icon>
      <v-spacer></v-spacer>
      <v-toolbar-title
        >フィードバックを{{ feedback.feedbackId | postOrEdit }}する
      </v-toolbar-title>
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
      <v-form v-show="tab === 0" ref="form" v-model="valid">
        <v-container>
          <v-textarea
            background-color="grey lighten-2"
            color="green darken-2"
            solo
            v-model="feedback.content"
            :rules="[required, maximum, blank]"
            label="テキストを入力"
          ></v-textarea>
        </v-container>
      </v-form>
      <v-container v-show="tab === 1">
        <Editor
          mode="viewer"
          ref="editor"
          hint="Hint"
          :outline="true"
          :render-config="renderConfig"
          v-model="feedback.content"
        />
      </v-container>
      <!-- 投稿ボタン -->
      <v-card-actions>
        <v-btn
          color="green"
          class="white--text"
          :disabled="!valid"
          @click="saveFeedback"
        >
          {{ feedback.feedbackId | postOrEdit }}する
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-card>
</template>

<script>
import { Editor } from "vuetify-markdown-editor";

export default {
  components: {
    Editor,
  },
  data() {
    return {
      loginUser: null,
      tab: 0,
      tabs: ["編集", "プレビュー"],
      renderConfig: {
        // Mermaid config
        mermaid: {
          theme: "dark",
        },
      },
      valid: false,
      required: (value) => !!value || "テキストを入力してください",
      maximum: (value) =>
        value === undefined
          ? true
          : value.length <= 20000 || "2万文字以内で入力してください",
      blank: (value) => {
        const pattern = /\S/;
        return pattern.test(value) || "空文字のみの入力はできません";
      },
    };
  },
  props: ["feedback"],
  created() {
    this.loginUser = this.$store.state.auth.loginUser;
  },
  beforeUpdate() {
    this.resetValidation();
  },
  filters: {
    postOrEdit: function(value) {
      if (!value) return "投稿";
      return "更新";
    },
  },
  methods: {
    closeEditor() {
      this.resetValidation();
      this.$emit("closeEditor");
    },
    saveFeedback() {
      if (this.valid) {
        this.$emit("postFeedback");
      }
      this.feedback.content = undefined;
    },
    resetValidation() {
      this.$refs.form.resetValidation();
    },
  },
};
</script>
