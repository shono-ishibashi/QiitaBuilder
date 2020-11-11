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
import {mapActions} from 'vuex'

export default {
  name: 'App',

  components: {
    Header,Footer
  },
  created(){
    this.fetchTags()
  },
  methods: {
    ...mapActions("articles",["fetchTags"])
  },
  data: () => ({
    //
  }),
};
</script>
