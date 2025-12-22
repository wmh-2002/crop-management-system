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
              placeholder="搜索计划" 
              style="width: 200px; margin-left: 10px;" 
              clearable
            />
            <el-select 
              v-model="statusFilter" 
              placeholder="状态筛选" 
              style="margin-left: 10px; width: 120px;"
              clearable
            >
              <el-option label="计划中" value="PLANNED" />
              <el-option label="进行中" value="IN_PROGRESS" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已取消" value="CANCELLED" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowRight, ArrowDown, Plus, Download } from '@element-plus/icons-vue'
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
        const response = await getPlantingPlanList({
          page: pagination.currentPage,
          size: pagination.pageSize
        })
        planList.value = response.data
      } catch (error) {
        console.error('获取种植计划列表失败:', error)
        ElMessage.error('获取种植计划列表失败')
      } finally {
        loading.value = false
      }
    }
    
    // 获取农田列表
    const fetchFarmlandList = async () => {
      try {
        const response = await getFarmlandList()
        farmlandList.value = response.data
      } catch (error) {
        console.error('获取农田列表失败:', error)
      }
    }
    
    // 获取作物列表
    const fetchCropList = async () => {
      try {
        const response = await getCropList()
        cropList.value = response.data
      } catch (error) {
        console.error('获取作物列表失败:', error)
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
        ...plan,
        farmlandId: plan.farmland.id,
        cropId: plan.crop.id
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
            // 创建或更新数据对象
            const formData = {
              ...planForm,
              farmland: { id: planForm.farmlandId },
              crop: { id: planForm.cropId }
            }
            
            if (dialogType.value === 'add') {
              await createPlantingPlan(formData)
              ElMessage.success('种植计划添加成功')
            } else {
              await updatePlantingPlan(planForm.id, formData)
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
    
    // 分页处理
    const handleSizeChange = (size) => {
      pagination.pageSize = size
      fetchPlanList()
    }
    
    const handleCurrentChange = (page) => {
      pagination.currentPage = page
      fetchPlanList()
    }
    
    const dialogTitle = () => {
      return dialogType.value === 'add' ? '添加种植计划' : '编辑种植计划'
    }
    
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