import Vue from 'vue'
import VueRouter from 'vue-router'
import ArticleList from '../views/ArticleList'
import firebase from "firebase";
import store from "../store/index";
import axios from "axios";

Vue.use(VueRouter)

const routes = [
  {
    path:'/articleList',
    name:'ArticleList',
    component:ArticleList
  },
  {
    path: '/article/detail',
    name: 'articleDetail',
    component: () => import(/* webpackChunkName: "articleDetail" */ '../views/ArticleDetail.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import(/* webpackChunkName: "login" */ '../views/Login.vue')
  },
  {
    path: '/qiitatest',
    name: 'qiitatest',
    component: () => import(/* webpackChunkName: "login" */ '../views/QiitaTest.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

const API_URL = 'http://localhost:8080/qiita_builder/';

router.beforeResolve(async (to, from, next) => {
  if (to.path === '/login') {
    next()
  } else {
    //ログイン中の処理
    await firebase.auth().onAuthStateChanged(user => {
      if (user) {
        store.commit('auth/setLoginUser',firebase.auth().currentUser);
        const uid = firebase.auth().currentUser.uid;
        firebase.firestore().collection("users").doc(uid).get()
            .then(data => {
              axios.post( API_URL + 'login?uid=' + uid + '&password=' + data.data().password,{},{
              })
                  .then(response => {
                    //Vuexにjwt Tokenを追加
                    store.commit("auth/setAPIToken", response.headers.authorization);
                  })
            })
        if (to.path === '/login') {
          next( {path : '/'});
        } else {
          next();
        }
      //ログイン中でないならログアウト処理を行う
      } else {
        store.commit('setAPIToken',null);
        store.dispatch('auth/logout');
        next({path: '/login'})
      }
    })
  }
})

export default router
