<script>
import {Pie, mixins} from 'vue-chartjs';

export default {
  name: "Chart",
  extends: Pie,
  mixins: [mixins.reactiveProp],
  props: ['chartData', 'options', 'change'],
  computed: {
    data: function () {
      return this.chartData
    }
  },
  watch: {
    change: {
      handler: function () {
        this.renderChart(this.data, this.options);
      },
      deep: true
    }
  }
}
</script>