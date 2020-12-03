import axios from 'axios';

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
        chartData: null,
    },
    getters: {
        userId(state) {
            return state.userDetail.userId
        },
        postedArticles(state) {
            return (state.userDetail.isLoginUser) ? state.postedArticles : state.postedArticles.filter((art) => art.stateFlag !== 0);
        },
        notDraftArticles(state) {
            return (state.postedArticles.length === 0) ? [] : state.postedArticles.filter((art) => art.stateFlag !== 0);
        },
        draftArticles(state) {
            return (state.userDetail.isLoginUser) ? (state.postedArticles.length !== 0) ? state.postedArticles.filter((art) => art.stateFlag === 0) : [] : []
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
        chartData(state) {
            return state.chartData;
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
        setChartData(state, chartData) {
            state.chartData = chartData;
        },
        setUserId(state, userId) {
            state.userDetail.userId = userId;
        },
        clearState(state) {
            state.articleCardDisplay=null
            state.postedArticles.splice(0)
            state.displayArticles.splice(0)
            state.myArticles.splice(0)
            state.feedbackArticles.splice(0)
            state.userDetail={
                userId: 0,
                displayName: "",
                photoUrl: "",
                usedTags: [],
                feedbackCount: 0,
                qiitaRecommendedAllCount: 0,
                postedArticleCount: 0,
                isLoginUser: false,
            }
            state.chartData=null
            state.usedTags.splice(0)
        }
    },
    actions: {
        clearState({commit}){
          commit("clearState");
        },
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
        setChartData({commit}, chartData) {
            commit("setChartData", chartData)
        },
        async fetchUserDetail({dispatch, commit, rootGetters, rootState}, userId) {
            const url = rootGetters.API_URL + 'user/detail/';
            let apiToken = rootState.auth.apiToken; // rootGetters["auth/apiToken"] も可

            await new Promise(((resolve, reject) => {
                axios.get(url, {
                    params: {userId},
                    headers: {
                        Authorization: apiToken,
                    },
                }).then(res => {
                    commit("setUserDetail", res.data);
                    resolve(res)
                }).catch((error) => {
                    const errorStatus = error.response.status;
                    if (errorStatus === 400) {
                        dispatch('window/setNotFound', true, {root: true})
                    } else {
                        dispatch('window/setInternalServerError', true, {root: true})
                    }
                    reject(error)
                })
            }))

        },
        fetchPostedArticles({dispatch, commit, rootGetters, rootState}, userId) {
            const url = rootGetters.API_URL + 'article/posted'
            let apiToken = rootState.auth.apiToken; // rootGetters["auth/apiToken"] も可

            axios.get(url, {
                params: {userId},
                headers: {
                    Authorization: apiToken,
                },
            })
                .then(res => {
                    commit("setPostedArticles", res.data)
                }).catch((error) => {
                console.log(error)
                const errorStatus = error.response.status;
                if (errorStatus === 400) {
                    dispatch('window/setNotFound', true, {root: true})
                } else {
                    dispatch('window/setInternalServerError', true, {root: true})
                }
            })
        },
        fetchFeedbackArticles({dispatch, commit, rootGetters, rootState}, userId) {
            const url = rootGetters.API_URL + 'article/feedbacked';
            let apiToken = rootState.auth.apiToken; // rootGetters["auth/apiToken"] も可


            axios.get(url, {
                params: {userId},
                headers: {
                    Authorization: apiToken,
                },
            }).then(res => {
                commit("setFeedbackArticles", res.data);
            }).catch((error) => {
                console.log(error)
                const errorStatus = error.response.status;
                if (errorStatus === 400) {
                    dispatch('window/setNotFound', true, {root: true})
                } else {
                    dispatch('window/setInternalServerError', true, {root: true})
                }
            })
        },
        fetchMyArticles({dispatch, commit, rootGetters, rootState}, userId) {
            const url = rootGetters.API_URL + 'article/my-articles';
            let apiToken = rootState.auth.apiToken; // rootGetters["auth/apiToken"] も可

            axios.get(url, {
                params: {userId},
                headers: {
                    Authorization: apiToken,
                },
            }).then(res => {
                commit("setMyArticles", res.data);
            }).catch((error) => {
                console.log(error)
                const errorStatus = error.response.status;
                if (errorStatus === 400) {
                    dispatch('window/setNotFound', true, {root: true})
                } else {
                    dispatch('window/setInternalServerError', true, {root: true})
                }
            })
        },
        async findUserIdByUid({dispatch, commit, rootGetters, rootState}, uid) {
            const url = rootGetters.API_URL + 'userId';
            let apiToken = rootState.auth.apiToken; // rootGetters["auth/apiToken"] も可

            await new Promise((resolve, reject) => {
                axios.get(url, {
                    params: {uid},
                    headers: {
                        Authorization: apiToken,
                    },
                }).then(res => {
                    commit("setUserId", res.data);
                    resolve(res)
                }).catch((error) => {
                    console.log(error)
                    const errorStatus = error.response.status;
                    if (errorStatus === 400) {
                        dispatch('window/setNotFound', true, {root: true})
                    } else {
                        dispatch('window/setInternalServerError', true, {root: true})
                    }
                    reject(errorStatus)
                })
            })

        }
    }
}
