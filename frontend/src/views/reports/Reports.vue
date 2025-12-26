<template>
  <div class="reports-container">
    <div class="page-title">
      <h2>数据分析与报告</h2>
    </div>

    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="作物生长分析" name="crop-growth">
        <router-view />
      </el-tab-pane>
      <el-tab-pane label="农田利用率分析" name="field-utilization">
        <router-view />
      </el-tab-pane>
      <el-tab-pane label="综合统计" name="comprehensive">
        <router-view />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'Reports',
  setup() {
    const router = useRouter()
    const activeTab = ref('crop-growth')

    // Tab切换处理
    const handleTabClick = (tab) => {
      const tabName = tab.props.name
      activeTab.value = tabName

      // 根据tab切换路由
      switch (tabName) {
        case 'crop-growth':
          router.push('/reports/crop-growth')
          break
        case 'field-utilization':
          router.push('/reports/field-utilization')
          break
        case 'comprehensive':
          router.push('/reports/comprehensive')
          break
      }
    }

    onMounted(() => {
      // 根据当前路由设置activeTab
      const currentPath = router.currentRoute.value.path
      if (currentPath.includes('crop-growth')) {
        activeTab.value = 'crop-growth'
      } else if (currentPath.includes('field-utilization')) {
        activeTab.value = 'field-utilization'
      } else if (currentPath.includes('production-analysis')) {
        activeTab.value = 'production-analysis'
      } else if (currentPath.includes('weather-impact')) {
        activeTab.value = 'weather-impact'
      } else if (currentPath.includes('predictive-analysis')) {
        activeTab.value = 'predictive-analysis'
      } else if (currentPath.includes('comprehensive')) {
        activeTab.value = 'comprehensive'
      }
    })

    return {
      activeTab,
      handleTabClick
    }
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header-left {
  display: flex;
  align-items: center;
}

.card-header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}
</style>