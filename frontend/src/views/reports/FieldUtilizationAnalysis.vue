<template>
  <div class="analysis-container">
    <div class="page-header">
      <h3>农田利用率分析</h3>
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

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>农田状态分布</span>
          </template>
          <div id="field-status-chart" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>农田利用率趋势</span>
          </template>
          <div id="utilization-trend-chart" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>农田使用详情</span>
          </template>
          <el-table :data="fieldData" style="width: 100%">
            <el-table-column prop="name" label="农田名称" width="150" />
            <el-table-column prop="area" label="面积(㎡)" width="120" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusTagType(row.status)">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="cropName" label="种植作物" width="120" />
            <el-table-column prop="utilizationRate" label="利用率" width="100">
              <template #default="{ row }">
                <el-progress :percentage="row.utilizationRate" :stroke-width="8" />
              </template>
            </el-table-column>
            <el-table-column prop="lastActivity" label="最后活动" width="150" />
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
import { Refresh } from '@element-plus/icons-vue'
import { getFieldUtilizationAnalysis } from '@/api/dataAnalysis'

export default {
  name: 'FieldUtilizationAnalysis',
  components: {
    Refresh
  },
  setup() {
    const dateRange = ref(['2025-01-01', '2025-12-31'])

    const fieldData = ref([
      {
        name: '农田A-001',
        area: 5000,
        status: 'PLANTED',
        cropName: '小麦',
        utilizationRate: 95,
        lastActivity: '2025-01-15 浇水'
      },
      {
        name: '农田A-002',
        area: 4500,
        status: 'AVAILABLE',
        cropName: '待种植',
        utilizationRate: 0,
        lastActivity: '2025-01-10 翻耕'
      },
      {
        name: '农田B-001',
        area: 6000,
        status: 'PLANTED',
        cropName: '玉米',
        utilizationRate: 88,
        lastActivity: '2025-01-14 施肥'
      },
      {
        name: '农田B-002',
        area: 5500,
        status: 'MAINTENANCE',
        cropName: '维修中',
        utilizationRate: 0,
        lastActivity: '2025-01-12 维修'
      }
    ])

    const refreshData = async () => {
      try {
        console.log('开始获取农田利用率分析数据...')
        const response = await getFieldUtilizationAnalysis()
        console.log('农田利用率分析响应:', response)

        if (response.data && response.data.code === 200 && response.data.data) {
          const data = response.data.data
          await nextTick()
          initFieldStatusChart(data.fieldStatusDistribution)
          initUtilizationTrendChart(data.utilizationTrends)
          // 更新表格数据
          fieldData.value = data.fieldUsageDetails || []
          console.log('成功获取农田利用率分析数据')
        } else {
          console.log('农田利用率分析响应格式不正确:', response.data)
          // 无数据时显示空状态
          await nextTick()
          initFieldStatusChart(null)
          initUtilizationTrendChart(null)
          fieldData.value = []
        }
      } catch (error) {
        console.error('获取农田利用率分析数据失败:', error)
        ElMessage.error('获取农田利用率分析数据失败')
        // 使用默认数据
        await nextTick()
        initFieldStatusChart(null)
        initUtilizationTrendChart(null)
        fieldData.value = [
          {
            fieldName: '农田A-001',
            area: 5000,
            status: 'PLANTED',
            cropName: '小麦',
            utilizationRate: 95,
            lastActivity: '2025-01-15 浇水'
          }
        ]
      }
    }

    const handleDateChange = () => {
      refreshData()
    }

    const initFieldStatusChart = (statusData) => {
      const chartDom = document.getElementById('field-status-chart')
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
              data: (statusData && statusData.length > 0) ? statusData.map(item => ({
                value: item.value,
                name: item.name
              })) : [
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

    const initUtilizationTrendChart = (trendData) => {
      const chartDom = document.getElementById('utilization-trend-chart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)
        const option = {
          title: {
            text: '农田利用率趋势'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['利用率']
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
            data: trendData && trendData.length > 0 ? trendData.map(item => item.month) : ['暂无数据']
          },
          yAxis: {
            type: 'value',
            name: '利用率(%)',
            max: 100
          },
          series: [
            {
              name: '利用率',
              type: 'line',
              areaStyle: {},
              data: trendData && trendData.length > 0 ? trendData.map(item => item.utilizationRate) : [0]
            }
          ]
        }
        myChart.setOption(option)

        window.addEventListener('resize', () => {
          myChart.resize()
        })
      }
    }

    const getStatusTagType = (status) => {
      switch (status) {
        case 'AVAILABLE': return 'success'
        case 'PLANTED': return 'primary'
        case 'MAINTENANCE': return 'warning'
        default: return 'info'
      }
    }

    const getStatusText = (status) => {
      switch (status) {
        case 'AVAILABLE': return '可用'
        case 'PLANTED': return '已种植'
        case 'MAINTENANCE': return '维护中'
        default: return status
      }
    }

    onMounted(() => {
      refreshData()
    })

    return {
      dateRange,
      fieldData,
      refreshData,
      handleDateChange,
      getStatusTagType,
      getStatusText
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
</style>
