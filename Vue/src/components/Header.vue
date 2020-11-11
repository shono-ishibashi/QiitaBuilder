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
          style="font-weight:bold;font-size:30px;cursor: pointer;"
          @click="toArticleList"
      >Qiita Builder
      </v-toolbar-title>

      <v-spacer></v-spacer>
      <div class="header-action-field">
        <v-btn class="header-btn" color="#008b8b" @click="toArticleList" v-if="loginUser!=null">
          <v-icon>mdi-format-list-bulleted-type</v-icon>
          記事一覧
        </v-btn>

        <v-btn class="header-btn" color="#008b8b" @click="toArticleNew" v-if="loginUser!=null">
          <v-icon>mdi-square-edit-outline</v-icon>
          記事投稿
        </v-btn>

        <v-btn class="header-btn" color="#008b8b" v-if="loginUser!=null" @click="toRanking">
          <v-icon>mdi-chess-king</v-icon>
          ランキング
        </v-btn>

        <v-menu
            open-on-hover
            left
            bottom
            offset-y
            v-if="loginUser!=null"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
                icon
                v-bind="attrs"
                v-on="on"
            >
              <v-avatar>
                <img
                    :src="$store.getters['auth/loginUser'].photoURL"
                    :alt="$store.getters['auth/loginUser'].displayName"
                >
              </v-avatar>
            </v-btn>
          </template>

          <v-list>
            <v-list-item link>
              <v-list-item-title>マイページ</v-list-item-title>
            </v-list-item>
            <v-list-item link>
              <v-list-item-title>マイ記事</v-list-item-title>
            </v-list-item>
            <v-list-item link>
              <v-list-item-title>下書き一覧</v-list-item-title>
            </v-list-item>
            <v-list-item link @click="toQiitaAPIAuthentication">
              <v-list-item-title>Qiitaと連携</v-list-item-title>
            </v-list-item>
            <v-divider></v-divider>
            <v-list-item @click="$store.dispatch('auth/logout')">
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
import {mapGetters,mapActions} from 'vuex'
import axios from "axios";

export default {
  name: "Header",
  computed: {
    ...mapGetters("auth",["loginUser"])
  },
  methods:{
    ...mapActions("auth",["logout"]),
    ...mapActions("article",["resetArticle"]),
    toArticleList(){
      this.$router.push('/article')
    },
    toArticleNew() {
      this.resetArticle()
      this.$router.push('/article/new')
    },
    toRanking(){
      this.$router.push({name:'ranking'})
    },
    toQiitaAPIAuthentication() {
      axios.get(this.$store.getters.API_URL + 'qiita/to-qiita-api-authentication', {
        headers: {
          Authorization: this.$store.getters["auth/apiToken"]
        }
      }).then((response) => {
        location.href = response.data;
      })
    },
  }
}
</script>

<style scoped>
.app-bar {
  padding-left: 40px;
  padding-right: 20px;
}
.header-btn{
  margin-right:10px;
}
</style>
