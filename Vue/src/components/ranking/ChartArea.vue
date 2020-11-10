<template>
  <v-container>
      <h3>
        <v-icon color="#5bc8ac">mdi-chess-king</v-icon>
        {{rankTitle}}
      </h3>
<!--      <Chart :chart-data="chartData" :options="options"></Chart>-->
  </v-container>
</template>

<script>
// import Chart from './Chart.vue';

export default {
  name: "ChartArea",
  components: {
    // Chart
  },
  props: {
    selectRankItemId: {
      type: Number,
      required: true
    }
  },
  computed: {
    rankTitle() {
      let rankTitle;
      switch (this.selectRankItemId) {
        case 1:
          rankTitle = 'FBした数ランキング';
          break;
        case 2:
          rankTitle = '記事投稿数ランキング';
          break;
        case 3:
          rankTitle = 'Qiita推薦累計数ランキング';
          break;
      }
      return rankTitle;
    },
    chartData(){
      return null;
    },
    options(){
      return null;
    }
  }
}
</script>

<style scoped>

</style>