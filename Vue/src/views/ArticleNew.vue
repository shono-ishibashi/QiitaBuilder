<template>
  <v-container id="article-edit-field" fluid>

    <!--  投稿ページ  -->
    <!--  loading処理追加時: v-show="isDisplay"  -->
    <div
        class="edit-field"
    >
      <!--      タイトル、タグ入力欄-->
      <v-form ref="form" v-model="valid" lazy-validation>
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
                counter="255"
            ></v-text-field>
            <v-combobox
                v-model="article.tags"
                :items="tagNameList"
                color="#5bc8ac"
                item-color="green"
                label="プログラミング技術に関するタグを5つまで入力"
                placeholder="(例) Java + <Enter>"
                deletable-chips
                :rules="[tags_max_size,tags_min_size,blank]"
                multiple
                counter="5"
                chips
            >
            </v-combobox>
          </v-col>
          <!--          記事投稿または下書き保存するフィールド-->
          <v-col cols="4">
            <div class="article-edit-action-field">
              <v-row v-if="this.slug==null" justify="center">
                <v-btn @click="postArticle(1)" class="btn" outlined color="#008b8b">記事を公開</v-btn>
              </v-row>
              <v-row v-if="this.slug==null" justify="center">
                <v-btn @click="postArticle(0)" class="btn" style="margin-top:40px;" outlined color="#008b8b">下書き保存
                </v-btn>
              </v-row>
            </div>
          </v-col>
          <!--          編集フォーマット選択タブフィールド-->
        </v-row>
        <v-tabs
            color="#5bc8ac"
            background-color="#f5f5f5"
            :class="selectedFormat===2?'change-format-field-tab':null"
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
        </v-tabs>
        <v-row>
          <v-main class="content">
            <component :is="currentView"></component>
          </v-main>
        </v-row>
      </v-form>
    </div>

    <!--  loading処理  -->
    <!--    <v-col cols="12" :class="{'progress-linear':isLoading}">-->
    <!--      <v-progress-linear-->
    <!--          v-show="isLoading"-->
    <!--          color="green"-->
    <!--          indeterminate-->
    <!--          rounded-->
    <!--          height="10"-->
    <!--      ></v-progress-linear>-->
    <!--    </v-col>-->

  </v-container>
</template>

<script>
import EditAndPreview from '../components/article_edit/format/FormatEditAndPreview'
import Edit from '../components/article_edit/format/FormatEdit'
import Preview from '../components/article_edit/format/FormatPreview'
import {mapState, mapGetters, mapActions} from 'vuex'

export default {
  name: "ArticleNew",
  components: {
    EditAndPreview,
    Edit,
    Preview,
  },
  data() {
    return {
      valid: true,
      currentView: Edit,
      selectedFormat: 0,
      // 各コンポーネント表示切替用のboolean
      // isDisplay: false,
      // loading処理表示切替用のboolean
      // isLoading: true,
      required: value => value && !!value || "必ず入力してください",
      blank: value => {
        const pattern = /\S/g
        return pattern.test(value[value.length - 1]) || "空文字のみの入力はできません"
      },
      title_limit_length: value => value && value.length <= 255 || "255文字以内で入力してください",
      tags_max_size: value => value && value.length <= 5 || "5つまで入力してください",
      tags_min_size: value => value && value.length >= 1 || "1つ以上入力してください",
    }
  },
  watch: {
    apiToken() {
      if (this.apiToken) {
        this.resetArticle()
            .catch(error => {
              this.articleErrorHandle(error)
            })
        this.fetchTags()
            .catch(error => {
              this.errorHandle(error)
            })
      }
    }
  },
  computed: {
    ...mapState("articles", ["tags"]),
    ...mapGetters("articles", ["tagNameList"]),
    ...mapState("article", ["article"]),
    slug() {
      return this.$route.params.articleId;
    },
    apiToken() {
      return this.$store.getters["auth/apiToken"];
    },
  },
  methods: {
    ...mapActions("article", ["saveArticle", "resetArticle", "toggleProcessFailure"]),
    ...mapActions("articles", ["fetchTags"]),
    //記事を投稿or更新するメソッド
    async postArticle(state) {
      //validationチェック
      if (this.$refs.form.validate()) {
        this.article.stateFlag = state
        for (let i = 0; i < this.article.tags.length; i++) {
          for (let tag of this.tags) {
            //タグが登録されているものには登録されているものをset
            if (tag.tagName === this.article.tags[i]) {
              this.article.tags.splice(i, 1, tag)
              break
            }
          }
        }
        //タグが登録されていないものにはtagIdにnullをset
        for (let i = 0; i < this.article.tags.length; i++) {
          if (typeof this.article.tags[i] == 'string') {
            this.article.tags.splice(i, 1, {
              tagId: null, tagName: this.article.tags[i]
            })
          }
        }
        await this.saveArticle(this.article)
            .catch((error) => {
              this.errorHandle(error)
            })
        await this.$router.push({name: "articleList"})
      } else {
        this.$refs.form.validate()
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
      }
    },
    errorHandle(error) {
      const status = error.response.status;
      switch (status) {
        case 401:
          this.nonValidToken = true;
          this.$store.dispatch("auth/logout");
          break;
        case 500:
          this.$store.dispatch("window/setInternalServerError", true);
          break;
        default:
          this.toggleProcessFailure();
      }
    },
    articleErrorHandle(error) {
      const status = error.response.status;
      switch (status) {
        case 400:
        case 404:
          this.$store.dispatch("window/setNotFound", true);
          break;
        case 403:
          this.$store.dispatch("window/setForbidden", true);
          break;
      }
      this.errorHandle(error);
    },
  }
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

/*.progress-linear{*/
/*  padding-top:150px;*/
/*  height:450px;*/
/*}*/
</style>