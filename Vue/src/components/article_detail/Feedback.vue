<template>
  <v-container class="area">
    <v-card
      elevation="10"
      outlined
      class="card"
      v-for="feedback in feedbacks"
      :key="feedback.feedbackId"
    >
      <div>username : @{{ feedback.postedUser.displayName }}</div>
      <div>
        created at :
        {{ feedback.createdAt }}
      </div>
      <div>
        content :
        {{ feedback.content }}
      </div>
    </v-card>
  </v-container>
</template>

<script>
export default {
  props: ["feedbacks"],
};
</script>

<style scoped>
.area {
  background-color: rgb(165, 188, 218);
  padding: 20px;
}
.card{
  padding: 10px;
}
</style>
