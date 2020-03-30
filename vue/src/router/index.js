import Vue from 'vue'
import VueRouter from 'vue-router'
import seckillList from '../views/seckillList.vue'
import userInfo from '../views/userInfo.vue'
import userOrder from '../views/userOrder.vue';



Vue.use(VueRouter)

const routes = [

  {
    path:'/',
    component: seckillList
  },
  {
    path:'/seckill/userInfo',
    component: userInfo
  },
  {
    path:'/seckill/userOrder',
    component: userOrder
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
