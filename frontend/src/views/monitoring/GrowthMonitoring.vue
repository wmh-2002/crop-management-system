<template>
  <div class="growth-monitoring-container">
    <div class="page-title">
      <h2>生长监控</h2>
    </div>
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="card-header-left">
            <el-button type="primary" @click="handleAddMonitoring">
              <el-icon><Plus /></el-icon>
              新增
            </el-button>
            <el-input 
              v-model="searchKeyword" 
              placeholder="搜索记录" 
              style="width: 200px; margin-left: 10px;" 
              clearable
            />
            <el-select 
              v-model="statusFilter" 
              placeholder="健康状态" 
              style="margin-left: 10px; width: 120px;"
              clearable
            >
              <el-option label="优秀" value="EXCELLENT" />
              <el-option label="良好" value="GOOD" />
              <el-option label="一般" value="FAIR" />
              <el-option label="较差" value="POOR" />
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
        :data="monitoringList" 
        style="width: 100%" 
        v-loading="loading"
        row-key="id"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="plan.planName" label="种植计划" width="150" />
        <el-table-column prop="monitoringDate" label="监测日期" width="120" />
        <el-table-column prop="heightMeasurement" label="高度(cm)" width="100">
          <template #default="{ row }">
            {{ row.heightMeasurement }} cm
          </template>
        </el-table-column>
        <el-table-column prop="widthMeasurement" label="宽度(cm)" width="100">
          <template #default="{ row }">
            {{ row.widthMeasurement }} cm
          </template>
        </el-table-column>
        <el-table-column prop="healthStatus" label="健康状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.healthStatus)">
              {{ getStatusText(row.healthStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="soilMoisture" label="土壤湿度(%)" width="120">
          <template #default="{ row }">
            {{ row.soilMoisture }}%
          </template>
        </el-table-column>
        <el-table-column prop="temperature" label="温度(°C)" width="120">
          <template #default="{ row }">
            {{ row.temperature }}°C
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
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
    
    <!-- 生长监控编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form 
        :model="monitoringForm" 
        :rules="monitoringRules" 
        ref="monitoringFormRef"
        label-width="120px"
      >
        <el-form-item label="种植计划" prop="planId">
          <el-select v-model="monitoringForm.planId" placeholder="请选择种植计划" style="width: 100%">
            <el-option 
              v-for="plan in planList" 
              :key="plan.id" 
              :label="plan.planName" 
              :value="plan.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="监测日期" prop="monitoringDate">
          <el-date-picker
            v-model="monitoringForm.monitoringDate"
            type="date"
            placeholder="选择监测日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="高度测量(cm)" prop="heightMeasurement">
          <el-input v-model.number="monitoringForm.heightMeasurement" type="number" placeholder="请输入高度测量值" />
        </el-form-item>
        <el-form-item label="宽度测量(cm)" prop="widthMeasurement">
          <el-input v-model.number="monitoringForm.widthMeasurement" type="number" placeholder="请输入宽度测量值" />
        </el-form-item>
        <el-form-item label="健康状态" prop="healthStatus">
          <el-select v-model="monitoringForm.healthStatus" placeholder="请选择健康状态" style="width: 100%">
            <el-option label="优秀" value="EXCELLENT" />
            <el-option label="良好" value="GOOD" />
            <el-option label="一般" value="FAIR" />
            <el-option label="较差" value="POOR" />
          </el-select>
        </el-form-item>
        <el-form-item label="土壤湿度(%)" prop="soilMoisture">
          <el-input v-model.number="monitoringForm.soilMoisture" type="number" placeholder="请输入土壤湿度" />
        </el-form-item>
        <el-form-item label="温度(°C)" prop="temperature">
          <el-input v-model.number="monitoringForm.temperature" type="number" placeholder="请输入温度" />
        </el-form-item>
        <el-form-item label="湿度(%)" prop="humidity">
          <el-input v-model.number="monitoringForm.humidity" type="number" placeholder="请输入湿度" />
        </el-form-item>
        <el-form-item label="光照强度(lux)" prop="lightIntensity">
          <el-input v-model.number="monitoringForm.lightIntensity" type="number" placeholder="请输入光照强度" />
        </el-form-item>
        <el-form-item label="pH值" prop="phLevel">
          <el-input v-model.number="monitoringForm.phLevel" type="number" placeholder="请输入pH值" step="0.01" />
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input 
            v-model="monitoringForm.notes" 
            type="textarea" 
            :rows="3"
            placeholder="请输入备注信息"
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
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowRight, ArrowDown, Plus, Download } from '@element-plus/icons-vue'
import { 
  getGrowthMonitoringList, 
  createGrowthMonitoring, 
  updateGrowthMonitoring, 
  deleteGrowthMonitoring 
} from '@/api/growthMonitoring'
import { getPlantingPlanList } from '@/api/plantingPlan'

export default {
  name: 'GrowthMonitoring',
  setup() {
    const monitoringList = ref([])
    const planList = ref([])
    const loading = ref(false)
    const dialogVisible = ref(false)
    const dialogType = ref('add') // 'add' 或 'edit'
    const monitoringFormRef = ref(null)
    const searchKeyword = ref('')
    const statusFilter = ref('')
    
    const monitoringForm = reactive({
      id: null,
      planId: null,
      monitoringDate: '',
      heightMeasurement: null,
      widthMeasurement: null,
      healthStatus: 'GOOD',
      soilMoisture: null,
      temperature: null,
      humidity: null,
      lightIntensity: null,
      phLevel: null,
      notes: ''
    })
    
    const monitoringRules = {
      planId: [
        { required: true, message: '请选择种植计划', trigger: 'change' }
      ],
      monitoringDate: [
        { required: true, message: '请选择监测日期', trigger: 'change' }
      ],
      heightMeasurement: [
        { required: true, message: '请输入高度测量值', trigger: 'blur' },
        { type: 'number', message: '高度测量值必须为数字', trigger: 'blur' }
      ],
      soilMoisture: [
        { required: true, message: '请输入土壤湿度', trigger: 'blur' },
        { type: 'number', message: '土壤湿度必须为数字', trigger: 'blur' }
      ]
    }
    
    const pagination = reactive({
      currentPage: 1,
      pageSize: 10,
      total: 0
    })
    
    // 获取生长监控列表
    const fetchMonitoringList = async () => {
      loading.value = true
      try {
        const response = await getGrowthMonitoringList({
          page: pagination.currentPage,
          size: pagination.pageSize
        })
        monitoringList.value = response.data
      } catch (error) {
        console.error('获取生长监控列表失败:', error)
        ElMessage.error('获取生长监控列表失败')
      } finally {
        loading.value = false
      }
    }
    
    // 获取种植计划列表
    const fetchPlanList = async () => {
      try {
        const response = await getPlantingPlanList()
        planList.value = response.data
      } catch (error) {
        console.error('获取种植计划列表失败:', error)
      }
    }
    
    // 状态标签类型
    const getStatusTagType = (status) => {
      switch (status) {
        case 'EXCELLENT': return 'success'
        case 'GOOD': return 'primary'
        case 'FAIR': return 'warning'
        case 'POOR': return 'danger'
        default: return 'info'
      }
    }
    
    // 状态文本
    const getStatusText = (status) => {
      switch (status) {
        case 'EXCELLENT': return '优秀'
        case 'GOOD': return '良好'
        case 'FAIR': return '一般'
        case 'POOR': return '较差'
        default: return status
      }
    }
    
    // 添加生长监控
    const handleAddMonitoring = () => {
      dialogType.value = 'add'
      Object.assign(monitoringForm, {
        id: null,
        planId: null,
        monitoringDate: '',
        heightMeasurement: null,
        widthMeasurement: null,
        healthStatus: 'GOOD',
        soilMoisture: null,
        temperature: null,
        humidity: null,
        lightIntensity: null,
        phLevel: null,
        notes: ''
      })
      dialogVisible.value = true
    }
    
    // 编辑生长监控
    const handleEdit = (monitoring) => {
      dialogType.value = 'edit'
      Object.assign(monitoringForm, { 
        ...monitoring,
        planId: monitoring.plan.id
      })
      dialogVisible.value = true
    }
    
    // 删除生长监控
    const handleDelete = (id) => {
      ElMessageBox.confirm('此操作将永久删除该生长监控记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteGrowthMonitoring(id)
          ElMessage.success('删除成功')
          fetchMonitoringList() // 重新获取列表
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败: ' + (error.message || '网络错误'))
        }
      })
    }
    
    // 提交表单
    const submitForm = async () => {
      if (!monitoringFormRef.value) return
      
      await monitoringFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            // 创建或更新数据对象
            const formData = {
              ...monitoringForm,
              plan: { id: monitoringForm.planId }
            }
            
            if (dialogType.value === 'add') {
              await createGrowthMonitoring(formData)
              ElMessage.success('生长监控记录添加成功')
            } else {
              await updateGrowthMonitoring(monitoringForm.id, formData)
              ElMessage.success('生长监控记录更新成功')
            }
            dialogVisible.value = false
            fetchMonitoringList() // 重新获取列表
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
      fetchMonitoringList()
    }
    
    const handleCurrentChange = (page) => {
      pagination.currentPage = page
      fetchMonitoringList()
    }
    
    const dialogTitle = computed(() => {
      return dialogType.value === 'add' ? '添加生长监控' : '编辑生长监控'
    })
    
    onMounted(() => {
      fetchMonitoringList()
      fetchPlanList()
    })
    
    return {
      monitoringList,
      planList,
      loading,
      dialogVisible,
      dialogType,
      monitoringForm,
      monitoringFormRef,
      monitoringRules,
      pagination,
      getStatusTagType,
      getStatusText,
      handleAddMonitoring,
      handleEdit,
      handleDelete,
      submitForm,
      handleSizeChange,
      handleCurrentChange,
      dialogTitle,
      searchKeyword,
      statusFilter,
      ArrowRight,
      ArrowDown,
      Plus,
      Download
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

.pagination {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>