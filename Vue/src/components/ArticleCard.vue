<template>
  <v-list-item>
    <v-list-item-avatar>
      <v-icon
          class="grey lighten-1"
          dark
      >
        mdi-folder
      </v-icon>
    </v-list-item-avatar>
    <v-list-item-content>
      <v-list-item-title v-text="article.title"></v-list-item-title>
      <v-list-item-subtitle></v-list-item-subtitle>
    </v-list-item-content>
    <v-list-item-content>
      <v-container>
        <v-row>
          <v-col>

          </v-col>
          <v-col>

          </v-col>
          <v-col>

          </v-col>
          <v-col>

          </v-col>
        </v-row>
      </v-container>
    </v-list-item-content>
  </v-list-item>
</template>

<script>
export default {
  name: "ArticleCard",
  props: {
    article: {
      type: Object,
      required: true
    }
  }
}
</script>

<style scoped>

</style>