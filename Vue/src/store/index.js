import Vue from 'vue'
import Vuex from 'vuex'
import auth from './modules/auth'
import articles from './modules/articles'
import article from './modules/article'
import users from './modules/users'
import user from './modules/user'
import qiitaAPI from './modules/qiitaAPI'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    auth:auth,
    articles:articles,
    article:article,
    users:users,
    user:user,
    qiitaAPI:qiitaAPI
  }
})
