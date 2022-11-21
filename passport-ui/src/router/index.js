import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    children:[
      {
        path: '/user-list',
        component: () => import('../views/admin/UserList.vue')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('../views/login/Index.vue')
  }

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  let token = localStorage.getItem("jwt");

  if (token) {
    if (to.path === '/login') { // 有 token还要去login
      next('/')
    }else {
      next()
    }
  }else {
    if (to.path === '/login') { // 没有token，去login，放行
      next()
    } else {
      next('/login') // 没有token，去其他页面

    }
  }

})

export default router
