import firebase from 'firebase/app';
import {v4 as uuidv4} from 'uuid';
import axios from 'axios'
import router from "@/router";

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
        async googleLoginPartners({commit, rootGetters}) {
            //firebase google authログイン
            const provider = await new firebase.auth.GoogleAuthProvider();
            provider.setCustomParameters({
                hd: 'rakus-partners.co.jp'
            });
            await firebase.auth().signInWithPopup(provider).then(async () => {
                    const loginUser = await firebase.auth().currentUser;
                    const db = await firebase.firestore();

                    await commit('setLoginUser', loginUser);


                    await db.collection('users').doc(loginUser.uid).get()
                        .then(async data => {
                            //firestore にユーザーのデータが存在するのかを確認
                            //--あれば、そのデータを用いてRESTAPIにログイン処理を行う
                            //--なければ、ユーザーデータをfirestoreに追加して、RESTAPIにも追加する

                            if (!data.exists) {
                                let password = await uuidv4();

                                await db.collection('users').doc(loginUser.uid)
                                    .set({password: password}).then(() => {
                                    axios.post(rootGetters.API_URL, {
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
        async googleLoginRakus({commit, rootGetters}) {
            //firebase google authログイン
            const provider = await new firebase.auth.GoogleAuthProvider();
            provider.setCustomParameters({
                hd: 'rakus.co.jp'
            });
            await firebase.auth().signInWithPopup(provider).then(async () => {
                    const loginUser = await firebase.auth().currentUser;
                    const db = await firebase.firestore();

                    await commit('setLoginUser', loginUser);


                    await db.collection('users').doc(loginUser.uid).get()
                        .then(async data => {
                            //firestore にユーザーのデータが存在するのかを確認
                            //--あれば、そのデータを用いてRESTAPIにログイン処理を行う
                            //--なければ、ユーザーデータをfirestoreに追加して、RESTAPIにも追加する

                            if (!data.exists) {
                                let password = await uuidv4();

                                await db.collection('users').doc(loginUser.uid)
                                    .set({password: password}).then(() => {
                                        axios.post(rootGetters.API_URL, {
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
        async loginRESTAPI({commit, rootGetters}, loginUser) {
            //RESTAPI ログイン
            console.log('loginRESTAPI');
            const db = await firebase.firestore();
            await db.collection('users').doc(loginUser.uid).get()
                .then(async data => {
                    await console.log(data);
                    await console.log(data.data());
                    const request = rootGetters.API_URL + 'login?uid=' + await loginUser.uid + '&password=' + await data.data().password;
                    await console.log(data.data());
                    await axios.post(request, {}, {headers: {'Content-Type': 'application/json'}})
                        .then(response => {
                            //Vuexにjwt tokenを追加
                            commit("setAPIToken", response.headers.authorization);
                        }).catch(error =>
                            console.log(error.status)
                        )
                })
        },
        logout({commit, rootGetters}) {
            firebase.auth().signOut().then(function () {
                commit('setLoginUser', null);
                commit('setAPIToken', null);
                axios.post(rootGetters.API_URL + 'logout').then(() => {
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
