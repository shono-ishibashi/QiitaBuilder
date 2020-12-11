import axios from "axios";
import qs from "qs";

export default {
    namespaced: true,
    state: {
        articles: [],
        searchCriteria: {
            sortNum: 0,
            period: null,
            searchWord: "",
            toggleSearchWord: "0",
            searchTag: [],
            pageSize: 10,
            currentPage: 1,
        },
        totalPage: undefined,
        tags: [],
    },
    getters: {
        tagNameList: state => state.tags.map(tag => tag.tagName)
    }
    ,
    mutations: {
        setArticles(state, articles) {
            state.articles = articles
        },
        setTags(state, tags) {
            state.tags = tags
        },
        setTotalPage(state, totalPage) {
            state.totalPage = totalPage
        },
        setToggleSearchWord(state, selected) {
            state.searchCriteria.toggleSearchWord = selected
        },
        resetArticles(state){
            state.articles = []
        }
    },
    actions: {
        async fetchArticles({dispatch, commit, rootState, rootGetters}, newSearchCriteria) {
            const fetchArticlesUrl = rootGetters.API_URL + 'article/'
            const fetchTotalPageUrl = rootGetters.API_URL + 'article/totalPage'
            const apiToken = rootState.auth.apiToken;
            const params = {
                sortNum: newSearchCriteria.sortNum,
                pageSize: newSearchCriteria.pageSize,
                currentPage: newSearchCriteria.currentPage,
                searchWord: newSearchCriteria.searchWord,
                toggleSearchWord: newSearchCriteria.toggleSearchWord,
                searchTag: newSearchCriteria.searchTag,
                period: newSearchCriteria.period
            }
            const paramsSerializer = (params) => qs.stringify(params);
            //記事一覧の取得
            await axios.get(fetchArticlesUrl, {
                params: params,
                headers: {
                    "Authorization": apiToken,
                    "Content-Type": "application/json"
                },
                paramsSerializer: paramsSerializer
            })
                .then(res => {
                    commit("setArticles", res.data)
                })
                .catch(error => {
                        const errorStatus = error.response.status;
                        if (errorStatus === 400) {
                            dispatch('window/setNotFound', true, {root: true})
                        } else {
                            dispatch('window/setInternalServerError', true, {root: true})
                        }
                    }
                )
//記事ページ数の取得
            await axios.get(fetchTotalPageUrl, {
                params: params,
                headers: {
                    "Authorization": apiToken,
                    "Content-Type": "application/json"
                },
                paramsSerializer: paramsSerializer
            })
                .then(res => {
                    commit("setTotalPage", res.data)
                })
                .catch((error) => {
                    const errorStatus = error.response.status;
                    if (errorStatus === 400) {
                        dispatch('window/setNotFound', true, {root: true})
                    } else {
                        dispatch('window/setInternalServerError', true, {root: true})
                    }
                })
        },
        async fetchTags({dispatch,commit, rootState, rootGetters}) {
            const url = rootGetters.API_URL + 'tag/'
            const apiToken = rootState.auth.apiToken;
            await axios.get(url, {
                headers: {
                    "Authorization": apiToken,
                }
            })
                .then(res => {
                    commit("setTags", res.data)
                }).catch(error=>{
                    const errorStatus=error.response.status
                    if(errorStatus){
                        dispatch('window/setInternalServerError', true, {root: true})
                    }
                })
        },
        setToggleSearchWord({commit}, selected) {
            commit('setToggleSearchWord', selected)
        }
    }
}

