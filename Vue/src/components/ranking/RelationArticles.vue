<template>
  <v-container>
    <h3>
      <v-icon color="#5bc8ac">
        mdi-book-open-blank-variant
      </v-icon>
      関連記事一覧
    </h3>
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