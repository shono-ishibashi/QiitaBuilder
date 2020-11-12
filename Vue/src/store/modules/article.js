import axios from "axios";
import marked from "marked";

export default {
  namespaced: true,
  state: {
    article: {
      postedUser: {},
      articleId: null,
      title: "",
      content: "",
      stateFlag: undefined,
      tags: [],
      feedbacks: [],
      qiitaRecommendPoint: null,
    },
    myArticleId: null,
    recommendId: null,
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
        state_flag: undefined,
        tags: [],
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
  },
  actions: {
    fetchArticle({ commit, rootGetters, rootState }, articleId) {
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
    async saveArticle({ commit, rootGetters }, article) {
      const articleEditUrl = rootGetters.API_URL + "article/";
      const apiToken = await rootGetters["auth/apiToken"];
      const requestBody = {
        articleId: article.articleId,
        content: article.content,
        title: article.title,
        stateFlag: article.stateFlag,
        tags: article.tags,
      };
      console.log(requestBody);
      if (article.articleId == null) {
        await axios
          .post(articleEditUrl, requestBody, {
            headers: {
              Authorization: apiToken,
              "Content-Type": "application/json",
            },
          })
          .then((res) => {
            console.log(res.data);
            commit("resetArticle");
          });
      } else {
        await axios
          .put(articleEditUrl, requestBody, {
            headers: {
              Authorization: apiToken,
              "Content-Type": "application/json",
            },
          })
          .then((res) => {
            console.log(res.data);
            commit("resetArticle");
          });
      }
    },
    resetArticle({ commit }) {
      commit("resetArticle");
    },
    commitMarkDownText({ commit }, text) {
      commit("mutateMarkDownText", text);
    },
    // feedback
    async postFeedback({ commit, rootGetters }, feedback) {
      const url = rootGetters.API_URL + "feedback";
      const apiToken = rootGetters["auth/apiToken"];
      const requestConfig = {
        headers: {
          Authorization: apiToken,
        },
      };

      try {
        const res = await axios.post(url, feedback, requestConfig);
        const item = res.data;
        commit("addFeedback", item);
      } catch (error) {
        console.log(error);
      }
    },
    async updateFeedback({ commit, rootGetters }, feedback) {
      const url = rootGetters.API_URL + "feedback";
      const apiToken = rootGetters["auth/apiToken"];
      const requestConfig = {
        headers: {
          Authorization: apiToken,
        },
      };

      try {
        const res = await axios.put(url, feedback, requestConfig);
        commit("updateFeedback", res.data);
      } catch (error) {
        console.log(error);
      }
    },
    async deleteFeedback({ commit, rootGetters }, feedback) {
      const url = rootGetters.API_URL + "feedback";
      const apiToken = rootGetters["auth/apiToken"];
      const requestConfig = {
        headers: {
          Authorization: apiToken,
        },
      };
      // 削除フラグ
      feedback.deleteFlag = 1;

      try {
        await axios.put(url, feedback, requestConfig);
        commit("removeFeedback", feedback.feedbackId);
      } catch (error) {
        console.log(error);
      }
    },
    // MyArticle
    async fetchMyArticle({ commit, rootGetters }, articleId) {
      const url = rootGetters.API_URL + "my-article?articleId=" + articleId;
      const apiToken = rootGetters["auth/apiToken"];
      const requestConfig = {
        headers: {
          Authorization: apiToken,
        },
      };
      try {
        const res = await axios.get(url, requestConfig);
        commit("setMyArticleId", res.data.myArticleId);
      } catch (error) {
        console.log(error);
      }
    },
    async registerMyArticle({ commit, rootGetters }, articleId) {
      const url = rootGetters.API_URL + "my-article";
      const requestBody = {
        articleId: articleId,
      };
      const apiToken = rootGetters["auth/apiToken"];
      const requestConfig = {
        headers: {
          Authorization: apiToken,
        },
      };
      try {
        const res = await axios.post(url, requestBody, requestConfig);
        commit("setMyArticleId", res.data.myArticleId);
      } catch (error) {
        console.log(error);
      }
    },
    async deleteMyArticle({ commit, rootGetters }, myArticleId) {
      const url = rootGetters.API_URL + "my-article/" + myArticleId;
      const apiToken = rootGetters["auth/apiToken"];
      const requestConfig = {
        headers: {
          Authorization: apiToken,
        },
      };
      try {
        await axios.delete(url, requestConfig);
        commit("setMyArticleId", null);
      } catch (error) {
        console.log(error);
      }
    },
    // Recommend
    async fetchRecommend({ commit, rootGetters }, articleId) {
      const url = rootGetters.API_URL + "recommend?articleId=" + articleId;
      const apiToken = rootGetters["auth/apiToken"];
      const requestConfig = {
        headers: {
          Authorization: apiToken,
        },
      };
      try {
        const res = await axios.get(url, requestConfig);
        commit("setRecommendId", res.data.recommendId);
      } catch (error) {
        console.log(error);
      }
    },
    async registerRecommend({ commit, rootGetters }, articleId) {
      const url = rootGetters.API_URL + "recommend";
      const requestBody = {
        articleId: articleId,
      };
      const apiToken = rootGetters["auth/apiToken"];
      const requestConfig = {
        headers: {
          Authorization: apiToken,
        },
      };
      try {
        const res = await axios.post(url, requestBody, requestConfig);
        commit("setRecommendId", res.data.recommendId);
        commit("incrementQiitaRecommendPoint");
      } catch (error) {
        console.log(error);
      }
    },
    async deleteRecommend({ commit, rootGetters }, recommendId) {
      const url = rootGetters.API_URL + "recommend/" + recommendId;
      const apiToken = rootGetters["auth/apiToken"];
      const requestConfig = {
        headers: {
          Authorization: apiToken,
        },
      };
      try {
        await axios.delete(url, requestConfig);
        commit("setRecommendId", null);
        commit("decrementQiitaRecommendPoint");
      } catch (error) {
        console.log(error);
      }
    },
  },
  getters: {
    compiledMarkdown: (state) => marked(state.article.content),
  },
};
