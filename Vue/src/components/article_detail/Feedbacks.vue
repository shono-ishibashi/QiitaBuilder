<template>
  <v-container class="area">
    <v-row>
      <v-col cols="1">
        <v-icon large dark>mdi-comment</v-icon>
      </v-col>
      <v-col cols="">
        <h3 class="white--text">フィードバック</h3>
      </v-col>
    </v-row>

    <Feedback
        v-for="feedback in feedbacks"
        :feedback="feedback"
        :key="feedback.feedbackId"
        @editFeedback="editFeedback"
        @deleteFeedback="deleteFeedback"
    ></Feedback>
  </v-container>
</template>

<script>
import Feedback from "./Feedback";

export default {
  name: "Feedbacks",
  components: {
    Feedback,
  },
  props: ["feedbacks"],
  methods: {
    editFeedback(feedback) {
      this.$emit("editFeedback", feedback);
    },
    deleteFeedback(feedback) {
      this.$emit("deleteFeedback", feedback);
    },
  },
};
</script>

<style scoped>
.area {
  background-color: #5bc8ac;
  padding: 20px;
}
</style>
