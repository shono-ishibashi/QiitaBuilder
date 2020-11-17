import axios from "axios";
import qs from "qs";

export default {
    namespaced: true,
    state: {
        articles: [],
        searchCriteria: {
            sortNum: 0,
            period: 0,
            searchWord: "",
            toggleSearchWord: 0,
            searchTag: [],
            pageSize: 10,
            currentPage: 1,
        },
        totalPage: undefined,
        tags: [],
        errorTransistionDialog:false,
    },
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
        toggleErrorTransitionDialog(state){
            state.errorTransistionDialog=!state.errorTransistionDialog
        },
    },
    actions: {
        async fetchArticles({commit, rootGetters}, newSearchCriteria) {
            const fetchArticlesUrl = rootGetters.API_URL + 'article/'
            const fetchTotalPageUrl = rootGetters.API_URL + 'article/totalPage'
            const apiToken = await rootGetters["auth/apiToken"];
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
        },
        async fetchTags({commit, rootGetters}) {
            const url = rootGetters.API_URL + 'tag/'
            const apiToken = await rootGetters["auth/apiToken"];
            await axios.get(url, {
                headers: {
                    "Authorization": apiToken,
                }
            })
                .then(res => {
                    commit("setTags", res.data)
                })
        },
        toggleErrorTransitionDialog({commit}){
            commit("toggleErrorTransitionDialog")
        }
    }
}

