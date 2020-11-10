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
        <v-btn class="header-btn" color="#008b8b" @click="toArticleList">
          <v-icon>mdi-format-list-bulleted-type</v-icon>
          記事一覧
        </v-btn>

        <v-btn class="header-btn" color="#008b8b">
          <v-icon>mdi-square-edit-outline</v-icon>
          記事投稿
        </v-btn>

        <v-btn class="header-btn" color="#008b8b" @click="toRanking">
          <v-icon>mdi-chess-king</v-icon>
          ランキング
        </v-btn>

        <v-btn class="header-btn" color="#008b8b">
          <v-icon>mdi-login</v-icon>
          ログイン
        </v-btn>

        <v-menu
            open-on-hover
            left
            bottom
            offset-y
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
                icon
                v-bind="attrs"
                v-on="on"
            >
              <v-icon>mdi-account</v-icon>
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
            <v-divider></v-divider>
            <v-list-item link>
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
export default {
  name: "Header",
  methods:{
    toArticleList(){
      this.$router.push('article')
    },
    toRanking(){
      this.$router.push({name:'ranking'})
    }
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