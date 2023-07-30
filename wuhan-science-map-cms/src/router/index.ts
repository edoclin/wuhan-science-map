import { route } from 'quasar/wrappers'
import { createMemoryHistory, createRouter, createWebHashHistory, createWebHistory } from 'vue-router'
// @ts-ignore
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

// @ts-ignore
import generatedRoutes from 'virtual:generated-pages'
import { useMapState } from 'src/stores'
import { useUserStore } from 'stores/user_store'

/*
 * If not building with SSR mode, you can
 * directly export the Router instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Router instance.
 */
const {
  token
} = useMapState(useUserStore, ['token'])

const createHistory = process.env.SERVER
  ? createMemoryHistory
  : (process.env.VUE_ROUTER_MODE === 'history' ? createWebHistory : createWebHashHistory)

generatedRoutes.push({
  path: '/:pathMatch(.*)*',
  name: '404',
  component: () => import("/src/pages/404.vue"),
})

export const router = createRouter({
  scrollBehavior: () => ({
    left: 0,
    top: 0
  }),
  routes: generatedRoutes,
  // Leave this as is and make changes in quasar.conf.js instead!
  // quasar.conf.js -> build -> vueRouterMode
  // quasar.conf.js -> build -> publicPath
  history: createHistory(process.env.VUE_ROUTER_BASE)
})

router.beforeEach(async (to, from, next) => {
  NProgress.start() // 开启顶部加载动画
  // if (to.path == '/cms/login' || to.path === '/cms/help' || to.path === '/cms/userguide') {
  //   next()
  // } else if (JSON.stringify(token.value) == '{}') {
  //   next('/cms/login')
  // } else {
  //   next()
  // }
  next()
  NProgress.done()
})

export default route(function (/* { store, ssrContext } */) {
  return router
})
