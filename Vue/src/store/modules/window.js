export default {
  namespaced: true,
  state: {
    notFound: false,
    internalServerError: false,
  },
  mutations: {
    setNotFound(state, val) {
      state.notFound = val;
    },
    setInternalServerError(state, val) {
      state.internalServerError = val;
    },
  },
  actions: {
    setNotFound({ commit }, val) {
      commit("setNotFound", val);
    },
    setInternalServerError({ commit }, val) {
      commit("setInternalServerError", val);
    },
    clearErrors({ commit }) {
      const val = false;
      commit("setNotFound", val);
      commit("setInternalServerError", val);
    },
  },
  getters: {
    isNotFound(state) {
      return state.notFound;
    },
    isInternalServerError(state) {
      return state.internalServerError;
    },
  },
};
