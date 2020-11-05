import Vue from 'vue'
import VueRouter from 'vue-router'
import ArticleList from '../views/ArticleList'

Vue.use(VueRouter)

const routes = [
    {
        path: '/articleList',
        name: 'ArticleList',
        component: ArticleList
    },
    {
        path: '/article/detail',
        name: 'articleDetail',
        component: () => import(/* webpackChunkName: "articleDetail" */ '../views/ArticleDetail.vue')
    },
    {
        path: '/user/detail',
        name: 'userDetail',
        component: () => import(/* webpackChunkName: "articleDetail" */ '../views/UserDetail.vue')
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
