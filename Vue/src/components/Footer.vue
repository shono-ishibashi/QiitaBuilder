<template>
  <v-footer
      color="#5bc8ac"
      padless
  >
    <v-container>

      <v-row
          justify="center"
          no-gutters
          class=" py-4 text-center white--text"
          color="#5bc8ac"
      >
        Qiita Builder<br>
        〜Qiitaでバズらせたい〜
      </v-row>
      <v-row
          class="lighten-2 py-4 text-center white--text"
      >
        <v-col>
          <v-btn
              v-for="link in links"
              :key="link"
              color="white"
              text
              rounded
              class="my-2"
          >
            {{ link }}
          </v-btn>
        </v-col>
      </v-row>
      <v-divider color="white"></v-divider>
      <v-row
          justify="center"
          class="lighten-2 py-4 text-center white--text"
      >
        <v-btn :href="rakusUrl" target="_blank" color="white" text>ラクスパートナーズ</v-btn>
        <v-btn :href="qiitaUrl" target="_blank" color="white" text>Qiita</v-btn>
      </v-row>
    </v-container>
  </v-footer>
</template>

<script>
export default {
  name: "Footer",
  data() {
    return {
      links: [
        'About',
        'ご利用規格',
        'プライバシー',
        'ガイドライン',
        'ご意見'
      ],
      rakusUrl: "https://www.rakus-partners.co.jp/",
      qiitaUrl: "https://qiita.com/"
    }
  }
}
</script>

<style scoped>

</style>