<template>
  <div class="analysis-container">
    <div class="page-header">
      <h3>综合统计</h3>
      <div class="header-actions">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          style="width: 300px"
          @change="handleDateChange"
        />
        <el-button type="primary" @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="4">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.userCount }}</p>
              <p class="stat-label">用户总数</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><SuitcaseLine /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.cropCount }}</p>
              <p class="stat-label">作物种类</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Location /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.farmlandCount }}</p>
              <p class="stat-label">农田数量</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.planCount }}</p>
              <p class="stat-label">种植计划</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Monitor /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.monitoringCount }}</p>
              <p class="stat-label">监控记录</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.reportCount }}</p>
              <p class="stat-label">数据报告</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>系统活动趋势</span>
          </template>
          <div id="activity-trend-chart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>各模块使用情况</span>
          </template>
          <div id="module-usage-chart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>数据概览表</span>
          </template>
          <el-table :data="overviewData" style="width: 100%">
            <el-table-column prop="module" label="模块" width="150" />
            <el-table-column prop="totalRecords" label="总记录数" width="120" />
            <el-table-column prop="activeRecords" label="活跃记录" width="120" />
            <el-table-column prop="growthRate" label="增长率" width="100">
              <template #default="{ row }">
                <el-tag :type="getGrowthRateTagType(row.growthRate)">
                  {{ row.growthRate > 0 ? '+' : '' }}{{ row.growthRate }}%
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="lastUpdate" label="最后更新" width="150" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { Refresh, User, SuitcaseLine, Location, TrendCharts, Monitor, Document } from '@element-plus/icons-vue'
import { getComprehensiveStatistics } from '@/api/dataAnalysis'

export default {
  name: 'ComprehensiveStatistics',
  components: {
    Refresh,
    User,
    SuitcaseLine,
    Location,
    TrendCharts,
    Monitor,
    Document
  },
  setup() {
    const dateRange = ref(['2025-01-01', '2025-12-31'])

    const stats = ref({
      userCount: 128,
      cropCount: 45,
      farmlandCount: 24,
      planCount: 89,
      monitoringCount: 156,
      reportCount: 23
    })

    const overviewData = ref([
      {
        module: '用户管理',
        totalRecords: 128,
        activeRecords: 115,
        growthRate: 12.5,
        lastUpdate: '2025-01-15 10:30'
      },
      {
        module: '作物管理',
        totalRecords: 45,
        activeRecords: 42,
        growthRate: 8.3,
        lastUpdate: '2025-01-14 15:20'
      },
      {
        module: '农田管理',
        totalRecords: 24,
        activeRecords: 22,
        growthRate: -2.1,
        lastUpdate: '2025-01-15 09:15'
      },
      {
        module: '种植计划',
        totalRecords: 89,
        activeRecords: 76,
        growthRate: 15.7,
        lastUpdate: '2025-01-15 11:45'
      },
      {
        module: '生长监控',
        totalRecords: 156,
        activeRecords: 142,
        growthRate: 22.4,
        lastUpdate: '2025-01-15 14:30'
      },
      {
        module: '数据报告',
        totalRecords: 23,
        activeRecords: 20,
        growthRate: 9.5,
        lastUpdate: '2025-01-15 08:20'
      }
    ])

    const refreshData = async () => {
      try {
        console.log('开始获取综合统计数据...')
        const response = await getComprehensiveStatistics()
        console.log('综合统计响应:', response)

        if (response.data && response.data.code === 200 && response.data.data) {
          const data = response.data.data

          // 更新统计数据
          if (data.overview) {
            stats.value.userCount = data.overview.userCount || 0
            stats.value.cropCount = data.overview.cropCount || 0
            stats.value.farmlandCount = data.overview.farmlandCount || 0
            stats.value.planCount = data.overview.planCount || 0
            stats.value.monitoringCount = data.overview.monitoringCount || 0
            stats.value.reportCount = data.overview.reportCount || 0
          }

          // 更新模块统计数据
          overviewData.value = data.moduleStatistics || []

          await nextTick()
          initActivityTrendChart(data.activityTrends)
          initModuleUsageChart(data.moduleUsage)
          console.log('成功获取综合统计数据')
        } else {
          console.log('综合统计响应格式不正确:', response.data)
          // 无数据时显示空状态
          await nextTick()
          initActivityTrendChart(null)
          initModuleUsageChart(null)
        }
      } catch (error) {
        console.error('获取综合统计数据失败:', error)
        ElMessage.error('获取综合统计数据失败')
        // 无数据时显示空状态
        await nextTick()
        initActivityTrendChart(null)
        initModuleUsageChart(null)
      }
    }

    const handleDateChange = () => {
      refreshData()
    }

    const initActivityTrendChart = (activityData) => {
      const chartDom = document.getElementById('activity-trend-chart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)

        let option = {
          title: {
            text: '系统活动趋势'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['新增记录', '活跃用户']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: ['第46周', '第47周', '第48周', '第49周', '第50周', '第51周']
          },
          yAxis: {
            type: 'value'
          },
          series: []
        }

        if (activityData && activityData.length > 0) {
          option.xAxis.data = activityData.map(item => item.period)
          option.series = [
            {
              name: '新增记录',
              type: 'bar',
              data: activityData.map(item => item.newRecords)
            },
            {
              name: '活跃用户',
              type: 'line',
              data: activityData.map(item => item.activeUsers)
            }
          ]
        } else {
          // 无数据时显示提示
          option.title.text = '系统活动趋势 - 暂无数据'
          option.legend = { data: [] }
          option.series = [{
            type: 'bar',
            data: [],
            itemStyle: { color: '#ccc' }
          }]
        }

        myChart.setOption(option)

        window.addEventListener('resize', () => {
          myChart.resize()
        })
      }
    }

    const initModuleUsageChart = (moduleData) => {
      const chartDom = document.getElementById('module-usage-chart')
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
              name: '模块使用',
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
              data: (moduleData && moduleData.length > 0) ? moduleData : [
                { value: 0, name: '暂无数据' }
              ]
            }
          ]
        }
        myChart.setOption(option)

        window.addEventListener('resize', () => {
          myChart.resize()
        })
      }
    }

    const getGrowthRateTagType = (rate) => {
      if (rate > 10) return 'success'
      if (rate > 0) return 'primary'
      if (rate > -5) return 'warning'
      return 'danger'
    }

    onMounted(() => {
      refreshData()
    })

    return {
      dateRange,
      stats,
      overviewData,
      refreshData,
      handleDateChange,
      getGrowthRateTagType
    }
  }
}
</script>

<style scoped>
.analysis-container {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 15px;
  align-items: center;
}

.stats-row {
  margin-bottom: 0;
}

.stat-card {
  text-align: center;
  height: 100px;
}

.stat-content {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.stat-icon {
  width: 40px;
  height: 40px;
  background-color: #409eff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  margin-right: 12px;
}

.stat-info {
  text-align: left;
  flex: 1;
}

.stat-number {
  font-size: 20px;
  font-weight: bold;
  margin: 0;
  color: #303133;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin: 4px 0 0 0;
}
</style>
