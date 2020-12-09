<template>
  <v-app class="white d-flex">
    <v-container class="area">
      <v-row align="center" class="spacer">
        <v-col cols="1" sm="1" md="1">
          <v-avatar size="42px" color="green" class="avatar">
            <img
              v-if="article.postedUser.photoUrl"
              :src="article.postedUser.photoUrl"
              alt="user-icon"
              @click="toUserDetail(article.postedUser.userId)"
            />
            <v-icon v-else dark size="42px">
              mdi-account-circle
            </v-icon>
          </v-avatar>
        </v-col>
        <v-col class="" cols="2" sm="2" md="2">
          <v-btn @click="toUserDetail(article.postedUser.userId)" text>
            <strong>@{{ article.postedUser.displayName }}</strong>
          </v-btn>
        </v-col>
        <!-- 投稿日または更新日 -->
        <v-col class="hidden-xs-only" sm="4" md="4">
          {{ lastEditAt.time | date }}{{ lastEditAt.text }}
        </v-col>
        <!-- Qiita投稿状況 -->
        <v-col class="" sm="3" md="3">
          <v-menu
            offset-y
            v-if="
              loginUser.uid == article.postedUser.uid &&
                article.stateFlag !== 0 &&
                article.stateFlag !== 9
            "
          >
            <template v-slot:activator="{ attrs, on }">
              <v-btn
                v-bind="attrs"
                v-on="on"
                color="green"
                outlined
                style="text-transform: none;"
              >
                {{ article.stateFlag | naming }} ▼
              </v-btn>
            </template>
            <v-list>
              <v-list-item
                v-for="(item, index) in qiitaMenus"
                :key="index"
                @click.stop="item.action"
              >
                <v-list-item-title v-text="item.name"></v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
          <v-btn
            v-if="
              loginUser.uid == article.postedUser.uid && article.stateFlag == 0
            "
            color="green"
            outlined
            style="text-transform: none; cursor: default;"
          >
            {{ article.stateFlag | naming }}
          </v-btn>
          <v-btn
            v-if="loginUser.uid != article.postedUser.uid"
            color="green"
            outlined
            style="text-transform: none; cursor: default;"
          >
            {{ article.stateFlag | naming }}
          </v-btn>
        </v-col>

        <!-- 記事メニュー -->
        <v-col
          class=""
          sm="2"
          md="2"
          v-if="loginUser.uid == article.postedUser.uid"
        >
          <v-menu offset-y>
            <template v-slot:activator="{ attrs, on }">
              <v-btn icon v-bind="attrs" v-on="on" class="">
                <v-icon>mdi-format-list-bulleted</v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item
                v-for="(item, index) in menus"
                :key="index"
                @click="item.action"
              >
                <v-list-item-title v-text="item.name"></v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </v-col>
      </v-row>
      <v-row v-if="article.stateFlag !== 0 && article.stateFlag !== 9">
        <v-col cols="12">
          <Button
            :myArticleId="myArticleId"
            :recommendId="recommendId"
            :qiitaRecommendPoint="article.qiitaRecommendPoint"
            @toggleMyArticle="toggleMyArticle"
            @toggleRecommend="toggleRecommend"
          />
        </v-col>
      </v-row>
      <v-flex>
        <v-card elevation="0">
          <v-container>
            <h1 class="">{{ article.title }}</h1>
          </v-container>
        </v-card>
      </v-flex>
      <v-chip-group active-class="primary--text">
        <v-chip
          v-for="tag in article.tags"
          :key="tag.tagId"
          @click="findByTagId(tag.tagId)"
          color="#5bc8ac"
          dark
        >
          {{ tag.tagName }}
        </v-chip>
      </v-chip-group>
    </v-container>
    <v-container>
      <Editor
        mode="viewer"
        ref="editor"
        hint="Hint"
        :outline="true"
        :render-config="renderConfig"
        v-model="article.content"
      />
    </v-container>
    <v-dialog v-model="dialog" max-width="290">
      <v-card>
        <v-card-title class="headline">
          削除しますか？
        </v-card-title>
        <v-card-text>
          投稿「{{ article.title }}」を削除しようとしています
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey" text @click="dialog = false">
            キャンセル
          </v-btn>
          <v-btn color="green darken-1" text @click="deleteArticle">
            削除する
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-app>
</template>

<script>
import { Editor } from "vuetify-markdown-editor";
import Button from "./ArticleButton";
import { mapActions, mapState } from "vuex";

export default {
  components: {
    Editor,
    Button,
  },
  data() {
    return {
      menus: [
        { name: "記事を編集する", action: this.toEdit },
        { name: "記事を削除する", action: this.toggleDialog },
      ],
      dateFormat: ["年", "月", "日"],
      dialog: false,
      renderConfig: {
        // Mermaid config
        mermaid: {
          theme: "dark",
        },
      },
    };
  },
  computed: {
    ...mapState("article", ["processFailure"]),
    lastEditAt() {
      if (this.article.updatedAt) {
        return { time: this.article.updatedAt, text: "に更新" };
      }
      return { time: this.article.createdAt, text: "に作成" };
    },
    loginUser() {
      return this.$store.state.auth.loginUser;
    },
    qiitaMenus() {
      if (this.article.stateFlag == 2)
        return [{ name: "Qiitaを更新する", action: this.updateQiita }];
      return [{ name: "Qiitaに投稿する", action: this.postQiita }];
    },
    ...mapState("articles", ["searchCriteria"]),
  },
  props: ["article", "myArticleId", "recommendId"],
  mounted() {
    // Access properties or methods using $refs
    // this.$refs.editor.focus();
    // this.$refs.editor.upload();
    // Dark theme
    // this.$vuetify.theme.dark = true;
  },
  filters: {
    naming: function(value) {
      if (value == 2) {
        return "Qiitaに投稿済み";
      } else if (value == 1) {
        return "Qiitaに未投稿";
      }
      return "下書き";
    },
    date: function(value) {
      if (!value) return "";
      var ymd = value.split("T")[0].split("-");
      return ymd[0] + "年" + ymd[1] + "月" + ymd[2] + "日";
    },
  },
  methods: {
    ...mapActions("article", ["toggleProcessFailure"]),
    ...mapActions("articles", ["fetchArticles"]),
    toggleRecommend() {
      this.$emit("toggleRecommend");
    },
    toggleMyArticle() {
      this.$emit("toggleMyArticle");
    },
    toggleDialog() {
      this.dialog = !this.dialog;
    },
    toEdit() {
      this.$router.push({ name: "articleEdit" });
    },
    toUserDetail(userId) {
      this.$router.push({ name: "userDetail", params: { userId: userId } });
    },
    findByTagId(tagId) {
      this.$router.push({ name: "articleList" });
      this.searchCriteria.searchTag = [tagId];
      this.fetchArticles(this.searchCriteria);
    },
    async deleteArticle() {
      // 削除前のstateFlag
      const beforeStateFlag = this.article.stateFlag;
      const item = this.article;
      item.stateFlag = 9;
      await this.$store
        .dispatch("article/saveArticle", item)
        .then(() => {
          this.$router.push({ name: "articleList" });
        })
        .catch((error) => {
          this.article.stateFlag = beforeStateFlag;
          this.toggleDialog();
          this.errorHandle(error);
        });
    },
    async updateQiita() {
      await this.$store.dispatch(
        "article/postArticleToQiita",
        this.article.articleId
      );
    },
    async postQiita() {
      await this.$store.dispatch(
        "article/postArticleToQiita",
        this.article.articleId
      );
    },
    errorHandle(error) {
      const status = error.response.status;
      if (status == 404) {
        this.$router.push({ name: "404" });
      } else if (status == 401) {
        this.nonValidToken = true;
      } else {
        this.toggleProcessFailure();
      }
    },
  },
};
</script>

<style scoped>
.area {
  padding: 20px;
}

.avatar {
  cursor: pointer;
}
</style>
