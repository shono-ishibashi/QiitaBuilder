<template>
  <v-container>
    <v-row>
      <v-col cols="4">
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
          <v-tab v-for="period in periodList" :key="period.key" @click="changePeriod(period.key)">
            {{ period.state }}
          </v-tab>
        </v-tabs>
      </v-col>
      <v-col cols="4"></v-col>
      <v-col cols="4">
        <v-text-field
            label="記事タイトル,ユーザーネームを入力"
            v-model="searchCriteria.searchWord"
        >
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
        <v-btn @click="searchWord">検索</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-list>
        <ArticleCard v-for="article in articles" :key="article.articleId" :article="article"></ArticleCard>
      </v-list>
    </v-row>
    <v-row>
      <v-col cols="2"></v-col>
      <v-col cols="2">
        <v-select :items="pageSizeList" v-model="searchCriteria.pageSize" label="ページ表示数">
        </v-select>
      </v-col>
      <v-col cols="8">
        <v-pagination
            v-model="searchCriteria.currentPage"
            :length="totalPage"
            circle
        ></v-pagination>
      </v-col>
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
        period: 0,
        searchWord: "",
        searchTag: [],
        pageSize: 10,
        currentPage: 1
      }
    }
  },
  watch: {
    ['searchCriteria.sortNum']() {
      console.log(this.searchCriteria.sortNum)
      this.fetchArticles(this.searchCriteria)
    },
    ['searchCriteria.period']() {
      console.log(this.searchCriteria.period)
      this.fetchArticles(this.searchCriteria)
    },
    ['searchCriteria.pageSize']() {
      console.log(this.searchCriteria.pageSize)
      this.searchCriteria.currentPage=1
      this.fetchArticles(this.searchCriteria)
    },
    ['searchCriteria.currentPage']() {
      console.log(this.searchCriteria.currentPage)
      this.fetchArticles(this.searchCriteria)
    }
  },
  created() {
    this.fetchArticles(this.searchCriteria)
    this.fetchTags()
    console.log("connect success")
  },
  computed: {
    ...mapState("articles", ["articles", "tags","totalPage"])
  },
  components: {
    ArticleCard
  },
  methods: {
    ...mapActions("articles", ["fetchArticles", "fetchTags"]),
    changePeriod(key) {
      this.searchCriteria.period = key
    },
    searchWord(){
      console.log(this.searchCriteria.period)
      this.fetchArticles(this.searchCriteria)
    }
  }
}
</script>

<style scoped>

</style>