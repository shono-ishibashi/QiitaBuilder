import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import vuetify from './plugins/vuetify';
import firebase from 'firebase';
import QiitaAPI from "@/mixins/QiitaAPI";

Vue.config.productionTip = false

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyDA9nF4IcWmD95tbTL1RxFjiYIjve3A5uM",
  authDomain: "qiitabuilder.firebaseapp.com",
  databaseURL: "https://qiitabuilder.firebaseio.com",
  projectId: "qiitabuilder",
  storageBucket: "qiitabuilder.appspot.com",
  messagingSenderId: "407476193145",
  appId: "1:407476193145:web:17059e995600b52589d1a8",
  measurementId: "G-YTV09E8XCC"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);
firebase.analytics();

Vue.mixin(QiitaAPI);

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')

