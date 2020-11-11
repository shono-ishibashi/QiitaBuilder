<template>
  <v-app>
    <v-card>
      <v-toolbar
          color="light-blue"
          dark
          flat
          class="justify-center"
      >
        <v-card-title class="justify-center" color="blue">
          Login
        </v-card-title>
      </v-toolbar>
      <v-card-text>
        <v-card-actions class="justify-center">
          <v-btn text-align @click.stop="login">
            <v-icon>mdi-google</v-icon>
            Google login
          </v-btn>
        </v-card-actions>
      </v-card-text>
    </v-card>
    <v-btn @click="$store.dispatch('auth/logout')">logout</v-btn>
  </v-app>
</template>

<script>
import router from "@/router";

export default {
  name: "googleLogin",
  computed: {},
  methods: {
    async login() {
      await this.$store.dispatch('auth/googleLogin');
      await router.push('/articleList');
    }
  }
}
</script>

<style scoped>

</style>
