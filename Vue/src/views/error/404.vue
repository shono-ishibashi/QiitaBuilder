<template>
  <v-container>
    <h1>404 Not Found</h1>
    <p>
      お探しのページは一時的にアクセスができない状況にあるか、移動もしくは削除された可能性があります。
    </p>
    <p>
      また、URL、ファイル名にタイプミスがないか再度ご確認ください。
    </p>
    <router-link :to="{ name: 'articleList' }"
      >QiitaBuilderトップページへ</router-link
    >
  </v-container>
</template>
