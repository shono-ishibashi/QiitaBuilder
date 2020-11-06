import axios from "axios";

export default {
  namespaced: true,
  state: {
    article: {
      postedUser: {},
    },
  },
  mutations: {
    setArticle(state, article) {
      state.article = article;
    },
  },
  actions: {
    fetchArticle({ commit, rootGetters }, articleId) {
      const url = rootGetters.API_URL + "article/" + articleId;
      axios
        .get(url)
        .then((res) => {
          commit("setArticle", res.data);
        })
        .catch((error) => console.log("Error getting data : ", error));
    },
  },
  getters: {},
};
