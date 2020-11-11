<script>
import {Pie} from 'vue-chartjs';
export default {
  name: "Chart",
  extends: Pie,
  props: ['chartData', 'options'],
  watch:{
    chartData(){
      this.renderChart(this.chartData, this.options)
    }
  },
  mounted() {
    this.renderChart(this.chartData, this.options)
  }
}
</script>