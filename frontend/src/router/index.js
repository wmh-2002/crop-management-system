import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Layout from '@/views/Layout.vue'
import Dashboard from '@/views/Dashboard.vue'
import UserManagement from '@/views/user/UserManagement.vue'
import UserDetail from '@/views/user/UserDetail.vue'
import CropManagement from '@/views/crop/CropManagement.vue'
import CropDetail from '@/views/crop/CropDetail.vue'
import FarmlandManagement from '@/views/farmland/FarmlandManagement.vue'
import PlantingPlan from '@/views/planting/PlantingPlan.vue'
import GrowthMonitoring from '@/views/monitoring/GrowthMonitoring.vue'
import Reports from '@/views/reports/Reports.vue'
import Notifications from '@/views/notifications/Notifications.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard
      },
      {
        path: 'users',
        name: 'UserManagement',
        component: UserManagement
      },
      {
        path: 'users/:id',
        name: 'UserDetail',
        component: UserDetail,
        props: true
      },
      {
        path: 'crops',
        name: 'CropManagement',
        component: CropManagement
      },
      {
        path: 'crops/:id',
        name: 'CropDetail',
        component: CropDetail,
        props: true
      },
      {
        path: 'farmlands',
        name: 'FarmlandManagement',
        component: FarmlandManagement
      },
      {
        path: 'planting-plans',
        name: 'PlantingPlan',
        component: PlantingPlan
      },
      {
        path: 'growth-monitoring',
        name: 'GrowthMonitoring',
        component: GrowthMonitoring
      },
      {
        path: 'reports',
        name: 'Reports',
        component: Reports
      },
      {
        path: 'notifications',
        name: 'Notifications',
        component: Notifications
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 检查用户是否已登录
  const token = localStorage.getItem('token')
  const userInfo = localStorage.getItem('userInfo')

  // 如果访问登录页且已登录，直接跳转到首页
  if (to.path === '/login' && token && userInfo) {
    next('/dashboard')
    return
  }

  // 如果访问非登录页且未登录，跳转到登录页
  if (to.path !== '/login' && (!token || !userInfo)) {
    next('/login')
    return
  }

  // 其他情况正常放行
  next()
})

export default router