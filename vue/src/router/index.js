import { createRouter, createWebHistory } from 'vue-router'
import first from "@/components/first";
import detail from "@/components/detail";
import index from "@/components/index";
const routes = [
  {
    path: '/',
    name: 'index',
    component: index
  },
  {
    path: '/first',
    name: 'first',
    component: first
  },
  {
    path: '/detail',
    name: 'detail',
    component: detail
  },

]
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
