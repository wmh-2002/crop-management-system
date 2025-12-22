<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="aside">
      <div class="logo">
        <h3>{{ isCollapse ? '农' : '农业管理系统' }}</h3>
      </div>
      <el-menu
        :default-active="$route.path"
        class="el-menu-vertical"
        :collapse="isCollapse"
        router
        unique-opened
        background-color="#304156"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="/dashboard">
          <el-icon><House /></el-icon>
          <span>仪表板</span>
        </el-menu-item>
        
        <el-sub-menu index="user">
          <template #title>
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/users">用户列表</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="crop">
          <template #title>
            <el-icon><SuitcaseLine /></el-icon>
            <span>作物管理</span>
          </template>
          <el-menu-item index="/crops">作物列表</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="farmland">
          <template #title>
            <el-icon><Location /></el-icon>
            <span>农田管理</span>
          </template>
          <el-menu-item index="/farmlands">农田列表</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="planting">
          <template #title>
            <el-icon><TrendCharts /></el-icon>
            <span>种植计划</span>
          </template>
          <el-menu-item index="/planting-plans">种植计划</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="monitoring">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <span>生长监控</span>
          </template>
          <el-menu-item index="/growth-monitoring">监控记录</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="reports">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>数据分析</span>
          </template>
          <el-menu-item index="/reports">报告中心</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="notifications">
          <template #title>
            <el-icon><Bell /></el-icon>
            <span>通知提醒</span>
          </template>
          <el-menu-item index="/notifications">通知列表</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <!-- 主内容区域 -->
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-button @click="toggleCollapse" type="primary" text>
            <el-icon v-if="!isCollapse"><Fold /></el-icon>
            <el-icon v-else><Expand /></el-icon>
          </el-button>
          <el-breadcrumb :separator-icon="ArrowRight" class="breadcrumb">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ breadcrumbTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="el-dropdown-link">
              {{ userInfo.realName || userInfo.username }} <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { 
  House, 
  User, 
  SuitcaseLine, 
  Location, 
  TrendCharts, 
  Monitor, 
  Document, 
  Bell, 
  ArrowDown, 
  Expand, 
  Fold,
  ArrowRight
} from '@element-plus/icons-vue'

export default {
  name: 'Layout',
  components: {
    House,
    User,
    SuitcaseLine,
    Location,
    TrendCharts,
    Monitor,
    Document,
    Bell,
    ArrowDown,
    Expand,
    Fold,
    ArrowRight
  },
  setup() {
    const router = useRouter()
    const isCollapse = ref(false)
    const userInfo = ref({})
    const breadcrumbTitle = ref('仪表板')

    onMounted(() => {
      // 获取用户信息
      const storedUserInfo = localStorage.getItem('userInfo')
      if (storedUserInfo) {
        userInfo.value = JSON.parse(storedUserInfo)
      }
      
      // 初始化面包屑标题
      updateBreadcrumbTitle(router.currentRoute.value.path)
      
      // 监听路由变化
      router.afterEach((to) => {
        updateBreadcrumbTitle(to.path)
      })
    })

    const updateBreadcrumbTitle = (path) => {
      switch (path) {
        case '/dashboard':
          breadcrumbTitle.value = '仪表板'
          break
        case '/users':
          breadcrumbTitle.value = '用户管理'
          break
        case '/crops':
          breadcrumbTitle.value = '作物管理'
          break
        case '/farmlands':
          breadcrumbTitle.value = '农田管理'
          break
        case '/planting-plans':
          breadcrumbTitle.value = '种植计划'
          break
        case '/growth-monitoring':
          breadcrumbTitle.value = '生长监控'
          break
        case '/reports':
          breadcrumbTitle.value = '数据分析'
          break
        case '/notifications':
          breadcrumbTitle.value = '通知与提醒'
          break
        default:
          breadcrumbTitle.value = '仪表板'
      }
    }

    const toggleCollapse = () => {
      isCollapse.value = !isCollapse.value
    }

    const logout = () => {
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 清除本地存储
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        
        // 跳转到登录页
        router.push('/login')
        ElMessage.success('已退出登录')
      })
    }

    return {
      isCollapse,
      userInfo,
      breadcrumbTitle,
      toggleCollapse,
      logout
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background-color: #304156;
  transition: width 0.3s ease;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
}

.el-menu-vertical {
  border: none;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  padding: 10px 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.breadcrumb {
  padding: 5px 0;
}

.header-right {
  display: flex;
  align-items: center;
}

.header-left .el-button {
  font-size: 16px;
}

.header-right {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
}

.main {
  background-color: #f0f2f5;
}
</style>