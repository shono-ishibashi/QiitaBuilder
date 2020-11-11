<script>
import {Bar} from 'vue-chartjs';

export default {
  name: "RankingChart",
  extends: Bar,
  props: ['chartData', 'options', 'height', 'width'],
  watch: {
    options: function () {
      this.renderChart(this.chartData, this.options);
    }
  }
}
</script>

<style scoped>

</style>