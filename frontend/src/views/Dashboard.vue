<template>
  <div class="dashboard-container">
    <div class="page-title">
      <h2>仪表板</h2>
    </div>
    <el-row :gutter="20" class="dashboard-row">
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="card-info">
              <p class="card-number">{{ userCount }}</p>
              <p class="card-label">用户总数</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon><SuitcaseLine /></el-icon>
            </div>
            <div class="card-info">
              <p class="card-number">{{ cropCount }}</p>
              <p class="card-label">作物种类</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon><Location /></el-icon>
            </div>
            <div class="card-info">
              <p class="card-number">{{ farmlandCount }}</p>
              <p class="card-label">农田数量</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="card-info">
              <p class="card-number">{{ planCount }}</p>
              <p class="card-label">种植计划</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="dashboard-row">
      <el-col :span="16">
        <el-card class="dashboard-chart-card">
          <template #header>
            <span>作物产量趋势</span>
          </template>
          <div id="production-chart" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="dashboard-card">
          <template #header>
            <span>近期提醒</span>
          </template>
          <div class="notification-list">
            <el-scrollbar max-height="350px">
              <el-card 
                v-for="item in recentNotifications" 
                :key="item.id" 
                class="notification-item"
                shadow="never"
              >
                <div class="notification-content">
                  <p class="notification-title">{{ item.title }}</p>
                  <p class="notification-time">{{ item.time }}</p>
                </div>
              </el-card>
            </el-scrollbar>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="dashboard-row">
      <el-col :span="8">
        <el-card class="dashboard-card">
          <template #header>
            <span>农田状态分布</span>
          </template>
          <div id="farmland-status-chart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="dashboard-card">
          <template #header>
            <span>作物种类分布</span>
          </template>
          <div id="crop-type-chart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="dashboard-card">
          <template #header>
            <span>种植计划状态</span>
          </template>
          <div id="plan-status-chart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { 
  User, 
  SuitcaseLine, 
  Location, 
  TrendCharts,
  Monitor,
  Document,
  ArrowRight
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  components: {
    User,
    SuitcaseLine,
    Location,
    TrendCharts,
    Monitor,
    Document,
    ArrowRight
  },
  setup() {
    const notifications = ref([
      { id: 1, title: '番茄需要浇水', time: '今天 10:30' },
      { id: 2, title: '施肥计划到期', time: '今天 14:00' },
      { id: 3, title: '病虫害检查提醒', time: '明天 09:00' },
      { id: 4, title: '小麦收获期', time: '后天 08:00' }
    ])

    const userCount = ref(0)
    const cropCount = ref(0)
    const farmlandCount = ref(0)
    const planCount = ref(0)
    const recentNotifications = ref([])
    
    onMounted(() => {
      // 获取统计数据
      fetchDashboardData()
      
      // 初始化图表
      initProductionChart()
      initFarmlandStatusChart()
      initCropTypeChart()
      initPlanStatusChart()
    })
    
    const fetchDashboardData = () => {
      // 使用mock数据获取统计信息
      userCount.value = 128
      cropCount.value = 45
      farmlandCount.value = 24
      planCount.value = 89
      recentNotifications.value = [
        { id: 1, title: '番茄需要浇水', time: '今天 10:30' },
        { id: 2, title: '施肥计划到期', time: '今天 14:00' },
        { id: 3, title: '病虫害检查提醒', time: '明天 09:00' },
        { id: 4, title: '小麦收获期', time: '后天 08:00' }
      ]
    }
    
    const initProductionChart = () => {
      const chartDom = document.getElementById('production-chart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)
        const option = {
          title: {
            text: '作物产量趋势'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['小麦', '玉米', '水稻', '番茄']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: ['1月', '2月', '3月', '4月', '5月', '6月']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '小麦',
              type: 'line',
              data: [120, 132, 101, 134, 90, 230]
            },
            {
              name: '玉米',
              type: 'line',
              data: [220, 182, 191, 234, 290, 330]
            },
            {
              name: '水稻',
              type: 'line',
              data: [150, 232, 201, 154, 190, 330]
            },
            {
              name: '番茄',
              type: 'line',
              data: [320, 332, 301, 334, 390, 330]
            }
          ]
        }
        myChart.setOption(option)
        
        // 监听窗口大小变化
        window.addEventListener('resize', () => {
          myChart.resize()
        })
      }
    }
    
    const initFarmlandStatusChart = () => {
      const chartDom = document.getElementById('farmland-status-chart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)
        const option = {
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '5%',
            left: 'center'
          },
          series: [
            {
              name: '农田状态',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: true,
                formatter: '{b}: {d}%'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '16',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true
              },
              data: [
                { value: 15, name: '可用' },
                { value: 6, name: '已种植' },
                { value: 3, name: '维护中' }
              ]
            }
          ]
        }
        myChart.setOption(option)
        
        // 监听窗口大小变化
        window.addEventListener('resize', () => {
          myChart.resize()
        })
      }
    }
    
    const initCropTypeChart = () => {
      const chartDom = document.getElementById('crop-type-chart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)
        const option = {
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '5%',
            left: 'center'
          },
          series: [
            {
              name: '作物种类',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: true,
                formatter: '{b}: {d}%'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '16',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true
              },
              data: [
                { value: 15, name: '蔬菜类' },
                { value: 12, name: '粮食作物' },
                { value: 10, name: '经济作物' },
                { value: 8, name: '其他' }
              ]
            }
          ]
        }
        myChart.setOption(option)
        
        // 监听窗口大小变化
        window.addEventListener('resize', () => {
          myChart.resize()
        })
      }
    }
    
    const initPlanStatusChart = () => {
      const chartDom = document.getElementById('plan-status-chart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)
        const option = {
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '5%',
            left: 'center'
          },
          series: [
            {
              name: '种植计划状态',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: true,
                formatter: '{b}: {d}%'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '16',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true
              },
              data: [
                { value: 35, name: '计划中' },
                { value: 28, name: '进行中' },
                { value: 18, name: '已完成' },
                { value: 8, name: '已取消' }
              ]
            }
          ]
        }
        myChart.setOption(option)
        
        // 监听窗口大小变化
        window.addEventListener('resize', () => {
          myChart.resize()
        })
      }
    }

    return {
      userCount,
      cropCount,
      farmlandCount,
      planCount,
      recentNotifications
    }
  }
}
</script>

<style scoped>
.page-title {
  margin-bottom: 20px;
}

.page-title h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.dashboard-container {
  padding: 0px;
}

.dashboard-row {
  margin-bottom: 20px;
}

.dashboard-card {
  text-align: center;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.card-content {
  display: flex;
  align-items: center;
  width: 100%;
}

.card-icon {
  width: 60px;
  height: 60px;
  background-color: #409eff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  color: white;
  font-size: 24px;
}

.card-info {
  text-align: left;
}

.card-number {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
}

.card-label {
  font-size: 14px;
  color: #909399;
  margin: 5px 0 0 0;
}

.dashboard-chart-card {
  height: 460px;
}

.notification-list {
  padding: 10px 0;
}

.notification-item {
  margin-bottom: 10px;
  border-left: 4px solid #409eff;
}

.notification-content {
  padding: 5px 0;
}

.notification-title {
  margin: 0;
  font-weight: bold;
}

.notification-time {
  margin: 5px 0 0 0;
  font-size: 12px;
  color: #909399;
}
</style>