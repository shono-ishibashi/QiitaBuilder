<template>
  <v-container>
    <v-text-field
        v-model="article.title"
        outlined
        label="タイトル"
    ></v-text-field>
    <v-combobox
        v-model="article.tags"
        :items="tags"
        item-value="tagId"
        item-text="tagName"
        return-object
        label="タグの入力"
        deletable-chips
        multiple
        chips>
    </v-combobox>
    <v-tabs>
      <v-tab
          v-for="format in formatList" :key="format.key"
      >
        {{ format.state }}
      </v-tab>
    </v-tabs>
    <v-row>
      <EditAndPreview></EditAndPreview>
    </v-row>
    <v-row>
      <v-btn @click="postArticle(1)">記事を公開</v-btn>
      <v-btn @click="postArticle(0)">下書き保存</v-btn>
    </v-row>
  </v-container>
</template>
<script>
import EditAndPreview from '../components/article_edit/format/EditAndPreview'
import {mapState,mapActions} from 'vuex'

export default {
  name: "ArticleEdit",
  components: {
    EditAndPreview
  },
  data() {
    return {
      formatList: [
        {key: 0, state: '2画面'},
        {key: 1, state: '編集のみ'},
        {key: 2, state: 'プレビュー'},
        {key: 3, state: 'FB編集'},
      ]
    }
  },
  created(){
    console.log(this.article)
    this.resetArticle()
    if(this.slug){
      this.fetchArticle(this.slug);
    }
  },
  watch: {
    selectedTags(value) {
      console.log(value)
    },
  },
  computed: {
    ...mapState("articles", ["tags"]),
    ...mapState("article", ["article"]),
    slug() {
      return this.$route.params.articleId;
    },
  },
  methods: {
    ...mapActions("article", ["fetchArticle", "saveArticle","resetArticle"]),
    async postArticle(state) {
      this.article.stateFlag = state
      for (var i = 0; i < this.article.tags.length; i++) {
        if (typeof this.article.tags[i] == 'string') {
          this.article.tags.splice(i, 1, {
            tagId: null, tagName: this.article.tags[i]
          })
        }
      }
      console.log(this.article.tags)
      await this.saveArticle(this.article)
      await this.$router.push('/article')
    }
}
}
</script>


<style scoped>

</style>