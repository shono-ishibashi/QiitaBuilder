<template>
  <v-container>
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

  data() {
    return {
      chartData: {},
      options: {}
    }
  },

  watch: {
    rankUsers: function () {

      if (this.rankUsers.length) {

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
      }
    },

    chartData: function () {

      let countMax;
      let countMin;

      let lastIndex;
      if (this.rankUsers.length < 10) {
        lastIndex = this.rankUsers.length - 1;
      } else {
        lastIndex = 9;
      }

      const champUser = this.rankUsers[0];
      const lastUser = this.rankUsers[lastIndex];

      switch (this.selectRankItemId) {
        case 1:
          countMax = champUser.feedbackCount;
          countMin = lastUser.feedbackCount;
          break;
        case 2:
          countMax = champUser.postedArticleCount;
          countMin = lastUser.postedArticleCount;
          break;
        case 3:
          countMax = champUser.qiitaRecommendedAllCount;
          countMin = lastUser.qiitaRecommendedAllCount;
          break;
        default:
          countMax = champUser.feedbackCount;
          countMin = lastUser.feedbackCount;
          break;
      }
      const suggestedMax = () => {
        return (Math.floor(countMax / 5) + 1) * 5;
      }

      const suggestedMin = () => {
        let result;
        if (countMin % 5 === 0) {
          result = ((countMin / 5) - 1) * 5;
        } else {
          result = Math.floor(countMin / 5) * 5;
        }
        return result;
      }

      this.options = {
        legend: {
          display: false
        },
        scales: {
          yAxes: [{
            ticks: {
              suggestedMax: suggestedMax(),
              suggestedMin: suggestedMin(),
              stepSize: 5
            }
          }]
        },
        responsive: true,
        onClick: this.handle
      }
    }
  },

  methods: {
    handle(point, event) {
      if (typeof event[0] === 'object') {
        const item = event[0];
        this.$emit('receive-index', item._index)
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