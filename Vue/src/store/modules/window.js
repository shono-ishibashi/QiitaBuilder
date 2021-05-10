export default {
  namespaced: true,
  state: {
    notFound: false,
    forbidden: false,
    internalServerError: false,
  },
  mutations: {
    setForbidden(state, val) {
      state.forbidden = val;
    },
    setNotFound(state, val) {
      state.notFound = val;
    },
    setInternalServerError(state, val) {
      state.internalServerError = val;
    },
  },
  actions: {
    setForbidden({ commit }, val) {
      commit("setForbidden", val);
    },
    setNotFound({ commit }, val) {
      commit("setNotFound", val);
    },
    setInternalServerError({ commit }, val) {
      commit("setInternalServerError", val);
    },
    clearErrors({ commit }) {
      const val = false;
      commit("setForbidden", val);
      commit("setNotFound", val);
      commit("setInternalServerError", val);
    },
  },
  getters: {
    isForbidden(state) {
      return state.forbidden;
    },
    isNotFound(state) {
      return state.notFound;
    },
    isInternalServerError(state) {
      return state.internalServerError;
    },
  },
};
