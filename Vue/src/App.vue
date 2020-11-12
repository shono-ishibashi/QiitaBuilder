<template>
  <v-app>
    <Header></Header>
      <v-main>
          <router-view></router-view>
      </v-main>
    <Footer></Footer>
  </v-app>
</template>

<script>
import Header from './components/Header'
import Footer from './components/Footer'

export default {
  name: 'App',

  components: {
    Header,Footer
  },
  created(){
  },
  methods: {
  },
  data: () => ({
    //
  }),
};
</script>
