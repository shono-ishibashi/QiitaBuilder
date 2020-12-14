import Vue from 'vue'
import VueRouter from 'vue-router'
import firebase from "firebase";
import store from "../store/index";
import axios from "axios";

Vue.use(VueRouter)

const routes = [

    {
        path: '/article',
        name: 'articleList',
        component: () => import(/* webpackChunkName: "articleList" */'../views/ArticleList')
    },
    {
        path: '/article/new',
        name: 'articleNew',
        component: () => import(/* webpackChunkName: "articleNew" */'../views/ArticleNew')
    },
    {
        path: '/article/:articleId',
        name: 'articleDetail',
        component: () => import(/* webpackChunkName: "articleDetail" */ '../views/ArticleDetail.vue')
    },
    {
        path: '/article/:articleId/edit',
        name: 'articleEdit',
        component: () => import(/* webpackChunkName: "articleEdit" */'../views/ArticleEdit')
    },
    {
        path: '/login',
        name: 'login',
        component: () => import(/* webpackChunkName: "login" */ '../views/Login.vue')
    },
    {
        path: '/qiita-0auth-done',
        name: 'qiita-0auth-done',
        component: () => import(/* webpackChunkName: "login" */ '../views/QiitaOauthDone.vue')
    },
    {
        path: '/user/ranking',
        name: 'ranking',
        component: () => import(/* webpackChunkName: "ranking" */ '../views/Ranking.vue')
    },
    {
        path: '/user/:userId',
        name: 'userDetail',
        component: () => import(/* webpackChunkName: "articleDetail" */ '../views/UserDetail.vue')
    },
    {
        path: '*',
        name: '404',
        component: () => import(/* webpackChunkName: "404" */ '../views/error/404.vue')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeResolve(async (to, from, next) => {
    store.dispatch("window/clearErrors");
    if (to.path === '/login') {
        next();
    } else {
        //ログイン中の処理
        await firebase.auth().onAuthStateChanged(async user => {
            if (user) {
                await store.commit('auth/setLoginUser', firebase.auth().currentUser);
                const uid = await firebase.auth().currentUser.uid;
                await firebase.firestore().collection("users").doc(uid).get()
                    .then(data => {
                        axios.post(store.getters.API_URL + 'login?uid=' + uid + '&password=' + data.data().password, {}, {})
                            .then(response => {
                                //Vuexにjwt Tokenを追加
                                store.commit("auth/setAPIToken", response.headers.authorization);
                            })
                    })
                if (to.path === '/login') {
                    await next({name: 'articleList'});
                } else {
                    await next();
                }
                //ログイン中でないならログアウト処理を行う
            } else {
                await store.dispatch('auth/logout');
            }
        })
    }
})

export default router
