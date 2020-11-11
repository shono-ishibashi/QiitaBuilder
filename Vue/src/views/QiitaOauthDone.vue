<template>
  <v-app>
    <v-container v-if="isLoading">
      <v-row>
        <h1>Qiitaと連携中</h1>
        <VueLoading
            type="spin"
            color="green"
            :size="{ width: '100px', height: '100px' }"
        />
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
import {VueLoading} from 'vue-loading-template';
import axios from 'axios';
import {mapGetters} from 'vuex';
import router from "@/router";

export default {
  components: {
    VueLoading
  },
  data() {
    return {
      isLoading: true,
    }
  },
  methods: {
    sleep(msec) {
      return new Promise(function (resolve) {

        setTimeout(function () {
          resolve()
        }, msec);

      })
    },
    async authenticateQiitaAPI() {
      this.isLoading = true;
      const apiToken = await this.$store.getters["auth/apiToken"];
      const requestBody = {
        state: this.$route.query.state,
        code: this.$route.query.code,
      }
      await console.log(requestBody);
      await axios.post(this.API_URL + 'qiita/check-qiita-api-authentication', requestBody, {
        headers: {
          "Authorization": apiToken,
          "Content-Type": "application/json"
        }
      })
          .then(async () => {
            await this.sleep(2000);
            await this.toggleIsLoading();
          })
          .catch(function () {
            this.toggleIsLoading();
            alert('Qiitaとの連携に失敗しました。再度お試しください');
          })
    },

    /**
     * Qiitaの認証画面へ遷移させるメソッド
     */
    toQiitaAPIAuthentication() {
      axios.get(this.API_URL + 'qiita/to-qiita-api-authentication', {
        headers: {
          Authorization: this.$store.getters["auth/apiToken"],
        }
      }).then((response) => {
        location.href = response.data;
      })
    },

    async postQiitaTest() {
      await axios.post(this.API_URL + 'qiita/save-article-to-qiita', {}, {
        headers: {
          Authorization: await this.$store.getters["auth/apiToken"],
        }
      }).then((response) => {
        console.log(response);
      }).catch(error => {
        console.error(error);
      })
    },
    toggleIsLoading() {
      this.isLoading = !this.isLoading;
    }
  },
  computed: {
    apiToken: function () {
      return this.$store.getters["auth/apiToken"];
    },
    ...mapGetters({API_URL: "API_URL"})
  },
  watch: {
    async apiToken() {
      //リダイレクトでstate,code が存在するなら
      if (await this.$route.query.state && await this.$route.query.code) {
        await this.authenticateQiitaAPI();
        await router.push('/articleList');
      }
    }
  },
}
</script>

<style scoped>

</style>
