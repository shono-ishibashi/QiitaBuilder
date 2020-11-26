import axios from "axios";
import store from "@/store/index";

export default {
  methods: {
    toQiitaAPIAuthentication() {
      axios.get(store.getters.API_URL + 'qiita/to-qiita-api-authentication', {
        headers: {
          Authorization: store.getters["auth/apiToken"]
        }
      }).then((response) => {
        location.href = response.data;
      })
    },

  }
}
