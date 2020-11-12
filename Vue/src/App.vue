<template>
  <v-app>
    <Header></Header>
      <v-main>
          <router-view></router-view>
      </v-main>
    <Footer></Footer>
    <transition name="fade">
      <v-btn
          v-scroll="onScroll"
          v-show="fab"
          fab
          dark
          fixed
          bottom
          right
          color="#008b8b"
          @click="toTop">
        <v-icon>mdi-chevron-up</v-icon>
      </v-btn>
    </transition>
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
    onScroll (e){
      if (typeof window === 'undefined') return
      const top = window.pageYOffset ||   e.target.scrollTop || 0
      this.fab = top > 500
    },
    toTop () {
      this.$vuetify.goTo(0)
    },
    ...mapActions("articles",["fetchTags"])
  },
  data: () => ({
    fab : false
  }),
};
</script>

<style>
.fade-enter-active, .fade-leave-active {
  transition: 0.5s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
  transform: scale(0);
}
</style>
