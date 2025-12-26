<template>
  <div class="analysis-container">
    <div class="page-header">
      <h3>作物生长分析</h3>
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
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>作物生长趋势</span>
          </template>
          <div id="growth-trend-chart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>生长健康度分布</span>
          </template>
          <div id="health-status-chart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>生长环境参数</span>
          </template>
          <div id="environment-params-chart" style="height: 300px;"></div>
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
import { getCropGrowthAnalysis } from '@/api/dataAnalysis'

export default {
  name: 'CropGrowthAnalysis',
  components: {
    Refresh
  },
  setup() {
    const dateRange = ref(['2025-01-01', '2025-12-31'])

    const refreshData = async () => {
      try {
        console.log('开始获取作物生长分析数据...')
        const response = await getCropGrowthAnalysis()
        console.log('作物生长分析响应:', response)

        if (response.data && response.data.code === 200 && response.data.data) {
          const data = response.data.data
          await nextTick()
          initGrowthTrendChart(data.growthTrends)
          initHealthStatusChart(data.healthStatusDistribution)
          initEnvironmentParamsChart(data.environmentalData)
          console.log('成功获取作物生长分析数据')
        } else {
          console.log('作物生长分析响应格式不正确:', response.data)
          // 使用默认数据
          await nextTick()
          initGrowthTrendChart(null)
          initHealthStatusChart(null)
          initEnvironmentParamsChart(null)
        }
      } catch (error) {
        console.error('获取作物生长分析数据失败:', error)
        ElMessage.error('获取作物生长分析数据失败')
        // 使用默认数据
        await nextTick()
        initGrowthTrendChart(null)
        initHealthStatusChart(null)
        initEnvironmentParamsChart(null)
      }
    }

    const handleDateChange = () => {
      refreshData()
    }

    const initGrowthTrendChart = (growthData) => {
      const chartDom = document.getElementById('growth-trend-chart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)

        let option = {
          title: {
            text: '作物生长趋势分析'
          },
          tooltip: {
            trigger: 'axis'
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
            type: 'value',
            name: '生长高度(cm)'
          },
          series: []
        }

        if (growthData && growthData.length > 0) {
          // 从API数据中提取所有日期
          const allDates = new Set()
          growthData.forEach(crop => {
            crop.dataPoints.forEach(point => allDates.add(point.date))
          })
          option.xAxis.data = Array.from(allDates).sort()

          // 创建系列数据
          option.legend = { data: growthData.map(crop => crop.cropName) }
          option.series = growthData.map(crop => {
            const dataMap = new Map()
            crop.dataPoints.forEach(point => {
              dataMap.set(point.date, point.height)
            })

            return {
              name: crop.cropName,
              type: 'line',
              data: option.xAxis.data.map(date => dataMap.get(date) || null)
            }
          })
        } else {
          // 无数据时显示提示
          option.title.text = '作物生长趋势分析 - 暂无数据'
          option.legend = { data: [] }
          option.series = [{
            type: 'line',
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

    const initHealthStatusChart = (healthData) => {
      const chartDom = document.getElementById('health-status-chart')
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
              name: '健康状态',
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
              data: (healthData && healthData.length > 0) ? healthData : [
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

    const initEnvironmentParamsChart = (envData) => {
      const chartDom = document.getElementById('environment-params-chart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)

        let option = {
          title: {
            text: '环境参数变化'
          },
          tooltip: {
            trigger: 'axis'
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: ['1月', '2月', '3月', '4月', '5月', '6月']
          },
          yAxis: [
            {
              type: 'value',
              name: '温度(°C)/湿度(%)',
              position: 'left'
            },
            {
              type: 'value',
              name: '土壤湿度(%)',
              position: 'right'
            }
          ],
          series: []
        }

        if (envData && envData.length > 0) {
          option.xAxis.data = envData.map(item => item.date).sort()
          option.legend = { data: ['温度', '湿度', '土壤湿度'] }

          option.series = [
            {
              name: '温度',
              type: 'line',
              data: envData.map(item => item.temperature)
            },
            {
              name: '湿度',
              type: 'line',
              data: envData.map(item => item.humidity)
            },
            {
              name: '土壤湿度',
              type: 'bar',
              yAxisIndex: 1,
              data: envData.map(item => item.soilMoisture)
            }
          ]
        } else {
          // 无数据时显示提示
          option.title.text = '环境参数变化 - 暂无数据'
          option.legend = { data: [] }
          option.series = [{
            type: 'line',
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

    onMounted(() => {
      refreshData()
    })

    return {
      dateRange,
      refreshData,
      handleDateChange
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
