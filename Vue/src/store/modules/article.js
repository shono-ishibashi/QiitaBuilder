import axios from "axios";

export default {
  namespaced: true,
  state: {
    article: {
      postedUser: {},
      feedbacks: [],
    },
  },
  mutations: {
    setArticle(state, article) {
      state.article = article;
    },
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
  },
  getters: {},
};
