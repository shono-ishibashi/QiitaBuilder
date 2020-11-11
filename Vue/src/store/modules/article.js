import axios from "axios";

export default {
  namespaced: true,
  state: {
    article: {
      postedUser: {},
      articleId: null,
      title: undefined,
      content:
        '## Java で hello world を出力させる方法\n```java\npublic static void main(String[] args){\n    System.out.println("hello world");\n}\n```',
      stateFlag: undefined,
      tags: [],
      feedbacks: [],
    },
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
        title: undefined,
        content:
          '## Java で hello world を出力させる方法\n```java\npublic static void main(String[] args){\n    System.out.println("hello world");\n}\n```',
        state_flag: undefined,
        tags: [],
      };
    },
    addFeedback(state, feedback) {
      state.article.feedbacks.push(feedback);
    },
    removeFeedback(state, feedbackId) {
      const targetIndex = state.article.feedbacks.findIndex(
        (feedback) => feedback.feedbackId == feedbackId
      );
      state.article.feedbacks.splice(targetIndex, 1);
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
    async deleteFeedback({ commit, rootGetters }, feedbackId) {
      const url = rootGetters.API_URL + "feedback/" + feedbackId;
      const apiToken = rootGetters["auth/apiToken"];
      const requestConfig = {
        headers: {
          Authorization: apiToken,
        },
      };

      try {
        await axios.delete(url, requestConfig);
        commit("removeFeedback", feedbackId);
      } catch (error) {
        console.log(error);
      }
    },
  },
  getters: {},
};
