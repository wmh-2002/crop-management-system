<template>
  <div class="planting-plan-container">
    <div class="page-title">
      <h2>种植计划管理</h2>
    </div>
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="card-header-left">
            <el-button type="primary" @click="handleAddPlan">
              <el-icon><Plus /></el-icon>
              新增
            </el-button>
            <el-input
              v-model="searchKeyword"
              placeholder="搜索计划名称"
              style="width: 200px; margin-left: 10px;"
              clearable
              @keyup.enter="handleSearch"
            />
            <el-select
              v-model="statusFilter"
              placeholder="状态筛选"
              style="margin-left: 10px; width: 120px;"
              clearable
              @change="handleFilter"
            >
              <el-option label="计划中" value="PLANNED" />
              <el-option label="进行中" value="IN_PROGRESS" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已取消" value="CANCELLED" />
            </el-select>
            <el-button type="primary" @click="handleSearch" style="margin-left: 10px;">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset" style="margin-left: 5px;">
              重置
            </el-button>
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
                  <el-dropdown-item>甘特图视图</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </template>
      
      <el-table 
        :data="planList" 
        style="width: 100%" 
        v-loading="loading"
        row-key="id"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="planName" label="计划名称" width="150" />
        <el-table-column prop="farmland.name" label="农田名称" width="120" />
        <el-table-column prop="crop.name" label="作物" width="120" />
        <el-table-column prop="plannedStartDate" label="计划开始日期" width="120" />
        <el-table-column prop="plannedEndDate" label="计划结束日期" width="120" />
        <el-table-column prop="expectedHarvestDate" label="预期收获日期" width="140" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
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
    
    <!-- 种植计划编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form 
        :model="planForm" 
        :rules="planRules" 
        ref="planFormRef"
        label-width="120px"
      >
        <el-form-item label="计划名称" prop="planName">
          <el-input v-model="planForm.planName" placeholder="请输入计划名称" />
        </el-form-item>
        <el-form-item label="农田选择" prop="farmlandId">
          <el-select v-model="planForm.farmlandId" placeholder="请选择农田" style="width: 100%">
            <el-option 
              v-for="farmland in farmlandList" 
              :key="farmland.id" 
              :label="farmland.name" 
              :value="farmland.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="作物选择" prop="cropId">
          <el-select v-model="planForm.cropId" placeholder="请选择作物" style="width: 100%">
            <el-option 
              v-for="crop in cropList" 
              :key="crop.id" 
              :label="crop.name" 
              :value="crop.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="计划开始日期" prop="plannedStartDate">
          <el-date-picker
            v-model="planForm.plannedStartDate"
            type="date"
            placeholder="选择开始日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="计划结束日期" prop="plannedEndDate">
          <el-date-picker
            v-model="planForm.plannedEndDate"
            type="date"
            placeholder="选择结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="预期收获日期" prop="expectedHarvestDate">
          <el-date-picker
            v-model="planForm.expectedHarvestDate"
            type="date"
            placeholder="选择收获日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="播种密度" prop="sowingDensity">
          <el-input v-model="planForm.sowingDensity" placeholder="请输入播种密度" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="planForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="计划中" value="PLANNED" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input 
            v-model="planForm.notes" 
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
import { ArrowRight, ArrowDown, Plus, Download, Search } from '@element-plus/icons-vue'
import { 
  getPlantingPlanList, 
  createPlantingPlan, 
  updatePlantingPlan, 
  deletePlantingPlan 
} from '@/api/plantingPlan'
import { getFarmlandList } from '@/api/farmland'
import { getCropList } from '@/api/crop'

export default {
  name: 'PlantingPlan',
  setup() {
    const planList = ref([])
    const farmlandList = ref([])
    const cropList = ref([])
    const loading = ref(false)
    const dialogVisible = ref(false)
    const dialogType = ref('add') // 'add' 或 'edit'
    const planFormRef = ref(null)
    const searchKeyword = ref('')
    const statusFilter = ref('')
    
    const planForm = reactive({
      id: null,
      planName: '',
      farmlandId: null,
      cropId: null,
      plannedStartDate: '',
      plannedEndDate: '',
      expectedHarvestDate: '',
      sowingDensity: '',
      status: 'PLANNED',
      notes: ''
    })
    
    const planRules = {
      planName: [
        { required: true, message: '请输入计划名称', trigger: 'blur' },
        { min: 2, max: 50, message: '计划名称长度在2-50个字符之间', trigger: 'blur' }
      ],
      farmlandId: [
        { required: true, message: '请选择农田', trigger: 'change' }
      ],
      cropId: [
        { required: true, message: '请选择作物', trigger: 'change' }
      ],
      plannedStartDate: [
        { required: true, message: '请选择计划开始日期', trigger: 'change' }
      ],
      plannedEndDate: [
        { required: true, message: '请选择计划结束日期', trigger: 'change' }
      ],
      expectedHarvestDate: [
        { required: true, message: '请选择预期收获日期', trigger: 'change' }
      ]
    }
    
    const pagination = reactive({
      currentPage: 1,
      pageSize: 10,
      total: 0
    })
    
    // 获取种植计划列表
    const fetchPlanList = async () => {
      loading.value = true
      try {
        console.log('开始获取种植计划列表...')
        const params = {
          page: pagination.currentPage,
          size: pagination.pageSize
        }

        // 添加筛选参数
        if (searchKeyword.value.trim()) {
          params.planName = searchKeyword.value.trim()
        }

        if (statusFilter.value) {
          params.status = statusFilter.value
        }

        console.log('请求参数:', params)
        const response = await getPlantingPlanList(params)
        console.log('种植计划列表响应:', response)

        // 后端返回的数据格式：{ code, message, data: { content, page, size, totalElements, ... } }
        if (response.data && response.data.code === 200 && response.data.data) {
          const pageData = response.data.data
          planList.value = pageData.content || []
          pagination.total = pageData.totalElements || 0
          pagination.currentPage = pageData.page || 1
          pagination.pageSize = pageData.size || 10
          console.log('成功获取种植计划列表:', planList.value.length, '条记录')
        } else {
          planList.value = []
          pagination.total = 0
          console.log('种植计划列表响应格式不正确:', response.data)
        }
      } catch (error) {
        console.error('获取种植计划列表失败:', error)
        ElMessage.error('获取种植计划列表失败')
        planList.value = []
        pagination.total = 0
      } finally {
        loading.value = false
      }
    }

    // 获取农田列表（用于下拉选择）
    const fetchFarmlandList = async () => {
      try {
        console.log('开始获取农田列表...')
        const response = await getFarmlandList({
          page: 1,
          size: 1000 // 获取所有农田用于选择
        })
        console.log('农田列表响应:', response)

        if (response.data && response.data.code === 200 && response.data.data) {
          farmlandList.value = response.data.data.content || []
          console.log('成功获取农田列表:', farmlandList.value.length, '条记录')
        } else {
          farmlandList.value = []
          console.log('农田列表响应格式不正确:', response.data)
        }
      } catch (error) {
        console.error('获取农田列表失败:', error)
        farmlandList.value = []
      }
    }

    // 获取作物列表（用于下拉选择）
    const fetchCropList = async () => {
      try {
        console.log('开始获取作物列表...')
        const response = await getCropList({
          page: 1,
          size: 1000 // 获取所有作物用于选择
        })
        console.log('作物列表响应:', response)

        if (response.data && response.data.code === 200 && response.data.data) {
          cropList.value = response.data.data.content || []
          console.log('成功获取作物列表:', cropList.value.length, '条记录')
        } else {
          cropList.value = []
          console.log('作物列表响应格式不正确:', response.data)
        }
      } catch (error) {
        console.error('获取作物列表失败:', error)
        cropList.value = []
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
    
    // 添加种植计划
    const handleAddPlan = () => {
      dialogType.value = 'add'
      Object.assign(planForm, {
        id: null,
        planName: '',
        farmlandId: null,
        cropId: null,
        plannedStartDate: '',
        plannedEndDate: '',
        expectedHarvestDate: '',
        sowingDensity: '',
        status: 'PLANNED',
        notes: ''
      })
      dialogVisible.value = true
    }
    
    // 编辑种植计划
    const handleEdit = (plan) => {
      dialogType.value = 'edit'
      Object.assign(planForm, {
        ...plan
      })
      dialogVisible.value = true
    }
    
    // 删除种植计划
    const handleDelete = (id) => {
      ElMessageBox.confirm('此操作将永久删除该种植计划, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deletePlantingPlan(id)
          ElMessage.success('删除成功')
          fetchPlanList() // 重新获取列表
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败: ' + (error.message || '网络错误'))
        }
      })
    }
    
    // 提交表单
    const submitForm = async () => {
      if (!planFormRef.value) return

      await planFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            if (dialogType.value === 'add') {
              // 创建种植计划 - 传递所有字段
              const createData = {
                farmlandId: planForm.farmlandId,
                cropId: planForm.cropId,
                planName: planForm.planName,
                plannedStartDate: planForm.plannedStartDate,
                plannedEndDate: planForm.plannedEndDate,
                expectedHarvestDate: planForm.expectedHarvestDate || undefined,
                sowingDensity: planForm.sowingDensity || undefined,
                notes: planForm.notes || undefined,
                status: planForm.status
              }
              console.log('创建种植计划数据:', createData)
              await createPlantingPlan(createData)
              ElMessage.success('种植计划添加成功')
            } else {
              // 更新种植计划 - 只传递有值的字段
              const updateData = {}
              if (planForm.farmlandId) updateData.farmlandId = planForm.farmlandId
              if (planForm.cropId) updateData.cropId = planForm.cropId
              if (planForm.planName) updateData.planName = planForm.planName
              if (planForm.plannedStartDate) updateData.plannedStartDate = planForm.plannedStartDate
              if (planForm.plannedEndDate) updateData.plannedEndDate = planForm.plannedEndDate
              if (planForm.expectedHarvestDate !== undefined) updateData.expectedHarvestDate = planForm.expectedHarvestDate || null
              if (planForm.sowingDensity !== undefined) updateData.sowingDensity = planForm.sowingDensity || null
              if (planForm.notes !== undefined) updateData.notes = planForm.notes || null
              if (planForm.status) updateData.status = planForm.status

              console.log('更新种植计划数据:', updateData)
              await updatePlantingPlan(planForm.id, updateData)
              ElMessage.success('种植计划更新成功')
            }
            dialogVisible.value = false
            fetchPlanList() // 重新获取列表
          } catch (error) {
              console.error('保存失败:', error)
              ElMessage.error('保存失败: ' + (error.message || '网络错误'))
            }
        }
      })
    }
    
    // 搜索处理
    const handleSearch = () => {
      pagination.currentPage = 1 // 搜索时重置到第一页
      fetchPlanList()
    }

    // 筛选处理
    const handleFilter = () => {
      pagination.currentPage = 1 // 筛选时重置到第一页
      fetchPlanList()
    }

    // 重置筛选
    const handleReset = () => {
      searchKeyword.value = ''
      statusFilter.value = ''
      pagination.currentPage = 1
      fetchPlanList()
    }

    // 分页处理
    const handleSizeChange = (size) => {
      pagination.pageSize = size
      fetchPlanList()
    }

    const handleCurrentChange = (page) => {
      pagination.currentPage = page
      fetchPlanList()
    }
    
    const dialogTitle = computed(() => {
      return dialogType.value === 'add' ? '添加种植计划' : '编辑种植计划'
    })
    
    onMounted(() => {
      fetchPlanList()
      fetchFarmlandList()
      fetchCropList()
    })
    
    return {
      planList,
      farmlandList,
      cropList,
      loading,
      dialogVisible,
      dialogType,
      planForm,
      planFormRef,
      planRules,
      pagination,
      getStatusTagType,
      getStatusText,
      handleAddPlan,
      handleEdit,
      handleDelete,
      submitForm,
      handleSearch,
      handleFilter,
      handleReset,
      handleSizeChange,
      handleCurrentChange,
      dialogTitle,
      searchKeyword,
      statusFilter,
      ArrowRight,
      ArrowDown,
      Plus,
      Download,
      Search
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