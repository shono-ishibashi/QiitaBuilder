<template>
  <v-container>
    <v-card elevation="10" outlined class="card">
      <v-row align="center" class="spacer">
        <v-col cols="auto">
          <v-avatar size="30px" color="green">
            <img
              v-if="feedback.postedUser.photoUrl"
              :src="feedback.postedUser.photoUrl"
              data-test-id="icon"
              alt="user-icon"
            />
            <v-icon v-else dark size="30px">
              mdi-account-circle
            </v-icon>
          </v-avatar>
        </v-col>
        <v-col cols="auto">
          <v-card-subtitle data-test-id="userName"
            >@{{ feedback.postedUser.displayName }}
          </v-card-subtitle>
        </v-col>
        <!-- 投稿日または更新日 -->
        <v-col cols="auto">
          <v-card-subtitle data-test-id="lastEdit"
            >{{ lastEditAt.time | date }}{{ lastEditAt.text }}
          </v-card-subtitle>
        </v-col>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <!-- メニューボタン -->
        <v-col cols="auto" v-if="loginUser.uid == feedback.postedUser.uid" data-test-id="menuIcon">
          <v-menu offset-y >
            <template v-slot:activator="{ attrs, on }">
              <v-btn icon v-bind="attrs" v-on="on" class="">
                <v-icon>mdi-format-list-bulleted</v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item
                v-for="(item, index) in menus"
                :key="index"
                @click="item.action"
              >
                <v-list-item-title v-text="item.name"></v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </v-col>
        <v-spacer></v-spacer>
      </v-row>
      <v-row>
        <v-col cols="12">
          <Editor
            mode="viewer"
            ref="editor"
            hint="Hint"
            data-test-id="mdEditor"
            :outline="true"
            :render-config="renderConfig"
            v-model="feedback.content"
          />
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

<script>
import { Editor } from "vuetify-markdown-editor";

export default {
  name: "Feedback",
  components: {
    Editor,
  },
  data() {
    return {
      menus: [
        { name: "編集する", action: this.editFeedback },
        { name: "削除する", action: this.deleteFeedback },
      ],
      renderConfig: {
        // Mermaid config
        mermaid: {
          theme: "dark",
        },
      },
    };
  },
  props: ["feedback"],
  computed: {
    lastEditAt() {
      if (this.feedback.updatedAt) {
        return { time: this.feedback.updatedAt, text: " (編集済み)" };
      }
      return { time: this.feedback.createdAt, text: "" };
    },
    loginUser() {
      return this.$store.state.auth.loginUser;
    },
  },
  filters: {
    date: function(value) {
      if (!value) return "";
      var dateAndTime = value.split("T");
      var ymd = dateAndTime[0];
      var hms = dateAndTime[1].split(":");
      return ymd + " " + hms[0] + ":" + hms[1];
    },
  },
  methods: {
    editFeedback() {
      this.$emit("editFeedback", this.feedback);
    },
    deleteFeedback() {
      this.$emit("deleteFeedback", this.feedback);
    },
  },
};
</script>

<style scoped>
.card {
  padding: 10px;
}
</style>
<style src="highlight.js/styles/github.css"></style>
