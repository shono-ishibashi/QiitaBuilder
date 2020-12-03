<template>
  <v-container>
    <ArticleCard v-for="(relArticle, index) in relArticles" :key="relArticle.articleId" :article="relArticle"
                 :index="index"></ArticleCard>
  </v-container>
</template>

<script>
import ArticleCard from "@/components/ArticleCard";

export default {
  name: "RelationArticles",

  components: {
    ArticleCard
  },

  props: {
    relArticles: {
      type: Array
    }
  }
}
</script>

<style scoped>

</style>