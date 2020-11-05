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
          <v-btn text-align @click="login">
            <v-icon>mdi-google</v-icon>
            Google login
          </v-btn>
        </v-card-actions>
      </v-card-text>
    </v-card>
  </v-app>
</template>

<script>
export default {
  name: "googleLogin",
  computed: {},
  methods: {
    login() {
      this.$store.dispatch('auth/googleLogin');
    }
  }
}
</script>

<style scoped>

</style>
