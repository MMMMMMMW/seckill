import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'
import VueCookies from 'vue-cookies'
Vue.use(VueCookies)


Vue.config.productionTip = false
axios.defaults.withCredentials=true  //开启发送cookie

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')


