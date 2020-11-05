import axios from 'axios';
import qs from 'qs';

export default {
    namespaced:true,
    state: {
        articles:[],
        searchCriteria:{
            sortNum:0,
            period:undefined,
            searchWord:undefined,
            searchTag:[],
            pageSize:10,
            currentPage:1
        },
        totalPage:undefined,
        tags:[]
    },
    mutations: {
        setArticles(state,articles){
            state.articles = articles
        },
        setTags(state,tags){
            state.tags = tags
        },
        setTotalPage(state,totalPage){
            state.totalPage = totalPage
            console.log(totalPage)
        }
    },
    actions: {
        async fetchArticles({commit},newSearchCriteria){
            const fetchArticlesUrl='http://localhost:8080/qiita_builder/article/'
            const fetchTotalPageUrl='http://localhost:8080/qiita_builder/article/totalPage'
            const params={
                sortNum:newSearchCriteria.sortNum,
                pageSize:newSearchCriteria.pageSize,
                currentPage:newSearchCriteria.currentPage,
                searchWord:newSearchCriteria.searchWord,
                searchTag:newSearchCriteria.searchTag,
                period:newSearchCriteria.period
            }
            const paramsSerializer = (params) => qs.stringify(params);

            await axios.get(fetchArticlesUrl,{params,paramsSerializer})
                .then(res=>{
                    console.log(res.data)
                    commit("setArticles",res.data)
                })
            await axios.get(fetchTotalPageUrl,{params,paramsSerializer})
                .then(res=>{
                    console.log(res.data)
                    commit("setTotalPage",res.data)
                })
        },
        async fetchTags({commit}){
            const url='http://localhost:8080/qiita_builder/tag/'
            await axios.get(url)
                .then(res=>{
                    console.log(res)
                    commit("setTags",res.data)
                })
        }
    }
}