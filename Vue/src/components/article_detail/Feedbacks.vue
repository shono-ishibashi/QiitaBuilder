<template>
  <v-container class="area">
    <Feedback
      v-for="feedback in feedbacks"
      :feedback="feedback"
      :key="feedback.feedbackId"
    ></Feedback>
  </v-container>
</template>

<script>
import Feedback from "./Feedback";

export default {
  components: {
    Feedback,
  },
  props: ["feedbacks"],
};
</script>

<style scoped>
.area {
  background-color: #5bc8ac;
  padding: 20px;
}
</style>
