import Vue from 'vue'
import Vuex from 'vuex'
import auth from './modules/auth'
import articles from './modules/articles'
import article from './modules/article'
import users from './modules/users'
import user from './modules/user'
import window from './modules/window';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    API_URL:"http://api.qiita-builder.ga/"
  },
  mutations: {
  },
  actions: {
  },
  getters:{
    API_URL:state=>state.API_URL
  },
  modules: {
    auth:auth,
    articles:articles,
    article:article,
    users:users,
    user:user,
    window:window,
  }
})
