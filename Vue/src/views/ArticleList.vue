<template>
  <v-container>
    <v-row>
      <v-col cols="3"></v-col>
      <v-col cols="6">
        <v-card class="searchForm">
          <v-row>
            <v-col>
              <v-card-title style="padding-left: 40px">
                検索フォーム
              </v-card-title>
            </v-col>
            <v-col>
              <v-card-actions>
                <v-btn @click="searchWord">検索</v-btn>
                <v-btn @click="reset">リセット</v-btn>
              </v-card-actions>
            </v-col>
          </v-row>
          <v-card-text>
            <v-row>
              <v-col>
                <v-text-field
                    label="記事タイトル,ユーザーネームを入力"
                    v-model="searchCriteria.searchWord"
                >
                </v-text-field>
              </v-col>
              <v-col>
                <v-autocomplete
                    v-model="searchCriteria.searchTag"
                    :items="tags"
                    item-value="tagId"
                    item-text="tagName"
                    label="タグを選択"
                    chips
                    deletable-chips
                    multiple
                    small-chips
                >
                </v-autocomplete>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="3"></v-col>
    </v-row>
    <v-row aline="center" justify="center">
      <v-list class="list">
        <v-subheader>
          <v-tabs
              fixed-tabs
              color="#5bc8ac"
          >
            <v-tab v-for="period in periodList" :key="period.key" @click="changePeriod(period.key)">
              {{ period.state }}
            </v-tab>
          </v-tabs>
          <v-spacer></v-spacer>
          <v-select
              :items="sortList"
              item-value="key"
              item-text="state"
              color="#5bc8ac"
              v-model="searchCriteria.sortNum">
          </v-select>
        </v-subheader>
        <ArticleCard v-for="(article,index) in articles" :key="article.articleId" :article="article"
                     :index="index"></ArticleCard>
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
    <div class="Page-Btn" @click="scrollTop">
      <i class="fas fa-chevron-up Page-Btn-Icon">
        <v-icon dark>mdi-arrow-up</v-icon>
      </i>
    </div>
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
    }
  },
  watch: {
    ['searchCriteria.sortNum']() {
      this.fetchArticles(this.searchCriteria)
    },
    ['searchCriteria.period']() {
      this.fetchArticles(this.searchCriteria)
    },
    ['searchCriteria.pageSize']() {
      this.searchCriteria.currentPage = 1
      this.fetchArticles(this.searchCriteria)
      this.scrollTop()
    },
    ['searchCriteria.currentPage']() {
      this.fetchArticles(this.searchCriteria)
      this.scrollTop()
    }
  },
  created() {
    this.fetchArticles(this.searchCriteria)
    this.fetchTags()
  },
  computed: {
    ...mapState("articles", ["articles", "tags", "totalPage","searchCriteria"])
  },
  components: {
    ArticleCard
  },
  methods: {
    ...mapActions("articles", ["fetchArticles", "fetchTags"]),
    changePeriod(key) {
      this.searchCriteria.period = key
    },
    searchWord() {
      this.fetchArticles(this.searchCriteria)
    },
    scrollTop() {
      window.scrollTo({
        top: 0,
        behavior: "smooth"
      });
    },
    reset(){
      this.searchCriteria.searchWord=""
      this.searchCriteria.searchTag=[]
      this.fetchArticles(this.searchCriteria)
    }
  }
}
</script>

<style scoped>
.Page-Btn {
  position: fixed;
  right: 200px;
  bottom: 100px;
  width: 45px;
  height: 45px;
  line-height: 45px;
  text-align: center;
  border-radius: 50%;
  background: #5bc8ac;
}
.list {
  width: 800px;
  padding-top:100px;
}
.searchForm{
}
</style>