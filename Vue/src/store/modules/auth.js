import firebase from "firebase/app";
import {v4 as uuidv4} from "uuid";
import axios from "axios";
import router from "@/router";

export default {
  namespaced: true,
  state: {
    loginUser: null,
    apiToken: "",
    isLinkedToQiita: null,
    nonValidToken: false,
  },
  mutations: {
    setLoginUser(state, loginUser) {
      state.loginUser = loginUser;
    },
    setAPIToken(state, apiToken) {
      state.apiToken = apiToken;
    },
    setIsLinkedToQiita(state, isLinked) {
      state.isLinkedToQiita = isLinked;
    },
  },
  actions: {
    async googleLogin({commit, rootGetters, dispatch}, domain) {
      //firebase google authログイン
      const provider = await new firebase.auth.GoogleAuthProvider();
      provider.setCustomParameters({
        hd: domain,
      });
      await firebase
        .auth()
        .signInWithPopup(provider)
        .then(async () => {
          const loginUser = await firebase.auth().currentUser;
          const db = await firebase.firestore();

          await commit("setLoginUser", loginUser);

          await db
            .collection("users")
            .doc(loginUser.uid)
            .get()
            .then(async (data) => {
              //firestore にユーザーのデータが存在するのかを確認
              //--あれば、そのデータを用いてRESTAPIにログイン処理を行う
              //--なければ、ユーザーデータをfirestoreに追加して、RESTAPIにも追加する

              if (!data.exists) {
                let password = await uuidv4();

                await db.collection("users").doc(loginUser.uid).set({password: password})
                  .then(() => {
                    axios.post(rootGetters.API_URL, {
                      uid: loginUser.uid,
                      password: password,
                      photoUrl: loginUser.photoURL,
                      displayName: loginUser.displayName,
                    })
                  });
              }
              await dispatch("loginRESTAPI", loginUser);
            });
        });
    },

    async loginRESTAPI({commit, rootGetters}, loginUser) {
      //RESTAPI ログイン
      const db = await firebase.firestore();
      await db.collection("users").doc(loginUser.uid).get()
        .then(async (data) => {

          const request =
            rootGetters.API_URL +
            "login?uid=" +
            (await loginUser.uid) +
            "&password=" +
            (await data.data().password);

          await axios.post(
            request,
            {},
            {headers: {"Content-Type": "application/json"}}
          ).then((response) => {
            //Vuexにjwt tokenを追加
            commit("setAPIToken", response.headers.authorization);
          })
            .catch((error) => console.log(error.status));
        });
    },
    async checkIsLinkedToQiita({commit, getters, rootGetters}) {
      const url = rootGetters.API_URL + "qiita/is-linked-to-qiita";
      const apiToken = getters.apiToken;
      const requestConfig = {
        headers: {
          Authorization: apiToken,
        },
      };
      await new Promise((resolve, reject) => {
        axios
          .get(url, requestConfig)
          .then((res) => {
            commit("setIsLinkedToQiita", res.data);
            resolve(res);
          })
          .catch((error) => {
            console.log(error);
            reject(error);
          });
      });
    },
    logout({commit, rootGetters}) {
      firebase
        .auth()
        .signOut()
        .then(function () {
          commit("setLoginUser", null);
          commit("setAPIToken", null);
          axios.post(rootGetters.API_URL + "logout").then(() => {
            router.push("/login").catch(()=>{});
          });
        });
    },
  },
  getters: {
    apiToken(state) {
      return state.apiToken;
    },
    loginUser(state) {
      return state.loginUser;
    },
    isLinkedToQiita(state) {
      return state.isLinkedToQiita;
    },
  },
};
