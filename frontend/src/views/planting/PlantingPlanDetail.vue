<template>
  <div class="planting-plan-detail-container">
    <div class="page-header">
      <el-page-header :content="`种植计划详情 - ${planDetail.planName || '加载中...'}`" @back="goBack" />
      <div class="header-actions">
        <el-button type="primary" @click="handleEdit">编辑</el-button>
        <el-button @click="handleDelete">删除</el-button>
      </div>
    </div>

    <el-card class="detail-card">
      <template #header>
        <div class="card-header">
          <span>基本信息</span>
        </div>
      </template>
      <div class="detail-content">
        <el-descriptions :column="2" border v-loading="detailLoading">
          <el-descriptions-item label="计划ID">{{ planDetail.id }}</el-descriptions-item>
          <el-descriptions-item label="计划名称">{{ planDetail.planName }}</el-descriptions-item>
          <el-descriptions-item label="农田名称">
            <span v-if="planDetail.farmland">{{ planDetail.farmland.name }}</span>
            <span v-else>{{ planDetail.farmlandName || '未指定' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="作物名称">
            <span v-if="planDetail.crop">{{ planDetail.crop.name }}</span>
            <span v-else>{{ planDetail.cropName || '未指定' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="计划开始日期">{{ planDetail.plannedStartDate }}</el-descriptions-item>
          <el-descriptions-item label="计划结束日期">{{ planDetail.plannedEndDate }}</el-descriptions-item>
          <el-descriptions-item label="预期收获日期">{{ planDetail.expectedHarvestDate || '未设置' }}</el-descriptions-item>
          <el-descriptions-item label="播种密度">{{ planDetail.sowingDensity || '未设置' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTagType(planDetail.status)">
              {{ getStatusText(planDetail.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ planDetail.createdAt }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">
            {{ planDetail.notes || '无备注' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>

    <!-- 种植记录 -->
    <el-card class="detail-card">
      <template #header>
        <div class="card-header">
          <span>种植记录</span>
          <el-button type="primary" size="small" @click="showAddRecordDialog">添加记录</el-button>
        </div>
      </template>
      <el-table :data="plantingRecords" style="width: 100%" v-loading="recordsLoading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="operationType" label="操作类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getOperationTypeTagType(row.operationType)">
              {{ getOperationTypeText(row.operationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationDate" label="操作日期" width="120" />
        <el-table-column prop="quantityUsed" label="用量" width="100">
          <template #default="{ row }">
            {{ row.quantityUsed || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="weatherConditions" label="天气条件" width="150" />
        <el-table-column prop="notes" label="备注" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="showEditRecordDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteRecord(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 生长监控 -->
    <el-card class="detail-card">
      <template #header>
        <div class="card-header">
          <span>生长监控</span>
          <div>
            <el-button type="primary" size="small" @click="showAddMonitoringDialog">手动录入</el-button>
            <el-button type="success" size="small" @click="collectMonitoringData">自动采集</el-button>
          </div>
        </div>
      </template>
      <el-table :data="growthMonitoring" style="width: 100%" v-loading="monitoringLoading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="monitoringDate" label="监测日期" width="120" />
        <el-table-column prop="heightMeasurement" label="高度(cm)" width="100" />
        <el-table-column prop="widthMeasurement" label="宽度(cm)" width="100" />
        <el-table-column prop="healthStatus" label="健康状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getHealthStatusTagType(row.healthStatus)">
              {{ getHealthStatusText(row.healthStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="temperature" label="温度(°C)" width="100" />
        <el-table-column prop="soilMoisture" label="土壤湿度(%)" width="120" />
        <el-table-column prop="humidity" label="湿度(%)" width="100" />
        <el-table-column prop="lightIntensity" label="光照强度(lux)" width="120" />
        <el-table-column prop="phLevel" label="pH值" width="100" />
        <el-table-column prop="notes" label="备注" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="showEditMonitoringDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteMonitoringRecord(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加种植记录对话框 -->
    <el-dialog
      v-model="addRecordDialogVisible"
      title="添加种植记录"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        :model="recordForm"
        :rules="recordFormRules"
        ref="recordFormRef"
        label-width="100px"
      >
        <el-form-item label="操作类型" prop="operationType">
          <el-select v-model="recordForm.operationType" placeholder="请选择操作类型" style="width: 100%">
            <el-option label="播种" value="SOWING" />
            <el-option label="施肥" value="FERTILIZING" />
            <el-option label="灌溉" value="WATERING" />
            <el-option label="施药" value="PESTICIDING" />
            <el-option label="收获" value="HARVESTING" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作日期" prop="operationDate">
          <el-date-picker
            v-model="recordForm.operationDate"
            type="date"
            placeholder="选择操作日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="用量" prop="quantityUsed">
          <el-input v-model="recordForm.quantityUsed" placeholder="请输入用量" />
        </el-form-item>
        <el-form-item label="天气条件" prop="weatherConditions">
          <el-input v-model="recordForm.weatherConditions" placeholder="请输入天气条件" />
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input
            v-model="recordForm.notes"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addRecordDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitRecordForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑种植记录对话框 -->
    <el-dialog
      v-model="editRecordDialogVisible"
      title="编辑种植记录"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        :model="editRecordForm"
        :rules="recordFormRules"
        ref="recordFormRef"
        label-width="100px"
      >
        <el-form-item label="操作类型" prop="operationType">
          <el-select v-model="editRecordForm.operationType" placeholder="请选择操作类型" style="width: 100%">
            <el-option label="播种" value="SOWING" />
            <el-option label="施肥" value="FERTILIZING" />
            <el-option label="灌溉" value="WATERING" />
            <el-option label="施药" value="PESTICIDING" />
            <el-option label="收获" value="HARVESTING" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作日期" prop="operationDate">
          <el-date-picker
            v-model="editRecordForm.operationDate"
            type="date"
            placeholder="选择操作日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="用量" prop="quantityUsed">
          <el-input v-model="editRecordForm.quantityUsed" placeholder="请输入用量" />
        </el-form-item>
        <el-form-item label="天气条件" prop="weatherConditions">
          <el-input v-model="editRecordForm.weatherConditions" placeholder="请输入天气条件" />
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input
            v-model="editRecordForm.notes"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editRecordDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEditRecordForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加生长监控对话框 -->
    <el-dialog
      v-model="addMonitoringDialogVisible"
      title="手动录入生长监控"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form
        :model="monitoringForm"
        :rules="monitoringFormRules"
        ref="monitoringFormRef"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
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
          </el-col>
          <el-col :span="12">
            <el-form-item label="健康状态" prop="healthStatus">
              <el-select v-model="monitoringForm.healthStatus" placeholder="请选择健康状态" style="width: 100%">
                <el-option label="优秀" value="EXCELLENT" />
                <el-option label="良好" value="GOOD" />
                <el-option label="一般" value="FAIR" />
                <el-option label="较差" value="POOR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="高度(cm)" prop="heightMeasurement">
              <el-input v-model="monitoringForm.heightMeasurement" placeholder="请输入高度" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宽度(cm)" prop="widthMeasurement">
              <el-input v-model="monitoringForm.widthMeasurement" placeholder="请输入宽度" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="温度(°C)" prop="temperature">
              <el-input v-model="monitoringForm.temperature" placeholder="请输入温度" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="土壤湿度(%)" prop="soilMoisture">
              <el-input v-model="monitoringForm.soilMoisture" placeholder="请输入土壤湿度" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="湿度(%)" prop="humidity">
              <el-input v-model="monitoringForm.humidity" placeholder="请输入湿度" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="光照强度(lux)" prop="lightIntensity">
              <el-input v-model="monitoringForm.lightIntensity" placeholder="请输入光照强度" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="pH值" prop="phLevel">
              <el-input v-model="monitoringForm.phLevel" placeholder="请输入pH值" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="温度(°C)" prop="temperature">
              <el-input v-model="monitoringForm.temperature" placeholder="请输入温度" />
            </el-form-item>
          </el-col>
        </el-row>
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
          <el-button @click="addMonitoringDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitMonitoringForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑生长监控对话框 -->
    <el-dialog
      v-model="editMonitoringDialogVisible"
      title="编辑生长监控"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form
        :model="monitoringForm"
        :rules="monitoringFormRules"
        ref="monitoringFormRef"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
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
          </el-col>
          <el-col :span="12">
            <el-form-item label="健康状态" prop="healthStatus">
              <el-select v-model="monitoringForm.healthStatus" placeholder="请选择健康状态" style="width: 100%">
                <el-option label="优秀" value="EXCELLENT" />
                <el-option label="良好" value="GOOD" />
                <el-option label="一般" value="FAIR" />
                <el-option label="较差" value="POOR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="高度(cm)" prop="heightMeasurement">
              <el-input v-model="monitoringForm.heightMeasurement" placeholder="请输入高度" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宽度(cm)" prop="widthMeasurement">
              <el-input v-model="monitoringForm.widthMeasurement" placeholder="请输入宽度" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="温度(°C)" prop="temperature">
              <el-input v-model="monitoringForm.temperature" placeholder="请输入温度" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="土壤湿度(%)" prop="soilMoisture">
              <el-input v-model="monitoringForm.soilMoisture" placeholder="请输入土壤湿度" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="湿度(%)" prop="humidity">
              <el-input v-model="monitoringForm.humidity" placeholder="请输入湿度" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="光照强度(lux)" prop="lightIntensity">
              <el-input v-model="monitoringForm.lightIntensity" placeholder="请输入光照强度" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="pH值" prop="phLevel">
              <el-input v-model="monitoringForm.phLevel" placeholder="请输入pH值" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="温度(°C)" prop="temperature">
              <el-input v-model="monitoringForm.temperature" placeholder="请输入温度" />
            </el-form-item>
          </el-col>
        </el-row>
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
          <el-button @click="editMonitoringDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEditMonitoringForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPlantingPlanById, deletePlantingPlan } from '@/api/plantingPlan'
import { getPlantingRecordsByPlan, createPlantingRecord, updatePlantingRecord, deletePlantingRecord } from '@/api/plantingRecord'
import { getGrowthMonitoringByPlan, collectGrowthMonitoring, createGrowthMonitoring, updateGrowthMonitoring, deleteGrowthMonitoring } from '@/api/growthMonitoring'

export default {
  name: 'PlantingPlanDetail',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const planDetail = ref({})
    const plantingRecords = ref([])
    const growthMonitoring = ref([])
    const recordsLoading = ref(false)
    const monitoringLoading = ref(false)
    const detailLoading = ref(false)
    const planId = ref(null)
    const addRecordDialogVisible = ref(false)
    const recordFormRef = ref(null)

    const recordForm = ref({
      operationType: '',
      operationDate: '',
      quantityUsed: '',
      weatherConditions: '',
      notes: ''
    })

    const editRecordDialogVisible = ref(false)
    const editRecordForm = ref({
      id: null,
      operationType: '',
      operationDate: '',
      quantityUsed: '',
      weatherConditions: '',
      notes: ''
    })

    // 从路由参数获取计划ID
    planId.value = parseInt(route.params.id)

    // 获取种植计划详情
    const fetchPlanDetail = async () => {
      detailLoading.value = true
      try {
        const response = await getPlantingPlanById(planId.value)
        if (response.data && response.data.code === 200) {
          planDetail.value = response.data.data || {}
        } else {
          ElMessage.error('获取种植计划详情失败')
        }
      } catch (error) {
        console.error('获取种植计划详情失败:', error)
        ElMessage.error('获取种植计划详情失败')
      } finally {
        detailLoading.value = false
      }
    }

    // 获取种植记录
    const fetchPlantingRecords = async () => {
      recordsLoading.value = true
      try {
        const response = await getPlantingRecordsByPlan(planId.value)
        if (response.data && response.data.code === 200) {
          plantingRecords.value = response.data.data || []
        } else {
          plantingRecords.value = []
        }
      } catch (error) {
        console.error('获取种植记录失败:', error)
        plantingRecords.value = []
      } finally {
        recordsLoading.value = false
      }
    }

    // 获取生长监控记录
    const fetchGrowthMonitoring = async () => {
      monitoringLoading.value = true
      try {
        const response = await getGrowthMonitoringByPlan(planId.value)
        if (response.data && response.data.code === 200) {
          growthMonitoring.value = response.data.data || []
        } else {
          growthMonitoring.value = []
        }
      } catch (error) {
        console.error('获取生长监控记录失败:', error)
        growthMonitoring.value = []
      } finally {
        monitoringLoading.value = false
      }
    }

    // 状态标签类型
    const getStatusTagType = (status) => {
      switch (status) {
        case 'PLANNED': return 'info'
        case 'IN_PROGRESS': return 'primary'
        case 'COMPLETED': return 'success'
        case 'CANCELLED': return 'danger'
        default: return 'info'
      }
    }

    // 状态文本
    const getStatusText = (status) => {
      switch (status) {
        case 'PLANNED': return '计划中'
        case 'IN_PROGRESS': return '进行中'
        case 'COMPLETED': return '已完成'
        case 'CANCELLED': return '已取消'
        default: return status
      }
    }

    // 操作类型标签类型
    const getOperationTypeTagType = (type) => {
      switch (type) {
        case 'SOWING': return 'primary'
        case 'FERTILIZING': return 'warning'
        case 'WATERING': return 'info'
        case 'PESTICIDING': return 'danger'
        case 'HARVESTING': return 'success'
        default: return 'info'
      }
    }

    // 操作类型文本
    const getOperationTypeText = (type) => {
      switch (type) {
        case 'SOWING': return '播种'
        case 'FERTILIZING': return '施肥'
        case 'WATERING': return '灌溉'
        case 'PESTICIDING': return '施药'
        case 'HARVESTING': return '收获'
        default: return type
      }
    }

    // 健康状态标签类型
    const getHealthStatusTagType = (status) => {
      switch (status) {
        case 'EXCELLENT': return 'success'
        case 'GOOD': return 'primary'
        case 'FAIR': return 'warning'
        case 'POOR': return 'danger'
        default: return 'info'
      }
    }

    // 健康状态文本
    const getHealthStatusText = (status) => {
      switch (status) {
        case 'EXCELLENT': return '优秀'
        case 'GOOD': return '良好'
        case 'FAIR': return '一般'
        case 'POOR': return '较差'
        default: return status
      }
    }

    // 返回列表页面
    const goBack = () => {
      router.push('/planting-plans')
    }

    // 编辑种植计划
    const handleEdit = () => {
      router.push(`/planting-plans/edit/${planId.value}`)
    }

    // 删除种植计划
    const handleDelete = async () => {
      try {
        await ElMessageBox.confirm('此操作将永久删除该种植计划, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 调用删除API
        await deletePlantingPlan(planId.value)
        ElMessage.success('种植计划已删除')
        router.push('/planting-plans')
      } catch (error) {
        console.log('取消删除')
      }
    }

    // 显示添加记录对话框
    const showAddRecordDialog = () => {
      // 重置表单
      Object.assign(recordForm.value, {
        operationType: '',
        operationDate: '',
        quantityUsed: '',
        weatherConditions: '',
        notes: ''
      })
      addRecordDialogVisible.value = true
    }

    // 种植记录表单验证规则
    const recordFormRules = {
      operationType: [
        { required: true, message: '请选择操作类型', trigger: 'change' }
      ],
      operationDate: [
        { required: true, message: '请选择操作日期', trigger: 'change' }
      ]
    }

    // 显示编辑记录对话框
    const showEditRecordDialog = (record) => {
      // 将记录数据复制到编辑表单
      Object.assign(editRecordForm.value, {
        id: record.id,
        operationType: record.operationType,
        operationDate: record.operationDate,
        quantityUsed: record.quantityUsed,
        weatherConditions: record.weatherConditions,
        notes: record.notes
      })
      editRecordDialogVisible.value = true
    }

    // 删除记录
    const deleteRecord = async (id) => {
      try {
        await ElMessageBox.confirm('此操作将永久删除该种植记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 调用删除API
        await deletePlantingRecord(id)
        ElMessage.success('种植记录已删除')
        // 重新获取种植记录列表
        fetchPlantingRecords()
      } catch (error) {
        console.log('取消删除')
      }
    }

    // 提交种植记录表单
    const submitRecordForm = async () => {
      if (!recordFormRef.value) return

      await recordFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            // 添加planId到提交数据中
            const submitData = {
              planId: planId.value,
              operationType: recordForm.value.operationType,
              operationDate: recordForm.value.operationDate,
              quantityUsed: recordForm.value.quantityUsed ? parseFloat(recordForm.value.quantityUsed) : null,
              weatherConditions: recordForm.value.weatherConditions,
              notes: recordForm.value.notes
            }

            await createPlantingRecord(submitData)
            ElMessage.success('种植记录添加成功')
            addRecordDialogVisible.value = false
            // 重新获取种植记录列表
            fetchPlantingRecords()
          } catch (error) {
            console.error('添加种植记录失败:', error)
            ElMessage.error('添加种植记录失败: ' + (error.message || '网络错误'))
          }
        }
      })
    }

    // 提交编辑记录表单
    const submitEditRecordForm = async () => {
      if (!recordFormRef.value) return

      await recordFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            // 更新记录数据
            const updateData = {
              operationType: editRecordForm.value.operationType,
              operationDate: editRecordForm.value.operationDate,
              quantityUsed: editRecordForm.value.quantityUsed ? parseFloat(editRecordForm.value.quantityUsed) : null,
              weatherConditions: editRecordForm.value.weatherConditions,
              notes: editRecordForm.value.notes
            }

            await updatePlantingRecord(editRecordForm.value.id, updateData)
            ElMessage.success('种植记录更新成功')
            editRecordDialogVisible.value = false
            // 重新获取种植记录列表
            fetchPlantingRecords()
          } catch (error) {
            console.error('更新种植记录失败:', error)
            ElMessage.error('更新种植记录失败: ' + (error.message || '网络错误'))
          }
        }
      })
    }

    // 生长监控表单
    const monitoringForm = ref({
      monitoringDate: '',
      heightMeasurement: '',
      widthMeasurement: '',
      healthStatus: 'GOOD',
      temperature: '',
      soilMoisture: '',
      humidity: '',
      lightIntensity: '',
      phLevel: '',
      notes: ''
    })

    const addMonitoringDialogVisible = ref(false)
    const monitoringFormRef = ref(null)

    // 生长监控表单验证规则
    const monitoringFormRules = {
      monitoringDate: [
        { required: true, message: '请选择监测日期', trigger: 'change' }
      ],
      heightMeasurement: [
        { required: true, message: '请输入高度', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请输入高度'))
            } else if (isNaN(value) || parseFloat(value) <= 0) {
              callback(new Error('高度必须为正数'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ],
      widthMeasurement: [
        { required: true, message: '请输入宽度', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请输入宽度'))
            } else if (isNaN(value) || parseFloat(value) <= 0) {
              callback(new Error('宽度必须为正数'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ],
      healthStatus: [
        { required: true, message: '请选择健康状态', trigger: 'change' }
      ],
      temperature: [
        { required: true, message: '请输入温度', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请输入温度'))
            } else if (isNaN(value) || parseFloat(value) < -50 || parseFloat(value) > 60) {
              callback(new Error('温度应在-50到60度之间'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ],
      soilMoisture: [
        { required: true, message: '请输入土壤湿度', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请输入土壤湿度'))
            } else if (isNaN(value) || parseFloat(value) < 0 || parseFloat(value) > 100) {
              callback(new Error('土壤湿度应在0-100%之间'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ],
      humidity: [
        { required: true, message: '请输入湿度', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请输入湿度'))
            } else if (isNaN(value) || parseFloat(value) < 0 || parseFloat(value) > 100) {
              callback(new Error('湿度应在0-100%之间'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ]
    }

    const editMonitoringDialogVisible = ref(false)


    // 显示添加监控对话框
    const showAddMonitoringDialog = () => {
      // 重置表单
      Object.assign(monitoringForm.value, {
        id: null,
        monitoringDate: '',
        heightMeasurement: '',
        widthMeasurement: '',
        healthStatus: 'GOOD',
        temperature: '',
        soilMoisture: '',
        lightIntensity: '',
        phLevel: '',
        notes: ''
      })
      addMonitoringDialogVisible.value = true
    }

    // 显示编辑监控对话框
    const showEditMonitoringDialog = (record) => {
      // 将记录数据复制到表单
      Object.assign(monitoringForm.value, {
        id: record.id,
        monitoringDate: record.monitoringDate,
        heightMeasurement: record.heightMeasurement,
        widthMeasurement: record.widthMeasurement,
        healthStatus: record.healthStatus,
        temperature: record.temperature,
        soilMoisture: record.soilMoisture,
        lightIntensity: record.lightIntensity,
        phLevel: record.phLevel,
        notes: record.notes
      })
      editMonitoringDialogVisible.value = true
    }

    // 提交监控表单
    const submitMonitoringForm = async () => {
      if (!monitoringFormRef.value) return

      await monitoringFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            // 添加planId到提交数据中
            const submitData = {
              planId: planId.value,
              monitoringDate: monitoringForm.value.monitoringDate,
              heightMeasurement: parseFloat(monitoringForm.value.heightMeasurement),
              widthMeasurement: parseFloat(monitoringForm.value.widthMeasurement),
              healthStatus: monitoringForm.value.healthStatus,
              temperature: parseFloat(monitoringForm.value.temperature),
              soilMoisture: parseFloat(monitoringForm.value.soilMoisture),
              humidity: monitoringForm.value.humidity ? parseFloat(monitoringForm.value.humidity) : null,
              lightIntensity: monitoringForm.value.lightIntensity ? parseInt(monitoringForm.value.lightIntensity) : null,
              phLevel: monitoringForm.value.phLevel ? parseFloat(monitoringForm.value.phLevel) : null,
              notes: monitoringForm.value.notes
            }

            await createGrowthMonitoring(submitData)
            ElMessage.success('生长监控记录添加成功')
            addMonitoringDialogVisible.value = false
            // 重新获取生长监控列表
            fetchGrowthMonitoring()
          } catch (error) {
            console.error('添加生长监控记录失败:', error)
            ElMessage.error('添加生长监控记录失败: ' + (error.message || '网络错误'))
          }
        }
      })
    }

    // 提交编辑监控表单
    const submitEditMonitoringForm = async () => {
      if (!monitoringFormRef.value) return

      await monitoringFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            // 更新记录数据
            const updateData = {
              monitoringDate: monitoringForm.value.monitoringDate,
              heightMeasurement: parseFloat(monitoringForm.value.heightMeasurement),
              widthMeasurement: parseFloat(monitoringForm.value.widthMeasurement),
              healthStatus: monitoringForm.value.healthStatus,
              temperature: parseFloat(monitoringForm.value.temperature),
              soilMoisture: parseFloat(monitoringForm.value.soilMoisture),
              humidity: monitoringForm.value.humidity ? parseFloat(monitoringForm.value.humidity) : null,
              lightIntensity: monitoringForm.value.lightIntensity ? parseInt(monitoringForm.value.lightIntensity) : null,
              phLevel: monitoringForm.value.phLevel ? parseFloat(monitoringForm.value.phLevel) : null,
              notes: monitoringForm.value.notes
            }

            await updateGrowthMonitoring(monitoringForm.value.id, updateData)
            ElMessage.success('生长监控记录更新成功')
            editMonitoringDialogVisible.value = false
            // 重新获取生长监控列表
            fetchGrowthMonitoring()
          } catch (error) {
            console.error('更新生长监控记录失败:', error)
            ElMessage.error('更新生长监控记录失败: ' + (error.message || '网络错误'))
          }
        }
      })
    }

    // 删除监控记录
    const deleteMonitoringRecord = async (id) => {
      try {
        await ElMessageBox.confirm('此操作将永久删除该生长监控记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 调用删除API
        await deleteGrowthMonitoring(id)
        ElMessage.success('生长监控记录已删除')
        // 重新获取生长监控列表
        fetchGrowthMonitoring()
      } catch (error) {
        console.log('取消删除')
      }
    }

    // 采集监控数据
    const collectMonitoringData = async () => {
      try {
        await ElMessageBox.confirm('此操作将自动生成一条随机的生长监控记录，是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await collectGrowthMonitoring(planId.value)

        if (response.data && response.data.code === 200) {
          ElMessage.success('自动采集成功')
          // 重新获取生长监控列表
          fetchGrowthMonitoring()
        } else {
          ElMessage.error('自动采集失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('自动采集失败:', error)
          ElMessage.error('自动采集失败: ' + (error.message || '网络错误'))
        }
      }
    }

    onMounted(() => {
      fetchPlanDetail()
      fetchPlantingRecords()
      fetchGrowthMonitoring()
    })

    return {
      planDetail,
      plantingRecords,
      growthMonitoring,
      recordsLoading,
      monitoringLoading,
      detailLoading,
      addRecordDialogVisible,
      editRecordDialogVisible,
      recordForm,
      editRecordForm,
      recordFormRef,
      recordFormRules,
      getStatusTagType,
      getStatusText,
      getOperationTypeTagType,
      getOperationTypeText,
      getHealthStatusTagType,
      getHealthStatusText,
      goBack,
      handleEdit,
      handleDelete,
      showAddRecordDialog,
      showEditRecordDialog,
      deleteRecord,
      submitRecordForm,
      submitEditRecordForm,
      showAddMonitoringDialog,
      showEditMonitoringDialog,
      deleteMonitoringRecord,
      collectMonitoringData,
      // 生长监控相关
      addMonitoringDialogVisible,
      editMonitoringDialogVisible,
      monitoringForm,
      monitoringFormRef,
      monitoringFormRules,
      showAddMonitoringDialog,
      showEditMonitoringDialog,
      submitMonitoringForm,
      submitEditMonitoringForm,
      deleteMonitoringRecord
    }
  }
}
</script>

<style scoped>
.planting-plan-detail-container {
  padding: 0px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.detail-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.detail-content {
  padding: 20px 0;
}
</style>