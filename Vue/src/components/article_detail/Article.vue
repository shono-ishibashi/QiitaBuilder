<template>
  <v-app class="area d-flex">
    <div>username : @{{ article.postedUser.displayName }}</div>
    <div>created at :{{ article.createdAt }}</div>
    <h1>title : {{ article.title }}</h1>
    <div>
      tags :
      <ul>
        <li v-for="tag in article.tags" :key="tag.tagId">{{ tag.tagName }}</li>
      </ul>
    </div>
    <v-main>
      content :
      {{ article.content }}
    </v-main>
  </v-app>
</template>

<script>
export default {
  props: ["article"],
};
</script>

<style scoped>
.area {
  background-color: rgb(165, 218, 165);
  padding: 20px;
}
</style>
