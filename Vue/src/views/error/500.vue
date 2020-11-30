<template>
  <v-container>
    <h1>500 Internal Server Error</h1>
    <p>
      サーバーエラーが発生しました。
    </p>
    <p>
      サーバーの問題でお探しのページを表示できません。
    </p>
    <p>
      再度時間をおいてアクセスしてください。
    </p>
    <router-link :to="{ name: 'articleList' }">
      QiitaBuilderトップページへ
    </router-link>
  </v-container>
</template>

<script>
export default {
  name: "error500"
}
</script>
