<template>
  <div>
    <v-app-bar
        color="#5bc8ac"
        dense
        dark
        height="70px"
        class="app-bar"
    >
      <v-toolbar-title
          id="logo"
          text
          style="font-size: 40px; cursor: pointer;"
          @click="toArticleList"
      >Qiita Builder
      </v-toolbar-title>

      <v-spacer></v-spacer>
      <div class="header-action-field">
        <v-btn data-test-id="toArticleList" class="header-btn" color="#008b8b" @click="toArticleList" v-if="loginUser">
          <v-icon>mdi-format-list-bulleted-type</v-icon>
          記事一覧
        </v-btn>

        <v-btn data-test-id="toArticleNew" class="header-btn" color="#008b8b" @click="toArticleNew" v-if="loginUser">
          <v-icon>mdi-square-edit-outline</v-icon>
          記事投稿
        </v-btn>

        <v-btn data-test-id="toRanking" class="header-btn" color="#008b8b" v-if="loginUser" @click="toRanking">
          <v-icon>mdi-chess-king</v-icon>
          ランキング
        </v-btn>

        <v-menu
            open-on-hover
            left
            bottom
            offset-y
            v-if="loginUser"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
                icon
                v-bind="attrs"
                v-on="on"
            >
              <v-avatar>
                <v-img
                    :src="loginUser.photoURL"
                    :alt="loginUser.displayName"
                />
              </v-avatar>
            </v-btn>
          </template>

          <v-list>
            <v-list-item link @click="toMyPage" data-test-id="menuToMyPage">
              <v-list-item-title>マイページ</v-list-item-title>
            </v-list-item>
            <v-list-item link @click="toMyArticles" data-test-id="menuToMyArticles">
              <v-list-item-title>お気に入り記事一覧</v-list-item-title>
            </v-list-item>
            <v-list-item link @click="toDraftArticles" data-test-id="menuToDraftArticles">
              <v-list-item-title>下書き一覧</v-list-item-title>
            </v-list-item>
            <v-list-item link @click="toQiitaAPIAuthentication" data-test-id="menuToQiitaAPIAuthentication">
              <v-list-item-title>Qiitaと連携</v-list-item-title>
            </v-list-item>
            <v-divider></v-divider>
            <v-list-item @click="logout" data-test-id="menuLogout">
              <v-icon>mdi-logout</v-icon>
              <v-list-item-title>ログアウト</v-list-item-title>
            </v-list-item>

          </v-list>
        </v-menu>
      </div>
    </v-app-bar>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex'

export default {
  name: "Header",
  computed: {
    ...mapGetters("auth", ["loginUser"])
  },
  methods: {
    ...mapActions("auth", ["logout"]),
    ...mapActions("article", ["resetArticle"]),
    toArticleList() {
      this.$router.push({name: "articleList"}, () => {
      })
    },
    toArticleNew() {
      this.resetArticle()
      this.$router.push({name: "articleNew"}, () => {
      })
    },
    toRanking() {
      this.$router.push({name: 'ranking'}, () => {
      })
    },
    async toMyPage() {
      await this.$router.push('/user/0?defaultList=MyPage').catch(err => {
        if (this.$route.path === '/user/0') {
          console.log(err)
        }
      })
    },
    async toMyArticles() {
      await this.$router.push('/user/0?defaultList=MyArticles').catch(err => {
        if (this.$route.path === '/user/0') {
          console.log(err)
        }
      })
    },
    async toDraftArticles() {
      await this.$router.push('/user/0?defaultList=DraftArticles').catch(err => {
        if (this.$route.path === '/user/0') {
          console.log(err)
        }
      })
    },
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@800&display=swap');

.app-bar {
  padding-left: 40px;
  padding-right: 20px;
}

.header-btn {
  margin-right: 10px;
}

#logo {
  font-family: poppins;
}
</style>
