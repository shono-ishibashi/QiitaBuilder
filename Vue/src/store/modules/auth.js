import firebase from 'firebase/app';
import {v4 as uuidv4} from 'uuid';
import axios from 'axios'
import router from "@/router";

const API_URL = 'http://localhost:8080/qiita_builder/';

export default {
    namespaced: true,
    state: {
        loginUser: null,
        apiToken: ''
    },
    mutations: {
        setLoginUser(state, loginUser) {
            state.loginUser = loginUser;
        },
        setAPIToken(state, apiToken) {
            state.apiToken = apiToken;
        }

    },
    actions: {
        async googleLogin({commit}) {
            //firebase google authログイン
            const provider = await new firebase.auth.GoogleAuthProvider();
            await firebase.auth().signInWithPopup(provider).then(() => {
                    const loginUser = firebase.auth().currentUser;
                    const db = firebase.firestore();

                    commit('setLoginUser', loginUser);

                    db.collection('users').doc(loginUser.uid).get()
                        .then(data => {
                            //firestore にユーザーのデータが存在するのかを確認
                            //--あれば、そのデータを用いてRESTAPIにログイン処理を行う
                            //--なければ、ユーザーデータをfirestoreに追加して、RESTAPIにも追加する

                            if (!data.exists) {
                                const password = uuidv4();
                                db.collection('users').doc(loginUser.uid)
                                    .set({password: password}).then(() => {
                                    axios.post(API_URL, {
                                        uid: loginUser.uid,
                                        password: password,
                                        photoUrl: loginUser.photoURL,
                                        displayName: loginUser.displayName
                                    }).then((result) => {
                                        console.log(result);
                                    })
                                })
                            }
                        })


                },
            )
        },
        async loginRESTAPI({commit}, loginUser) {
            //RESTAPI ログイン
            const db = await firebase.firestore();
            await db.collection('users').doc(loginUser.uid).get()
                .then(data => {
                    const request = API_URL + 'login?uid=' + loginUser.uid + '&password=' + data.data().password;
                    console.log(data.data());
                    axios.post(request, {}, {headers: {'Content-Type': 'application/json'}})
                        .then(response => {
                            //Vuexにjwt tokenを追加
                            commit("setAPIToken", response.headers.authorization);
                            router.push('/')
                        })
                })
        },
        logout({commit}) {
            firebase.auth().signOut().then(function () {
                commit('setLoginUser', null);
                axios.post(API_URL + 'logout').then(() => {
                    router.push('/login')
                })
            })
        }
    },
    getters: {
        apiToken(state) {
            return state.apiToken;
        },
        loginUser(state) {
            return state.loginUser;
        }
    }
}
