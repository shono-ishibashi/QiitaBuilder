<template>
  <v-container>
    <v-card elevation="10" outlined class="card">
      <v-row align="center" class="spacer">
        <v-col cols="1" sm="1" md="1">
          <v-avatar size="36px" color="green">
            <img
              v-if="feedback.postedUser.photoURL"
              :src="feedback.postedUser.photoURL"
              alt="user-icon"
            />
            <v-icon v-else dark size="36px">
              mdi-account-circle
            </v-icon>
          </v-avatar>
        </v-col>
        <v-col class="" cols="2" sm="2" md="2">
          <strong>@{{ feedback.postedUser.displayName }}</strong>
        </v-col>
        <!-- 投稿日または更新日 -->
        <v-col class="" cols="7" sm="7" md="7">
          {{ lastEditAt.time | date }}{{ lastEditAt.text }}
        </v-col>
        <!-- メニューボタン -->
        <v-col class="" cols="2" sm="2" md="2">
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
      </v-row>
      <div>
        content :
        {{ feedback.content }}
      </div>
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
      console.log(
        "editFeedback (feedbackId : " + this.feedback.feedbackId + ")"
      );
    },
    deleteFeedback() {
      console.log(
        "deleteFeedback (feedbackId : " + this.feedback.feedbackId + ")"
      );
    },
  },
};
</script>

<style scoped>
.card {
  padding: 10px;
}
</style>
