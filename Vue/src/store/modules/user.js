import axios from 'axios';
import qs from 'qs';

export default {
    namespaced: true,
    state: {
        userId: 0,
        userDetail: {
            userId: 0,
            displayName: "",
            photoUrl: "",
            usedTags: [],
            feedbackCount: 0,
            qiitaRecommendedAllCount: 0,
            postedArticleCount: 0,
        },
        postedArticles: [],
        myArticles: [],
        feedbackArticles: []
    },
    mutations: {
        setUserDetail(state, user) {
            if (!user.postedArticleCount) user.postedArticleCount = 0;
            if (!user.feedbackCount) user.feedbackCount = 0;
            if (!user.qiitaRecommendedAllCount) user.qiitaRecommendedAllCount = 0;
            state.userDetail = user;
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
    },
    actions: {
        async fetchUserDetail({commit, rootGetters}, userId) {
            const url = rootGetters.API_URL + 'user/detail/';

            await axios.get(url, {params: {userId}}).then(res => {
                commit("setUserDetail", res.data);
            })
        },
        async fetchPostedArticles({commit, rootGetters}, userId) {
            const url = rootGetters.API_URL + 'article/'
            const params = {
                sortNum: 1,
                pageSize: 0,
                currentPage: 0,
                userId: userId,
            }
            const paramsSerializer = (params) => qs.stringify(params);

            await axios.get(url, {params, paramsSerializer})
                .then(res => {
                    commit("setPostedArticles", res.data)
                })
        },
        async fetchFeedbackArticles({commit, rootGetters}, userId) {
            const url = rootGetters.API_URL + 'article/feedbacked';

            await axios.get(url, {params: {userId}}).then(res => {
                commit("setFeedbackArticles", res.data);
            })
        },
        async fetchMyArticles({commit, rootGetters}, userId) {
            const url = rootGetters.API_URL + 'article/my-articles';

            await axios.get(url, {params: {userId}}).then(res => {
                commit("setMyArticles", res.data);
            })
        },
    }
}