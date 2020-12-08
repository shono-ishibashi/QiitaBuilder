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
  apiKey: "AIzaSyCIOljGNKF377Hy5i2UlWiHiKZhv7gZIas",
  authDomain: "qiitabuilder-production.firebaseapp.com",
  projectId: "qiitabuilder-production",
  storageBucket: "qiitabuilder-production.appspot.com",
  messagingSenderId: "141909287348",
  appId: "1:141909287348:web:651969cabfdd0dbfc892e1",
  measurementId: "G-FW6B709BNK"
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

