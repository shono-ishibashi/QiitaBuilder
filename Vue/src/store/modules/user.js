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
        chartDisplay: null,
        articleCardDisplay: null,
    },
    getters: {
        userId(state) {
            return state.userDetail.userId
        },
        postedArticles(state) {
            return (state.userDetail.isLoginUser) ? state.postedArticles : state.postedArticles.filter((art) => art.stateFlag !== 0);
        },
        feedbackArticles(state) {
            //login(画面を開いている)ユーザーと画面に表示中のユーザーが等しいか
            //等しい中でも表示中のFB記事の投稿者自身かで下書き表示の有無を分けている
            return (state.userDetail.isLoginUser) ? state.feedbackArticles.filter(function (art) {
                if (state.userDetail.userId === art.postedUser.userId) {
                    return art
                } else {
                    return (art.stateFlag === 0) ? null : art
                }
            }) : state.feedbackArticles.filter((art) => art.stateFlag !== 0);
        },
        myArticles(state) {
            //login(画面を開いている)ユーザーと画面に表示中のユーザーが等しいか
            //等しい中でも表示中のMy記事の投稿者自身かで下書き表示の有無を分けている
            return (state.userDetail.isLoginUser) ? state.myArticles.filter(function (art) {
                if (state.userDetail.userId === art.postedUser.userId) {
                    return art
                } else {
                    return (art.stateFlag === 0) ? null : art
                }
            }) : state.myArticles.filter((art) => art.stateFlag !== 0);
        },
        displayArticles(state) {
            return state.displayArticles.filter((art) => art.stateFlag !== 9)
        },
        usedTags(state) {
            return state.usedTags;
        },
        articleCardDisplay(state) {
            return state.articleCardDisplay;
        },
        chartDisplay(state) {
            return state.chartDisplay;
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
            articles.forEach((art) => {
                state.displayArticles.push(art)
            })
        },
        setUsedTags(state, tags) {
            tags.forEach((tag) => {
                state.usedTags.push(tag)
            })
        },
        clearDisplayArticles(state) {
            state.displayArticles.splice(0);
        },
        clearUsedTag(state) {
            state.usedTags.splice(0);
        },
        setArticleCardDisplay(state, articleCard) {
            state.articleCardDisplay = articleCard;
        },
        setChartDisplay(state, chartDiaplay) {
            state.chartDisplay = chartDiaplay;
        }
    },
    actions: {
        async setArticlesAndTags({commit}, articles) {
            await commit("clearDisplayArticles");
            await commit("clearUsedTag");
            if (articles.length !== 0) {
                await commit("setDisplayArticles", articles);
                await articles.forEach((art) => commit("setUsedTags", art.tags));
            }
        },
        async setArticles({commit}, articles) {
            await commit("clearDisplayArticles");
            if (articles.length !== 0) {
                await commit("setDisplayArticles", articles);
            }
        },
        setArticleCardDisplay({commit}, articleCard) {
            commit("setArticleCardDisplay", articleCard)
        },
        setChartDisplay({commit}, chartDiaplay) {
            commit("setChartDisplay", chartDiaplay)
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
                stateFlagList: [10],
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
                params: {userId},
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
                params: {userId},
                headers: {
                    Authorization: apiToken,
                },
            }).then(res => {
                commit("setMyArticles", res.data);
            })
        },
    }
}
