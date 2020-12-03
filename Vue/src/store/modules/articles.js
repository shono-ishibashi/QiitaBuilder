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
        // 遷移エラー時に表示するダイアログ
        errorTransistionDialog: false,
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
        toggleErrorTransitionDialog(state) {
            state.errorTransistionDialog = !state.errorTransistionDialog
        },
        setToggleSearchWord(state,selected){
            state.searchCriteria.toggleSearchWord = selected
        }
    },
    actions: {
        async fetchArticles({commit, rootState, rootGetters}, newSearchCriteria) {
            const fetchArticlesUrl = rootGetters.API_URL + 'article/'
            const fetchTotalPageUrl = rootGetters.API_URL + 'article/totalPage'
            const apiToken = rootState.auth.apiToken;
            const params = {
                sortNum: newSearchCriteria.sortNum,
                pageSize: newSearchCriteria.pageSize,
                currentPage: 1,
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
                .catch(error=>{
                    console.log(error)
                })
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
                .catch((error) =>{
                    console.log(error)
                })
        },
        async fetchTags({commit, rootState, rootGetters}) {
            const url = rootGetters.API_URL + 'tag/'
            const apiToken = rootState.auth.apiToken;
            await axios.get(url, {
                headers: {
                    "Authorization": apiToken,
                }
            })
                .then(res => {
                    commit("setTags", res.data)
                })
        },
        toggleErrorTransitionDialog({commit}) {
            commit("toggleErrorTransitionDialog")
        },
        setToggleSearchWord({commit},selected){
            commit('setToggleSearchWord',selected)
        }
    }
}

