import Vue from 'vue'
import VueRouter from 'vue-router'
import ArticleList from '../views/ArticleList'

Vue.use(VueRouter)

const routes = [
  {
    path:'/articleList',
    name:'ArticleList',
    component:ArticleList
  },
  {	
    path: '/article/:articleId',	
    name: 'articleDetail',	
    component: () => import(/* webpackChunkName: "articleDetail" */ '../views/ArticleDetail.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import(/* webpackChunkName: "login" */ '../views/Login.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
