<template>
  <v-container>
    <v-row>
      <v-col cols="6" class="contentWrap"><span
          style="font-weight: bold; font-size: x-large;">@{{ userDetail.displayName }}</span></v-col>
      <v-col cols="6" class="contentWrap">
        <v-btn @click="toQiitaAPIAuthentication" v-if="userDetail.isLoginUser" color="#5bc8ac" elevation="2"
               style="font-weight: bold" dark>Qiita連携
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols=6 class="contentWrap">
        <v-avatar size="140">
          <img :src="userDetail.photoUrl" alt=""/></v-avatar>
      </v-col>
      <v-col cols=6 align-self="center">
        <Pie class="chart" :chart-data="chartData" :options="chartOptions"
             v-if="userDetail.usedTags.length!==0"/>
        <v-alert
            v-if="userDetail.usedTags.length===0"
            text
            dense
            color="teal"
            border="left"
            class="contentWrap"
        >
          タグの使用履歴がありません
        </v-alert>
      </v-col>
    </v-row>

    <v-row align-content="center" justify="center" class="box16">
      <v-col cols="4" class="box6">
        <v-row align-content="center" justify="center">
          Qiita投稿数 / Builder投稿数
        </v-row>
        <v-row align-content="center" justify="center" class="count">
          {{ userDetail.postedArticleCount }} / {{ notDraftArticles.length }}
        </v-row>
      </v-col>
      <v-col cols="3" class="box6">
        <v-row align-content="center" justify="center">
          FB数
        </v-row>
        <v-row align-content="center" justify="center" class="count">
          {{ userDetail.feedbackCount }}
        </v-row>
      </v-col>
      <v-col cols="3" class="box6">
        <v-row align-content="center" justify="center">
          総獲得推奨数
        </v-row>
        <v-row align-content="center" justify="center" class="count">
          {{ userDetail.qiitaRecommendedAllCount }}
        </v-row>
      </v-col>
    </v-row>
  </v-container>

</template>

<script>
import {mapGetters, mapState} from "vuex";
import Pie from "@/components/user_detail/Pie";

export default {
  name: "UserInfo",
  components: {Pie},
  data() {
    return {
      chartOptions: {
        responsive: true,
        legend: {
          position: 'right'
        }
      },//Pieコンポーネントのグラフ表示用オプション
    };
  },
  computed: {
    ...mapGetters("user", [
      "notDraftArticles",
      "chartData",]),
    ...mapState("user", ["userDetail",]),
  }
}
</script>

<style scoped>
.contentWrap {
  display: flex;
  align-items: center;
  justify-content: center;
}

.count {
  font-size: x-large;
  font-weight: bold;
}

.box16 {
  padding: 0.5em 1em;
  margin: 2em 0;
  background: -webkit-repeating-linear-gradient(-45deg, #f0f8ff, #f0f8ff 3px, #e9f4ff 3px, #e9f4ff 7px);
  background: repeating-linear-gradient(-45deg, #f0f8ff, #f0f8ff 3px, #e9f4ff 3px, #e9f4ff 7px);
}

.box6 {
  padding: 0.5em 1em;
  margin: 2em 0.25em;
  background: #f0f7ff;
  border: dashed 2px #5bc8ac; /*点線*/
  border-radius: 10px;
}
</style>