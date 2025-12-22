import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Layout from '@/views/Layout.vue'
import Dashboard from '@/views/Dashboard.vue'
import UserManagement from '@/views/user/UserManagement.vue'
import CropManagement from '@/views/crop/CropManagement.vue'
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
        path: 'crops',
        name: 'CropManagement',
        component: CropManagement
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

export default router