<template>
  <v-app>
    <div v-if="isLoading">
      <h1>認証中</h1>
      <VueLoading
          type="spiningDubbles"
          color="green"
          :size="{ width: '100px', height: '100px' }"
      />
    </div>
    <v-btn @click="toQiitaAPIAuthentication">toQiita</v-btn>
  </v-app>
</template>

<script>
import {VueLoading} from 'vue-loading-template';
import axios from 'axios';

export default {
  name: "QiitaTest.vue",
  components: {
    VueLoading
  },
  data() {
    return {
      isLoading: false,
    }
  },
  methods: {
    async authenticateQiitaAPI() {
      this.isLoading = true;
      const apiToken = await this.$store.getters["auth/apiToken"];
      const API_URL = 'http://localhost:8080/qiita_builder/';
      const requestBody = {
        state: this.$route.query.state,
        code: this.$route.query.code,
      }
      await console.log(requestBody);
      await axios.post(API_URL + 'qiita/check-qiita-api-authentication',requestBody,{
        headers:{
          "Authorization": apiToken,
          "Content-Type": "application/json"
        }
      })
      .then(() => {
        this.toggleIsLoading();
      })
      .catch(function() {
        this.toggleIsLoading();
        alert('Qiitaとの連携に失敗しました。再度お試しください');
      })
    },
    toQiitaAPIAuthentication() {
      const API_URL = 'http://localhost:8080/qiita_builder/';
      axios.get(API_URL + 'qiita/to-qiita-api-authentication',{
        headers: {
          Authorization: this.$store.getters["auth/apiToken"],
        }
      }).then((response) => {
        location.href = response.data;
      })
    },
    toggleIsLoading() {
      this.isLoading = !this.isLoading;
    }
  },
  computed:{
    apiToken : function(){
      return this.$store.getters["auth/apiToken"];
    }
  },
  watch:{
    apiToken(){
      if(this.$route.query.state) {
        this.authenticateQiitaAPI();
      }
    }
  }
}
</script>

<style scoped>

</style>
