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
        tags:[]
    },
    mutations: {
        setArticles(state,articles){
            state.articles = articles
        },
        setTags(state,tags){
            state.tags = tags
        }
    },
    actions: {
        async fetchArticles({commit},newSearchCriteria){
            const url='http://localhost:8080/qiita_builder/article/'
            const params={
                sortNum:newSearchCriteria.sortNum,
                pageSize:newSearchCriteria.pageSize,
                currentPage:newSearchCriteria.currentPage,
                searchWord:newSearchCriteria.searchWord,
                searchTag:newSearchCriteria.searchTag,
                period:newSearchCriteria.period
            }
            const paramsSerializer = (params) => qs.stringify(params);

            await axios.get(url,{params,paramsSerializer})
                .then(res=>{
                    console.log(res.data)
                    commit("setArticles",res.data)
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