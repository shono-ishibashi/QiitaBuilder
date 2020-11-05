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
    fetchArticle({ commit }, articleId) {
      const url = "http://localhost:8080/qiita_builder/article/" + articleId;
      axios
        .get(url)
        .then((res) => {
          commit("setArticle", res.data);
          console.log("Data : ", res.data);
        })
        .catch((error) => console.log("Error getting data : ", error));
    },
  },
  getters: {},
};
