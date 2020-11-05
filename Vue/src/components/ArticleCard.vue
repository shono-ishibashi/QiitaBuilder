<template>
  <v-list-item class="articleCard">
    <v-list-item-avatar>
      <v-icon
          class="grey lighten-1"
          dark
      >
        mdi-folder
      </v-icon>
    </v-list-item-avatar>
    <v-list-item-content>
      <v-container>
        <v-row class="title">
          <v-list-item-title v-text="article.title"></v-list-item-title>
          <v-chip-group active-class="primary--text">
            <v-chip
                v-for="tag in article.tags"
                :key="tag.tagId"
                color="green"
            >
              {{ tag.tagName }}
            </v-chip>
          </v-chip-group>
        </v-row>
        <v-row>
            <v-col>
              {{ article.createdAt|moment()}}
            </v-col>
            <v-col>
              {{article.updatedAt|moment()}}
            </v-col>
            <v-col>
          {{article.qiitaRecommendPoint}}
            </v-col>
            <v-col>
              {{article.registeredMyArticleCount}}
            </v-col>
        </v-row>
      </v-container>
    </v-list-item-content>
  </v-list-item>
</template>

<script>
import moment from 'moment'

export default {
  name: "ArticleCard",
  props: {
    article: {
      type: Object,
      required: true
    }
  },
  filters:{
    moment(value){
      return moment(value).format("YY/MM/dd");
    }
  }
}
</script>

<style scoped>
.articleCard{
  background-color:#EEEEEE;
}
.title{
  background-color:#DDDDDD;
  height:70px;
}
</style>