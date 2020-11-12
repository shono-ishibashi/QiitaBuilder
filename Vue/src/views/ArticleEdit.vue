<template>
  <v-container id="article-edit-field" fluid>
    <h1>{{ editTitle }}(60分で消えます)(ここの文字はみんなで決める)
    </h1>
    <div class="title-tag-form">
      <v-text-field
          class="title"
          v-model="article.title"
          outlined
          color="#5bc8ac"
          placeholder="タイトル"
          background-color="#ffffff"
          :rules="[required,title_limit_length]"
          counter="100"
      ></v-text-field>
      <v-combobox
          v-model="article.tags"
          :items="tags"
          color="#5bc8ac"
          item-value="tagId"
          item-text="tagName"
          item-color="green"
          return-object
          label="プログラミング技術に関するタグを5つまで入力(例：Java)"
          deletable-chips
          :rules="[required,tags_limit_size]"
          multiple
          counter="5"
          chips>
      </v-combobox>
    </div>
    <v-tabs
        color="#5bc8ac"
        background-color="#f5f5f5"
        :class="selectedFormat===0||selectedFormat===3?'change-format-field-tab':null"
    >
      <v-tab
          @click="changeFormat(0)"
      >
        編集 & プレビュー
      </v-tab>
      <v-tab
          @click="changeFormat(1)"
      >
        編集
      </v-tab>
      <v-tab
          @click="changeFormat(2)"
      >
        プレビュー
      </v-tab>
      <v-tab
          v-if="slug!=null"
          @click="changeFormat(3)"
      >
        編集 & プレビュー & FB
      </v-tab>
    </v-tabs>
    <v-row>
      <v-content class="content">
        <component :is="currentView"></component>
      </v-content>
    </v-row>
    <div class="article-edit-action-field">
      <v-row v-if="this.slug==null" justify="center">
        <v-btn @click="postArticle(1)" class="create-article-btn">記事を公開</v-btn>
        <v-btn @click="postArticle(0)" class="create-article-btn">下書き保存</v-btn>
      </v-row>
      <v-row v-if="this.slug!=null" justify="center">
        <v-btn @click="postArticle(article.stateFlag)">Qiita Builderの記事を更新</v-btn>
      </v-row>
      <v-row v-if="this.slug!=null" justify="center" class="post-article-toQiita-btn">
        <v-btn @click.stop="toggleQiitaDialog">{{ postToQiita }}</v-btn>
      </v-row>
    </div>
    <v-dialog v-model="qiitaDialog" max-width="400">
      <v-card>
        <v-card-title>
          {{ qiitaConfirmMessage }}
        </v-card-title>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="red darken-1"
              text
              @click="toggleQiitaDialog"
          >
            キャンセル
          </v-btn>
          <v-btn
              color="green darken-1"
              text
              @click="dialog = false"
          >
            投稿する
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>
<script>
import EditAndPreview from '../components/article_edit/format/FormatEditAndPreview'
import Edit from '../components/article_edit/format/FormatEdit'
import Preview from '../components/article_edit/format/FormatPreview'
import EditAndPreviewAndFeedback from '../components/article_edit/format/FormatEditAndPreviewAndFeedback'
import {mapState, mapActions} from 'vuex'

export default {
  name: "ArticleEdit",
  components: {
    EditAndPreview,
    Edit,
    Preview,
    EditAndPreviewAndFeedback
  },
  data() {
    return {
      currentView: EditAndPreview,
      selectedFormat: 0,
      qiitaDialog: false,
      required: value => !!value || "必ず入力してください",
      title_limit_length: value => value.length <= 100 || "100文字以内で入力してください",
      tags_limit_size: value => value.length <= 5 || "5つまで入力してください"
    }
  },
  created() {
    this.resetArticle()
    if (this.slug) {
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
    editTitle() {
      if (this.slug) {
        return "編集"
      } else {
        return "新規作成"
      }
    },
    //qiitaのトークンを取得して比較
    postToQiita() {
      if (this.article.qiitaArticleId != null) {
        return "Qiitaに記事を更新"
      } else {
        return "Qiitaに記事を投稿"
      }
    },
    //qiitaのトークンを取得して比較
    qiitaConfirmMessage() {
      if (this.article.qiitaArticleId != null) {
        return "Qiitaに記事を更新しますか？"
      } else {
        return "Qiitaに記事を投稿しますか？"
      }
    }
  },
  methods: {
    ...mapActions("article", ["fetchArticle", "saveArticle", "resetArticle"]),
    async postArticle(state) {
      this.article.stateFlag = state
      console.log(this.article.tags)
      for (var i = 0; i < this.article.tags.length; i++) {
        if (typeof this.article.tags[i] == 'string') {
          this.article.tags.splice(i, 1, {
            tagId: null, tagName: this.article.tags[i]
          })
        }
      }
      await this.saveArticle(this.article)
      await this.$router.push('/article')
    },
    changeFormat(key) {
      this.selectedFormat = key
      if (key === 0) {
        console.log(this.article)
        this.currentView = EditAndPreview
      } else if (key === 1) {
        this.currentView = Edit
      } else if (key === 2) {
        this.currentView = Preview
      } else if (key === 3) {
        this.currentView = EditAndPreviewAndFeedback
      }
    },
    toggleQiitaDialog() {
      this.qiitaDialog = !this.qiitaDialog
    },
  }
}
</script>


<style scoped>
#article-edit-field {
  background-color: #f5f5f5;
  padding: 30px;
}

.title-tag-form {
  margin-top: 60px;
  margin-bottom: 45px;
}

.title-tag-form .title {
  margin-bottom: 30px;
}

h1 {
  margin-bottom: 30px;
  margin-left: 30px;
}

.create-article-btn {
  margin-right: 30px;
}

.post-article-toQiita-btn {
  margin-top: 30px;
}

.change-format-field-tab {
  position: sticky;
  top: 1%;
}

.article-edit-action-field {
  margin-top: 30px;
  margin-bottom: 30px;
}
</style>