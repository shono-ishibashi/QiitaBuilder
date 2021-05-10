<template>
  <v-container>
    <h1>403 Forbidden</h1>
    <p>
      お探しのページへのアクセスは禁止されています。
    </p>
    <p>
      また、URL、ファイル名にタイプミスがないか再度ご確認ください。
    </p>
    <router-link :to="{ name: 'articleList' }"
      >QiitaBuilderトップページへ</router-link
    >
  </v-container>
</template>

<script>
export default {
  name: "error403"
}
</script>
