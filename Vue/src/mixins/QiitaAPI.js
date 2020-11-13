import axios from "axios";
import store from "../store/index";

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
    async postArticleToQiita(articleId) {
      console.log(store.getters["auth/isLinked"]);
      if (store.getters["auth/isLinked"]) {
        axios.post(store.getters.API_URL + 'qiita/save-article-to-qiita/' + articleId, {}, {
          headers: {
            "Authorization": store.getters["auth/apiToken"]
          }
        }).then(() => {

        }).catch(() => {
          if(confirm('Qiitaへの記事投稿に失敗しました。Qiita連携からやり直しますか?')){
            this.toQiitaAPIAuthentication();
          }

        })
      } else {
        await localStorage.setItem('articleId', articleId);
        await this.toQiitaAPIAuthentication();
      }
    },

  }
}
