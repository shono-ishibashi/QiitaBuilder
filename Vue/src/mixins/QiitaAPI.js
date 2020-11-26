import axios from "axios";

export default {
  methods: {
    toQiitaAPIAuthentication() {
      axios.get(this.$store.getters.API_URL + 'qiita/to-qiita-api-authentication', {
        headers: {
          Authorization: this.$store.getters["auth/apiToken"]
        }
      }).then((response) => {
        location.href = response.data;
      })
    },



  }
}
