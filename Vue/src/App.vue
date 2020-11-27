<template>
  <v-app>
    <Header></Header>
    <v-main>
      <NotFound v-if="notFound" />
      <InternalServerError v-else-if="internalServerError" />
      <router-view v-else></router-view>
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
import NotFound from './views/error/404';
import InternalServerError from './views/error/500';

export default {
  name: 'App',

  components: {
    Header, Footer, NotFound, InternalServerError
  },
  created() {
  },
  methods: {
    onScroll(e) {
      if (typeof window === 'undefined') return
      const top = window.pageYOffset || e.target.scrollTop || 0
      this.fab = top > 500
    },
    toTop() {
      this.$vuetify.goTo(0)
    }
  },
  data: () => ({
    fab: false
  }),
  computed: {
    notFound() {
      return this.$store.getters["window/isNotFound"];
    },
    internalServerError() {
      return this.$store.getters["window/isInternalServerError"];
    },
  },
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

/* markdown editor */
.v-application .md code {
  font-weight: initial;
  /* background-color: #2f2f2f; */
  /* color: rgba(255, 255, 255, 0.9); */
  font-size: 95%;
  display: inline-block;
  padding: 1px 0.5em 1px 0.5em;
  margin: 2px;
  margin-bottom: 14px;
}

/* Code block */
.v-application .md pre code {
  display: block;
  padding: 0.5em 0.8em 0.5em 0.8em;
}

.v-application .md code:before,
.v-application .md code:after {
  content: initial;
}

/* Change h1, h2... styles to make markdown's style better */
.v-application .md p {
  margin-bottom: 8px;
}

.markdown-text {
  word-wrap: break-word;
}

</style>
