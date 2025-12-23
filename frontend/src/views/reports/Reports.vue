<template>
  <div class="reports-container">
    <div class="page-title">
      <h2>数据分析与报告</h2>
    </div>
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="card-header-left">
            <el-button type="primary" @click="handleAddReport">
              <el-icon><Plus /></el-icon>
              新增
            </el-button>
            <el-input 
              v-model="searchKeyword" 
              placeholder="搜索报告" 
              style="width: 200px; margin-left: 10px;" 
              clearable
            />
            <el-select 
              v-model="typeFilter" 
              placeholder="类型筛选" 
              style="margin-left: 10px; width: 150px;"
              clearable
            >
              <el-option label="作物生长分析" value="CROP_GROWTH" />
              <el-option label="农田利用率分析" value="FIELD_UTILIZATION" />
              <el-option label="产量分析" value="PRODUCTION_ANALYSIS" />
              <el-option label="天气影响分析" value="WEATHER_IMPACT" />
              <el-option label="预测分析" value="PREDICTIVE_ANALYSIS" />
            </el-select>
          </div>
          <div class="card-header-right">
            <el-button>
              <el-icon><Download /></el-icon>
              导出
            </el-button>
            <el-dropdown>
              <el-button>
                视图 <el-icon><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>列表视图</el-dropdown-item>
                  <el-dropdown-item>图表视图</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </template>
      
      <el-table 
        :data="reportList" 
        style="width: 100%" 
        v-loading="loading"
        row-key="id"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="报告标题" width="200" show-overflow-tooltip />
        <el-table-column prop="reportType" label="报告类型" width="150">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.reportType)">
              {{ getTypeText(row.reportType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        class="pagination"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </el-card>
    
    <!-- 报告编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form 
        :model="reportForm" 
        :rules="reportRules" 
        ref="reportFormRef"
        label-width="120px"
      >
        <el-form-item label="报告标题" prop="title">
          <el-input v-model="reportForm.title" placeholder="请输入报告标题" />
        </el-form-item>
        <el-form-item label="报告类型" prop="reportType">
          <el-select v-model="reportForm.reportType" placeholder="请选择报告类型" style="width: 100%">
            <el-option label="作物生长分析" value="CROP_GROWTH" />
            <el-option label="农田利用率分析" value="FIELD_UTILIZATION" />
            <el-option label="产量分析" value="PRODUCTION_ANALYSIS" />
            <el-option label="天气影响分析" value="WEATHER_IMPACT" />
            <el-option label="预测分析" value="PREDICTIVE_ANALYSIS" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker
            v-model="reportForm.startDate"
            type="date"
            placeholder="选择开始日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker
            v-model="reportForm.endDate"
            type="date"
            placeholder="选择结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="报告内容" prop="content">
          <el-input 
            v-model="reportForm.content" 
            type="textarea" 
            :rows="6"
            placeholder="请输入报告内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 报告查看对话框 -->
    <el-dialog 
      v-model="viewDialogVisible" 
      title="查看报告" 
      width="900px"
      :close-on-click-modal="false"
    >
      <div v-if="currentReport">
        <h3>{{ currentReport.title }}</h3>
        <p><strong>报告类型:</strong> {{ getTypeText(currentReport.reportType) }}</p>
        <p><strong>时间范围:</strong> {{ currentReport.startDate }} 至 {{ currentReport.endDate }}</p>
        <div class="report-content">
          <p><strong>报告内容:</strong></p>
          <div class="content-text">{{ currentReport.content }}</div>
        </div>
        <div class="report-chart" v-if="currentReport.chartData">
          <p><strong>图表数据:</strong></p>
          <div id="report-chart" style="height: 400px;"></div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowRight, ArrowDown, Plus, Download } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getReportList, createReport, updateReport, deleteReport } from '@/api/report'

export default {
  name: 'Reports',
  setup() {
    const reportList = ref([])
    const loading = ref(false)
    const dialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const dialogType = ref('add') // 'add' 或 'edit'
    const reportFormRef = ref(null)
    const currentReport = ref(null)
    const searchKeyword = ref('')
    const typeFilter = ref('')
    
    const reportForm = reactive({
      id: null,
      title: '',
      reportType: 'CROP_GROWTH',
      startDate: '',
      endDate: '',
      content: ''
    })
    
    const reportRules = {
      title: [
        { required: true, message: '请输入报告标题', trigger: 'blur' },
        { min: 2, max: 100, message: '报告标题长度在2-100个字符之间', trigger: 'blur' }
      ],
      reportType: [
        { required: true, message: '请选择报告类型', trigger: 'change' }
      ],
      startDate: [
        { required: true, message: '请选择开始日期', trigger: 'change' }
      ],
      endDate: [
        { required: true, message: '请选择结束日期', trigger: 'change' }
      ]
    }
    
    const pagination = reactive({
      currentPage: 1,
      pageSize: 10,
      total: 0
    })
    
    // 获取报告列表
    const fetchReportList = async () => {
      loading.value = true
      try {
        const response = await getReportList({
          page: pagination.currentPage,
          size: pagination.pageSize
        })
        reportList.value = response.data
      } catch (error) {
        console.error('获取报告列表失败:', error)
        ElMessage.error('获取报告列表失败')
      } finally {
        loading.value = false
      }
    }
    
    // 类型标签类型
    const getTypeTagType = (type) => {
      switch (type) {
        case 'CROP_GROWTH': return 'primary'
        case 'FIELD_UTILIZATION': return 'success'
        case 'PRODUCTION_ANALYSIS': return 'warning'
        case 'WEATHER_IMPACT': return 'danger'
        case 'PREDICTIVE_ANALYSIS': return 'info'
        default: return 'info'
      }
    }
    
    // 类型文本
    const getTypeText = (type) => {
      switch (type) {
        case 'CROP_GROWTH': return '作物生长分析'
        case 'FIELD_UTILIZATION': return '农田利用率分析'
        case 'PRODUCTION_ANALYSIS': return '产量分析'
        case 'WEATHER_IMPACT': return '天气影响分析'
        case 'PREDICTIVE_ANALYSIS': return '预测分析'
        default: return type
      }
    }
    
    // 添加报告
    const handleAddReport = () => {
      dialogType.value = 'add'
      Object.assign(reportForm, {
        id: null,
        title: '',
        reportType: 'CROP_GROWTH',
        startDate: '',
        endDate: '',
        content: ''
      })
      dialogVisible.value = true
    }
    
    // 编辑报告
    const handleEdit = (report) => {
      dialogType.value = 'edit'
      Object.assign(reportForm, { ...report })
      dialogVisible.value = true
    }
    
    // 查看报告
    const handleView = async (report) => {
      currentReport.value = report
      viewDialogVisible.value = true
      
      // 等待DOM更新后初始化图表
      await nextTick()
      initReportChart()
    }
    
    // 初始化报告图表
    const initReportChart = () => {
      const chartDom = document.getElementById('report-chart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)
        const option = {
          title: {
            text: '报告数据图表'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['数据1', '数据2', '数据3']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
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
              name: '数据1',
              type: 'line',
              data: [120, 132, 101, 134, 90, 230]
            },
            {
              name: '数据2',
              type: 'line',
              data: [220, 182, 191, 234, 290, 330]
            },
            {
              name: '数据3',
              type: 'line',
              data: [150, 232, 201, 154, 190, 330]
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
    
    // 删除报告
    const handleDelete = (id) => {
      ElMessageBox.confirm('此操作将永久删除该报告, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteReport(id)
          ElMessage.success('删除成功')
          fetchReportList() // 重新获取列表
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败: ' + (error.message || '网络错误'))
        }
      })
    }
    
    // 提交表单
    const submitForm = async () => {
      if (!reportFormRef.value) return
      
      await reportFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            if (dialogType.value === 'add') {
              await createReport(reportForm)
              ElMessage.success('报告生成成功')
            } else {
              await updateReport(reportForm.id, reportForm)
              ElMessage.success('报告更新成功')
            }
            dialogVisible.value = false
            fetchReportList() // 重新获取列表
          } catch (error) {
              console.error('保存失败:', error)
              ElMessage.error('保存失败: ' + (error.message || '网络错误'))
            }
        }
      })
    }
    
    // 分页处理
    const handleSizeChange = (size) => {
      pagination.pageSize = size
      fetchReportList()
    }
    
    const handleCurrentChange = (page) => {
      pagination.currentPage = page
      fetchReportList()
    }
    
    const dialogTitle = computed(() => {
      return dialogType.value === 'add' ? '生成报告' : '编辑报告'
    })
    
    onMounted(() => {
      fetchReportList()
    })
    
    return {
      reportList,
      loading,
      dialogVisible,
      viewDialogVisible,
      dialogType,
      reportForm,
      reportFormRef,
      reportRules,
      currentReport,
      pagination,
      getTypeTagType,
      getTypeText,
      handleAddReport,
      handleEdit,
      handleView,
      handleDelete,
      submitForm,
      handleSizeChange,
      handleCurrentChange,
      dialogTitle,
      searchKeyword,
      typeFilter,
      ArrowRight,
      ArrowDown,
      Plus,
      Download
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