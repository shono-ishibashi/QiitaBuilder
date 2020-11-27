export default {
  namespaced: true,
  state: {
    notFound: false,
  },
  mutations: {
    setNotFound(state, val) {
      state.notFound = val;
    },
  },
  actions: {
    setNotFound({ commit }, val) {
      commit("setNotFound", val);
    },
  },
  getters: {
    isNotFound(state) {
      return state.notFound;
    },
  },
};
