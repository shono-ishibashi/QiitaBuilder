import axios from 'axios';
import qs from 'qs';

export default {
    namespaced: true,
    state: {
        userDetail: {
            userId: 0,
            displayName: "",
            photoUrl: "",
            usedTags: [],
            feedbackCount: 0,
            qiitaRecommendedAllCount: 0,
            postedArticleCount: 0,
            isLoginUser: false,
        },
        postedArticles: [],
        myArticles: [],
        feedbackArticles: [],
        displayArticles: [],
        usedTags: [],
    },
    getters: {
        userId(state) {
            return state.userDetail.userId
        },
        postedQiitaArticles(state) {
            return state.postedArticles.filter((art) => art.stateFlag === 2)
        },
        notPostedQiitaArticles(state) {
            return state.postedArticles.filter((art) => art.stateFlag === 1)
        },
        draftArticles(state) {
            return state.postedArticles.filter((art) => art.stateFlag === 0)
        },
        displayArticles(state) {
            return state.displayArticles.filter((art) => art.stateFlag !== 9)
        },
        usedTags(state) {
            return state.usedTags;
        },
    },
    mutations: {
        setUserDetail(state, user) {
            if (user.userId) {
                if (!user.postedArticleCount) user.postedArticleCount = 0;
                if (!user.feedbackCount) user.feedbackCount = 0;
                if (!user.qiitaRecommendedAllCount) user.qiitaRecommendedAllCount = 0;
                state.userDetail = user;
            }
        },
        setPostedArticles(state, postedArticles) {
            state.postedArticles = postedArticles;
        },
        setMyArticles(state, myArticles) {
            state.myArticles = myArticles;
        },
        setFeedbackArticles(state, feedbackArticles) {
            state.feedbackArticles = feedbackArticles;
        },
        setDisplayArticles(state, articles) {
            if (articles.length === 0) state.displayArticles.length = 0;
            articles.forEach((art) => {
                state.displayArticles.push(art)
            })
        },
        setUsedTags(state, tags) {
            if (tags.length === 0) state.usedTags.length = 0;
            tags.forEach((tag) => {
                state.usedTags.push(tag)
            })
        },
        clearDisplayArticles(state) {
            state.displayArticles.length = 0;
        },
        clearUsedTag(state) {
            state.usedTags.length = 0;
        }
    },
    actions: {
        async setArticlesAndTags({commit}, articles) {
            await commit("clearDisplayArticles");
            await commit("setDisplayArticles", articles);
            await commit("clearUsedTag");
            await articles.forEach((art) => commit("setUsedTags", art.tags));
        },
        async fetchUserDetail({commit, rootGetters, rootState}, userId) {
            const url = rootGetters.API_URL + 'user/detail/';
            let apiToken = rootState.auth.apiToken; // rootGetters["auth/apiToken"] も可

            await axios.get(url, {
                params: {userId},
                headers: {
                    Authorization: apiToken,
                },
            }).then(res => {
                commit("setUserDetail", res.data);
            })
        },
        async fetchPostedArticles({commit, rootGetters, rootState}, userId) {
            const url = rootGetters.API_URL + 'article/'
            let apiToken = rootState.auth.apiToken; // rootGetters["auth/apiToken"] も可
            let searchArticleForm = {
                sortNum: 1,
                pageSize: 0,
                currentPage: 0,
                userId: userId,
                //stateFlagList: [10],
            }
            //let paramsSerializer = (params) => qs.stringify(params);

            await axios.get(url, {
                params: searchArticleForm,
                paramsSerializer: params => {
                    return qs.stringify(params)
                },
                headers: {
                    Authorization: apiToken,
                },
            })
                .then(res => {
                    commit("setPostedArticles", res.data)
                }).catch((error) => {
                    console.log(error)
                })
        },
        async fetchFeedbackArticles({commit, rootGetters, rootState}, userId) {
            const url = rootGetters.API_URL + 'article/feedbacked';
            let apiToken = rootState.auth.apiToken; // rootGetters["auth/apiToken"] も可


            await axios.get(url, {
                params: userId,
                headers: {
                    Authorization: apiToken,
                },
            }).then(res => {
                commit("setFeedbackArticles", res.data);
            })
        },
        async fetchMyArticles({commit, rootGetters, rootState}, userId) {
            const url = rootGetters.API_URL + 'article/my-articles';
            let apiToken = rootState.auth.apiToken; // rootGetters["auth/apiToken"] も可

            await axios.get(url, {
                params: userId,
                headers: {
                    Authorization: apiToken,
                },
            }).then(res => {
                commit("setMyArticles", res.data);
            })
        },
    }
}