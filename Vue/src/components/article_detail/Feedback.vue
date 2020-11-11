<template>
  <v-container>
    <v-card elevation="10" outlined class="card">
      <v-row align="center" class="spacer">
        <v-col cols="auto">
          <v-avatar size="30px" color="green">
            <img
              v-if="feedback.postedUser.photoURL"
              :src="feedback.postedUser.photoURL"
              alt="user-icon"
            />
            <v-icon v-else dark size="30px">
              mdi-account-circle
            </v-icon>
          </v-avatar>
        </v-col>
        <v-col cols="auto">
          <v-card-subtitle
            >@{{ feedback.postedUser.displayName }}</v-card-subtitle
          >
        </v-col>
        <!-- 投稿日または更新日 -->
        <v-col cols="auto">
          <v-card-subtitle
            >{{ lastEditAt.time | date }}{{ lastEditAt.text }}</v-card-subtitle
          >
        </v-col>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <!-- メニューボタン -->
        <v-col cols="auto" v-if="loginUser.uid == feedback.postedUser.uid">
          <v-menu offset-y>
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
        <v-col cols="auto">
          <v-card-text>{{ feedback.content }}</v-card-text>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      menus: [
        { name: "編集する", action: this.editFeedback },
        { name: "削除する", action: this.deleteFeedback },
      ],
    };
  },
  props: ["feedback"],
  computed: {
    lastEditAt() {
      if (this.feedback.updatedAt) {
        return { time: this.feedback.updatedAt, text: "に更新" };
      }
      return { time: this.feedback.createdAt, text: "に作成" };
    },
    loginUser() {
      return this.$store.state.auth.loginUser;
    },
  },
  filters: {
    date: function(value) {
      if (!value) return "";
      var ymd = value.split("T")[0].split("-");
      return ymd[0] + "年" + ymd[1] + "月" + ymd[2] + "日";
    },
  },
  methods: {
    editFeedback() {
      this.$emit("editFeedback", this.feedback);
    },
    deleteFeedback() {
      this.$store.dispatch("article/deleteFeedback", this.feedback);
    },
  },
};
</script>

<style scoped>
.card {
  padding: 10px;
}
</style>
