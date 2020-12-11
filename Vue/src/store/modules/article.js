import axios from "axios";
import marked from "marked";
import QiitaAPI from "@/mixins/QiitaAPI";

export default {
    namespaced: true,
    state: {
        article: {
            postedUser: {},
            articleId: null,
            title: "",
            content: "",
            stateFlag: undefined,
            articleVersion: undefined,
            tags: undefined,
            feedbacks: [],
            qiitaRecommendPoint: null,
        },
        myArticleId: null,
        recommendId: null,
        processFailure: false,
    },
    getters: {
        compiledMarkdown: (state) => marked(state.article.content)
    },
    mutations: {
        setArticle(state, article) {
            state.article = article;
        },
        mutateMarkDownText(state, text) {
            state.article.content = text;
        },
        resetArticle(state) {
            state.article = {
                postedUser: {},
                articleId: null,
                title: "",
                content: "",
                stateFlag: undefined,
                articleVersion: undefined,
                tags: undefined,
                feedbacks: [],
                qiitaRecommendPoint: null,
            };
        },
        // feedback
        addFeedback(state, feedback) {
            state.article.feedbacks.push(feedback);
        },
        updateFeedback(state, feedback) {
            const targetIndex = state.article.feedbacks.findIndex(
                (elem) => elem.feedbackId == feedback.feedbackId
            );
            state.article.feedbacks.splice(targetIndex, 1, feedback);
        },
        removeFeedback(state, feedbackId) {
            const targetIndex = state.article.feedbacks.findIndex(
                (feedback) => feedback.feedbackId == feedbackId
            );
            state.article.feedbacks.splice(targetIndex, 1);
        },
        // MyArticle
        setMyArticleId(state, myArticleId) {
            state.myArticleId = myArticleId;
        },
        // Recommend
        setRecommendId(state, recommendId) {
            state.recommendId = recommendId;
        },
        incrementQiitaRecommendPoint(state) {
            state.article.qiitaRecommendPoint++;
        },
        decrementQiitaRecommendPoint(state) {
            state.article.qiitaRecommendPoint--;
        },
        updateStateFlag(state, stateFlag) {
            state.article.stateFlag = stateFlag;
        },
        toggleProcessFailure(state) {
            state.processFailure = !state.processFailure
        }
    },
    actions: {
        async postArticleToQiita({commit, rootGetters, rootState, dispatch}, articleId) {
            if (await rootState.auth.isLinkedToQiita) {
                axios.post(rootGetters.API_URL + 'qiita/save-article-to-qiita/' + articleId, {}, {
                    headers: {
                        "Authorization": rootState.auth.apiToken
                    }
                }).then(() => {
                    alert('Qiitaに記事を投稿しました');
                    commit('updateStateFlag', 2);
                }).catch(async ({response}) => {
                        const errorLocation = 'QiitaAPI';
                        //======================401======================
                        if (response.status === 401 && response.data.message === errorLocation) {
                            if (await confirm('Qiitaとの連携が確認できませんでした。Qiitaと連携しますか？')) {
                                await QiitaAPI.methods.toQiitaAPIAuthentication()
                            }
                            //======================403======================
                        } else if (response.status === 403 && response.data.message === errorLocation) {
                            await alert('この記事を更新する権限はありません。');
                            //======================404======================
                        } else if (response.status === 404 && response.data.message === errorLocation) {
                            if (confirm('Qiitaの記事が見つからないため、更新できませんでした。\nQiitaに再投稿しますか？')) {
                                await commit("updateStateFlag", 2);
                                await dispatch('postArticleToQiita', articleId);
                            } else {
                                await commit("updateStateFlag", 1);
                            }
                            //======================else======================
                        } else {
                            if (await confirm('Qiitaへの記事投稿に失敗しました。Qiita連携からやり直しますか?')) {
                                await QiitaAPI.methods.toQiitaAPIAuthentication()
                            }
                        }
                    }
                )
            } else {
                await localStorage.setItem('articleId', articleId);
                await QiitaAPI.methods.toQiitaAPIAuthentication();
            }
        },
        async fetchArticle({commit, rootGetters, rootState}, articleId) {
            const url = rootGetters.API_URL + "article/" + articleId;
            const apiToken = rootState.auth.apiToken;// rootGetters["auth/apiToken"] も可
            const reqHeader = {
                headers: {
                    Authorization: apiToken,
                },
            };
            await new Promise((resolve, reject) => {
                axios
                    .get(url, reqHeader)
                    .then((res) => {
                        commit("setArticle", res.data);
                        resolve(res);
                    })
                    .catch((error) => {
                        console.log(error);
                        reject(error);
                    });
            });
        },
        async saveArticle({rootState, rootGetters}, article) {
            const articleEditUrl = rootGetters.API_URL + "article/";
            const apiToken = rootState.auth.apiToken;
            const insertRequestBody = {
                articleId: article.articleId,
                content: article.content,
                title: article.title,
                stateFlag: article.stateFlag,
                tags: article.tags,
            };
            const updateRequestBody = {
                articleId: article.articleId,
                content: article.content,
                title: article.title,
                stateFlag: article.stateFlag,
                articleVersion: article.articleVersion,
                tags: article.tags,
            }
            const requestConfig = {
                headers: {
                    Authorization: apiToken,
                    "Content-Type": "application/json",
                }
            }
            //insert処理
            if (article.articleId == null) {
                await new Promise((resolve, reject) => {
                    axios.post(articleEditUrl, insertRequestBody, requestConfig)
                        .then((res) => {
                            resolve(res)
                        })
                        .catch((error) => {
                            console.log(error);
                            reject(error)
                        })
                })
                //  update処理
            } else {
                await new Promise((resolve, reject) => {
                    axios.put(articleEditUrl, updateRequestBody, requestConfig)
                        .then((res) => {
                            resolve(res)
                        })
                        .catch((error) => {
                            console.log(error);
                            reject(error)
                        })
                })
            }
        },
        resetArticle({commit}) {
            commit("resetArticle");
        },
        commitMarkDownText({commit}, text) {
            commit("mutateMarkDownText", text);
        },
        // feedback
        async postFeedback({commit, rootState, rootGetters}, feedback) {
            const url = rootGetters.API_URL + "feedback";
            const apiToken = rootState.auth.apiToken;
            const requestConfig = {
                headers: {
                    Authorization: apiToken,
                },
            };
            await new Promise((resolve, reject) => {
                axios
                    .post(url, feedback, requestConfig)
                    .then((res) => {
                        commit("addFeedback", res.data);
                        resolve(res);
                    })
                    .catch((error) => {
                        console.log(error);
                        reject(error);
                    });
            });
        },
        async updateFeedback({commit, rootState, rootGetters}, feedback) {
            const url = rootGetters.API_URL + "feedback";
            const apiToken = rootState.auth.apiToken;
            const requestConfig = {
                headers: {
                    Authorization: apiToken,
                },
            };
            await new Promise((resolve, reject) => {
                axios
                    .put(url, feedback, requestConfig)
                    .then((res) => {
                        commit("updateFeedback", res.data);
                        resolve(res);
                    })
                    .catch((error) => {
                        console.log(error);
                        reject(error);
                    });
            });
        },
        async deleteFeedback({commit, rootState, rootGetters}, feedback) {
            const url = rootGetters.API_URL + "feedback";
            const apiToken = rootState.auth.apiToken;
            const requestConfig = {
                headers: {
                    Authorization: apiToken,
                },
            };
            // 削除フラグ
            feedback.deleteFlag = 1;
            await new Promise((resolve, reject) => {
                axios
                    .put(url, feedback, requestConfig)
                    .then((res) => {
                        commit("removeFeedback", feedback.feedbackId);
                        resolve(res);
                    })
                    .catch((error) => {
                        console.log(error);
                        reject(error);
                    });
            });
        },
        // MyArticle
        async fetchMyArticle({commit, rootState, rootGetters}, articleId) {
            const url = rootGetters.API_URL + "my-article?articleId=" + articleId;
            const apiToken = rootState.auth.apiToken;
            const requestConfig = {
                headers: {
                    Authorization: apiToken,
                },
            };
            await new Promise((resolve, reject) => {
                axios
                    .get(url, requestConfig)
                    .then((res) => {
                        commit("setMyArticleId", res.data.myArticleId);
                        resolve(res);
                    })
                    .catch((error) => {
                        console.log(error);
                        reject(error);
                    });
            });
        },
        async registerMyArticle({commit, rootState, rootGetters}, articleId) {
            const url = rootGetters.API_URL + "my-article";
            const requestBody = {
                articleId: articleId,
            };
            const apiToken = rootState.auth.apiToken;
            const requestConfig = {
                headers: {
                    Authorization: apiToken,
                },
            };
            await new Promise((resolve, reject) => {
                axios
                    .post(url, requestBody, requestConfig)
                    .then((res) => {
                        commit("setMyArticleId", res.data.myArticleId);
                        resolve(res);
                    })
                    .catch((error) => {
                        console.log(error);
                        reject(error);
                    });
            });
        },
        async deleteMyArticle({commit, rootState, rootGetters}, myArticleId) {
            const url = rootGetters.API_URL + "my-article/" + myArticleId;
            const apiToken = rootState.auth.apiToken;
            const requestConfig = {
                headers: {
                    Authorization: apiToken,
                },
            };
            await new Promise((resolve, reject) => {
                axios
                    .delete(url, requestConfig)
                    .then((res) => {
                        commit("setMyArticleId", null);
                        resolve(res);
                    })
                    .catch((error) => {
                        console.log(error);
                        reject(error);
                    });
            });
        },
        // Recommend
        async fetchRecommend({commit, rootState, rootGetters}, articleId) {
            const url = rootGetters.API_URL + "recommend?articleId=" + articleId;
            const apiToken = rootState.auth.apiToken;
            const requestConfig = {
                headers: {
                    Authorization: apiToken,
                },
            };
            await new Promise((resolve, reject) => {
                axios
                    .get(url, requestConfig)
                    .then((res) => {
                        commit("setRecommendId", res.data.recommendId);
                        resolve(res);
                    })
                    .catch((error) => {
                        console.log(error);
                        reject(error);
                    });
            });
        },
        async registerRecommend({commit, rootState, rootGetters}, articleId) {
            const url = rootGetters.API_URL + "recommend";
            const requestBody = {
                articleId: articleId,
            };
            const apiToken = rootState.auth.apiToken;
            const requestConfig = {
                headers: {
                    Authorization: apiToken,
                },
            };
            await new Promise((resolve, reject) => {
                axios
                    .post(url, requestBody, requestConfig)
                    .then((res) => {
                        commit("setRecommendId", res.data.recommendId);
                        commit("incrementQiitaRecommendPoint");
                        resolve(res);
                    })
                    .catch((error) => {
                        console.log(error);
                        reject(error);
                    });
            });
        },
        async deleteRecommend({commit, rootState, rootGetters}, recommendId) {
            const url = rootGetters.API_URL + "recommend/" + recommendId;
            const apiToken = rootState.auth.apiToken;
            const requestConfig = {
                headers: {
                    Authorization: apiToken,
                },
            };
            await new Promise((resolve, reject) => {
                axios
                    .delete(url, requestConfig)
                    .then((res) => {
                        commit("setRecommendId", null);
                        commit("decrementQiitaRecommendPoint");
                        resolve(res);
                    })
                    .catch((error) => {
                        console.log(error);
                        reject(error);
                    });
            });
        },
        // 認証の通ったユーザーであれば該当する記事とタグを取得
        // params(articleId: 記事ID,userid: ユーザーID)
        async fetchArticleEdit({dispatch, state, rootState, rootGetters}, params) {
            const apiToken = rootState.auth.apiToken;
            // アクセス権限のあるユーザーだと200が返ってくる
            await axios.get(rootGetters.API_URL + 'article/isExist', {
                params: params,
                headers: {
                    "Authorization": apiToken,
                    "Content-Type": "application/json"
                },
            })
                .then(() => {
                    dispatch('resetArticle')
                    dispatch('articles/fetchTags', null, {root: true})
                    dispatch('fetchArticle', params.articleId)
                        // ここの処理はコンポーネントに書く
                        .then(() => {
                            if (state.article.stateFlag === 9) {
                                dispatch("window/setNotFound", true, {root: true});
                            }
                        })
                        .catch((error) => {
                            const errorStatus = error.response.status;
                            if (errorStatus === 400) {
                                dispatch('window/setNotFound', true, {root: true})
                            } else {
                                dispatch('window/setInternalServerError', true, {root: true})
                            }
                        })

                })
                .catch(() => {
                    dispatch("window/setNotFound", true, {root: true});
                })
        },
        toggleProcessFailure({commit}) {
            commit("toggleProcessFailure");
        }
    }
};
