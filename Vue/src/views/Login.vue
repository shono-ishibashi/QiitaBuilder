<template>
  <v-flex align-self-center>
    <google-login/>
  </v-flex>
</template>

<script>
import GoogleLogin from '../components/auth/GoogleLogin'

export default {
  name: "Login",
  components: {
    GoogleLogin
  }
}
</script>

<style scoped>

</style>
