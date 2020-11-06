import Vue from 'vue'
import Vuex from 'vuex'
import auth from './modules/auth'
import articles from './modules/articles'
import article from './modules/article'
import users from './modules/users'
import user from './modules/user'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    API_URL:"http://localhost:8080/qiita_builder/"
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
    user:user
  }
})
