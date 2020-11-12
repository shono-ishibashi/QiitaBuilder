<template>
  <v-container>
    <h3>
      <v-icon color="#5bc8ac">mdi-chess-king</v-icon>
      {{ rankTitle }}
    </h3>
    <RankingChart class="ranking-chart" :chart-data="chartData" :options="options" height="60%"
                  width="100%"></RankingChart>
  </v-container>
</template>

<script>
import RankingChart from './RankingChart.vue';


export default {
  name: "ChartArea",
  components: {
    RankingChart
  },
  data() {
    return {
      chartData: {},
      options: {}
    }
  },
  props: {
    selectRankItemId: {
      type: Number,
      required: true
    },
    rankUsers: {
      type: Array,
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
    }
  },
  watch: {
    rankUsers: function () {
      let rankUserNames = [];
      let countData = [];
      let labelName = null;


      this.rankUsers.slice(0, 10).forEach(rankUser => {
        rankUserNames.push(rankUser.displayName);
        switch (this.selectRankItemId) {
          case 1:
            countData.push(rankUser.feedbackCount);
            labelName = 'FBした数';
            break;
          case 2:
            countData.push(rankUser.postedArticleCount);
            labelName = '記事投稿数';
            break;
          case 3:
            countData.push(rankUser.qiitaRecommendedAllCount);
            labelName = 'Qiita推薦累計数';
            break;
          default :
            countData.push(rankUser.feedbackCount);
            labelName = 'FBした数';
            break;
        }
      })

      this.chartData = {
        labels: rankUserNames,
        datasets: [
          {
            label: labelName,
            data: countData,
            backgroundColor: 'rgba(91,200,172,0.2)',
            borderColor: 'rgba(91,200,172,1)',
            borderWidth: 1
          }
        ]
      }
    },

    chartData: function () {
      let countMax;
      let countMin;
      const rank1User = this.rankUsers[0];
      const rank10User = this.rankUsers[9];

      switch (this.selectRankItemId) {
        case 1:
          countMax = rank1User.feedbackCount;
          countMin = rank10User.feedbackCount;
          break;
        case 2:
          countMax = rank1User.postedArticleCount;
          countMin = rank10User.postedArticleCount;
          break;
        case 3:
          countMax = rank1User.qiitaRecommendedAllCount;
          countMin = rank10User.qiitaRecommendedAllCount;
          break;
      }
      const suggestedMax = () => {
        //let result;
        // if (countMax % 5 === 0) {
        //   result = (countMax / 5) * 5;
        // } else {
        //   result = (Math.floor(countMax / 5) + 1) * 5;
        // }
        // return result;
        return (Math.floor(countMax / 5) + 1) * 5;
      }

      const suggestedMin = () => {
        let result;
        if (countMin % 5 === 0) {
          result = ((countMax / 5) - 1) * 5;
        } else {
          result = Math.floor(countMin / 5) * 5;
        }
        return result;
      }

      this.options = {
        scales: {
          yAxes: [{
            ticks: {
              suggestedMax: suggestedMax(),
              suggestedMin: suggestedMin(),
              stepSize: 5
            }
          }]
        }
      }
    }
  }
}
</script>

<style scoped>
.ranking-chart {
  width: 100%;
  height: 50%;
}

</style>