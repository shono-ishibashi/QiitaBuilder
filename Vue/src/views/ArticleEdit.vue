<template>
  <v-container id="article-edit-field" fluid>
    <div class="edit-field">
      <v-form ref="edit_form" v-model="valid" lazy-validation>
        <v-row>
          <v-col cols="8" class="title-tag-form">
            <v-text-field
                class="title"
                v-model="article.title"
                outlined
                color="#5bc8ac"
                placeholder="タイトル"
                background-color="#ffffff"
                :rules="[required,title_limit_length,blank]"
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
                :rules="[tags_max_size,tags_min_size,blank]"
                multiple
                counter="5"
                chips
            >
            </v-combobox>
          </v-col>
          <v-col cols="4">
            <div class="article-edit-action-field">
              <v-row v-if="this.slug!=null" justify="center">
                <v-btn @click="postArticle(article.stateFlag)" class="btn" outlined color="#008b8b">Qiita Builder
                  に記事を更新
                </v-btn>
              </v-row>
              <v-row v-if="this.slug!=null" justify="center" class="post-article-toQiita-btn">
                <v-btn @click.stop="toggleQiitaDialog" width="220" class="btn" outlined color="#008b8b">{{
                    postToQiita
                  }}
                </v-btn>
              </v-row>
            </div>
          </v-col>
        </v-row>
        <v-tabs
            color="#5bc8ac"
            background-color="#f5f5f5"
            :class="selectedFormat===2||selectedFormat===3?'change-format-field-tab':null"
        >
          <v-tab
              @click="changeFormat(0)"
          >
            編集
          </v-tab>
          <v-tab
              @click="changeFormat(1)"
          >
            プレビュー
          </v-tab>
          <v-tab
              @click="changeFormat(2)"
          >
            編集 & プレビュー
          </v-tab>
          <v-tab
              v-if="slug!=null"
              @click="changeFormat(3)"
          >
            編集 & プレビュー & FB
          </v-tab>
        </v-tabs>
        <v-row>
          <v-main class="content">
            <component :is="currentView"></component>
          </v-main>
        </v-row>
      </v-form>
    </div>
    <v-dialog v-model="qiitaDialog" max-width="400">
      <v-card>
        <v-card-title>
          {{ qiitaConfirmTitle }}
        </v-card-title>
        <v-card-text>
          {{ qiitaConfirmMessage }}
        </v-card-text>
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
              @click="postedToQiita(article)"
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
import {mapState, mapGetters, mapActions} from 'vuex'
import axios from 'axios'

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
      valid: true,
      currentView: Edit,
      selectedFormat: 0,
      qiitaDialog: false,
      required: value => value && !!value || "必ず入力してください",
      blank: value => {
        const pattern = /\S/g
        return pattern.test(value[value.length-1]) || "空文字のみの入力はできません"
      },
      title_limit_length: value => value && value.length <= 100 || "100文字以内で入力してください",
      tags_max_size: value => value && value.length <= 5 || "5つまで入力してください",
      tags_min_size: value => value && value.length >= 1 || "1つ以上入力してください",
    }
  },
  watch: {
    async apiToken() {
      const uid=await this.loginUser.uid
      await this.findUserIdByUid(uid)
      const article=await this.slug
      const params= await {
        articleId:article,
        userId:this.userId
      }
      console.log(params)
      await axios.get(this.API_URL+'article/isExist',{
        params: params,
        headers: {
          "Authorization": this.apiToken,
          "Content-Type": "application/json"
        },
      }).then(()=>{
        this.resetArticle()
        this.fetchTags()
        this.fetchArticle(this.slug);
      }).catch(()=>{
        this.$router.push('/article')
        this.toggleErrorTransitionDialog()
      })
    }
  },
  computed: {
    ...mapState("articles", ["tags"]),
    ...mapState("article", ["article"]),
    ...mapGetters("auth", ["loginUser"]),
    ...mapGetters(["API_URL"]),
    ...mapGetters("user",["userId"]),
    slug() {
      return this.$route.params.articleId;
    },
    apiToken() {
      return this.$store.getters["auth/apiToken"];
    },
    //qiitaのトークンを取得し、表示するボタンの文字列を変更
    postToQiita() {
      if (this.article.qiitaArticleId != null) {
        return "Qiita に記事を更新"
      } else {
        return "Qiita に記事を投稿"
      }
    },
    //qiitaのトークンを取得し、qiita更新時のモーダルのタイトルを変更
    qiitaConfirmTitle() {
      if (this.article.qiitaArticleId != null) {
        return "Qiitaに記事を更新しますか？"
      } else {
        return "Qiitaに記事を投稿しますか？"
      }
    },
    //qiitaのトークンを取得し、qiita更新時のモーダルのメッセージを変更
    qiitaConfirmMessage() {
      if (this.article.qiitaArticleId != null) {
        return "編集内容をQiitaBuilderに保存し、Qiitaに記事を更新します"
      } else {
        return "編集内容をQiitaBuilderに保存し、Qiitaに記事を投稿します"
      }
    },
  },
  methods: {
    ...mapActions("article", ["fetchArticle", "saveArticle", "resetArticle"]),
    ...mapActions("articles", ["fetchTags","toggleErrorTransitionDialog"]),
    ...mapActions("user", ["findUserIdByUid"]),
    //記事を投稿or更新するメソッド
    async postArticle(state) {
      //validationチェック
      if (this.$refs.edit_form.validate()) {
        this.article.stateFlag = state
        //タグが登録されていないものにはtagIdにnullをset
        for (var i = 0; i < this.article.tags.length; i++) {
          if (typeof this.article.tags[i] == 'string') {
            this.article.tags.splice(i, 1, {
              tagId: null, tagName: this.article.tags[i]
            })
          }
        }
        await this.saveArticle(this.article)
        await this.$router.push('/article')
        await this.resetArticle()
      } else {
        this.$refs.edit_form.validate()
      }
    },
    //v-tabの変更に応じて表示する編集formatを変更
    changeFormat(key) {
      this.selectedFormat = key
      if (key === 0) {
        this.currentView = Edit
      } else if (key === 1) {
        this.currentView = Preview
      } else if (key === 2) {
        this.currentView = EditAndPreview
      } else if (key === 3) {
        this.currentView = EditAndPreviewAndFeedback
      }
    },
    //qiita投稿or更新ボタンを押下した時にモーダルを変更するメソッド
    toggleQiitaDialog() {
      this.qiitaDialog = !this.qiitaDialog
    },
    //qiitaに投稿or更新するメソッド
    async postedToQiita(article) {
      await this.postArticle(article.stateFlag)
      await this.postArticleToQiita(article.articleId)
    },
  },

}
</script>


<style scoped>
#article-edit-field {
  background-color: #f5f5f5;
  padding: 5px 30px;
}

.title-tag-form {
  margin-top: 40px;
}

.title-tag-form .title {
  padding: 0;
}


.edit-field {
  position: sticky;
  top: 1%;
}

.post-article-toQiita-btn {
  margin-top: 40px;
}

.change-format-field-tab {
  position: sticky;
  top: 1%;
}

.article-edit-action-field {
  margin-top: 30px;
  margin-left: 20px;
  margin-bottom: 30px;
  padding-top: 30px;
  padding-bottom: 20px;
  height: 150px;
  width: 400px;
  /*border: 2px solid #00fa9a;*/
  /*border-radius: 15px;*/
  /*background-color: ;*/
}

.btn {
  background-color: #ffffff;
}
</style>