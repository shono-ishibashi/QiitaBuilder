import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {	
    path: '/article/detail',	
    name: 'articleDetail',	
    component: () => import(/* webpackChunkName: "articleDetail" */ '../views/ArticleDetail.vue')
  },
  {
    path: '/user/detail',
    name: 'userDetail',
    component:()=>import(/* webpackChunkName: "articleDetail" */ '../views/UserDetail.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
