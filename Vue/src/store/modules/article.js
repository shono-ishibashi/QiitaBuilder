import axios from "axios";
import marked from "marked";

export default {
  namespaced: true,
  state: {
    article: {
      postedUser: {},
      articleId:null,
      title:"",
      content:"",
      stateFlag:undefined,
      tags: [],
      feedbacks: [],
    },
  },
  mutations: {
    setArticle(state, article) {
      state.article = article;
    },
    mutateMarkDownText(state,text){
      state.article.content = text;
    },
    resetArticle(state){
      state.article={
        postedUser: {},
        articleId:null,
        title:"",
        content:"",
        state_flag:undefined,
        tags: []
      }
    }
  },
  actions: {
    fetchArticle({ commit, rootGetters , rootState}, articleId) {
      const url = rootGetters.API_URL + "article/" + articleId;
      var apiToken = rootState.auth.apiToken; // rootGetters["auth/apiToken"] も可

      axios
        .get(url, {
          headers: {
            Authorization: apiToken,
          },
        })
        .then((res) => {
          commit("setArticle", res.data);
        })
        .catch((error) => console.log("Error getting data : ", error));
    },
    async saveArticle({commit,rootGetters},article){
      const articleEditUrl=rootGetters.API_URL + "article/";
      const apiToken = await rootGetters["auth/apiToken"];
      const requestBody={
        articleId:article.articleId,
        content:article.content,
        title:article.title,
        stateFlag:article.stateFlag,
        tags:article.tags
      }
      console.log(requestBody)
      if(article.articleId==null){
        await axios
            .post(articleEditUrl,requestBody,{
              headers:{
                "Authorization": apiToken,
                "Content-Type": "application/json"
              }
            })
            .then(res => {
              console.log(res.data)
              commit("resetArticle")
            })
      }else{
        await axios
            .put(articleEditUrl,requestBody,{
              headers:{
                "Authorization": apiToken,
                "Content-Type": "application/json"
              }
            })
            .then(res => {
              console.log(res.data)
              commit("resetArticle")
            })
      }
    },
    resetArticle({commit}){
      commit("resetArticle")
    },
    commitMarkDownText({commit},text) {
      commit("mutateMarkDownText", text)
    }
  },
  getters: {
    compiledMarkdown:state=>marked(state.article.content)
  },
};
