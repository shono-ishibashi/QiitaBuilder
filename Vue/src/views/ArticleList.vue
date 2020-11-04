<template>
  <v-container>
    <v-row>
      <v-col>
        <v-select
            :items="sortList"
            item-value="key"
            item-text="state"
            v-model="searchCriteria.sortNum">
        </v-select>
        <v-tabs
            fixed-tabs
            background-color="indigo"
            dark
        >
          <v-tab v-for="period in periodList" :key="period.key">
            {{ period.state }}
          </v-tab>
        </v-tabs>
      </v-col>
      <v-col>
        <v-text-field
            label="記事タイトル,ユーザーネームを入力"
            v-model="searchCriteria.searchWord"
            type="text"
        >
          <template v-slot:append>
              <v-btn>検索</v-btn>
          </template>
        </v-text-field>
        <v-autocomplete
            v-model="searchCriteria.searchTag"
            :items="tags"
            item-value="tagId"
            item-text="tagName"
            chips
            deletable-chips
            multiple
            small-chips
        >

        </v-autocomplete>
      </v-col>
    </v-row>
    <v-row>
      <v-list>
        <ArticleCard v-for="article in articles" :key="article.articleId" :article="article"></ArticleCard>
      </v-list>
    </v-row>
  </v-container>
</template>

<script>
import ArticleCard from "../components/ArticleCard"
import {mapState, mapActions} from "vuex"

export default {
  name: "ArticleList",
  data() {
    return {
      pageSizeList: [10, 20, 30],
      sortList: [
        {key: 0, state: "新着順"},
        {key: 1, state: "更新順"},
        {key: 2, state: "Qiita推奨数順"},
        {key: 3, state: "My記事登録順"}
      ],
      periodList: [
        {key: 0, state: "週間"},
        {key: 1, state: "月間"},
        {key: null, state: "全て"},
      ],
      searchCriteria: {
        sortNum: 0,
        period: undefined,
        searchWord: undefined,
        searchTag: [],
        pageSize: 10,
        currentPage: 1
      }
    }
  },
  watch: {},
  created() {
    this.fetchArticles(this.searchCriteria)
    this.fetchTags()
    console.log("connect success")
  },
  computed: {
    ...mapState("articles", ["articles", "tags"])
  },
  components: {
    ArticleCard
  },
  methods: {
    ...mapActions("articles", ["fetchArticles","fetchTags"])
  }
}
</script>

<style scoped>

</style>