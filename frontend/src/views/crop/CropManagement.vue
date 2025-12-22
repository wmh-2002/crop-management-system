<template>
  <div class="crop-management-container">
    <div class="page-title">
      <h2>作物管理</h2>
    </div>
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="card-header-left">
            <el-button type="primary" @click="handleAddCrop">
              <el-icon><Plus /></el-icon>
              新增
            </el-button>
            <el-input 
              v-model="searchKeyword" 
              placeholder="搜索作物" 
              style="width: 200px; margin-left: 10px;" 
              clearable
            />
            <el-select 
              v-model="seasonFilter" 
              placeholder="季节筛选" 
              style="margin-left: 10px; width: 120px;"
              clearable
            >
              <el-option label="春夏季" value="春夏季" />
              <el-option label="秋季" value="秋季" />
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
                  <el-dropdown-item>卡片视图</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </template>
      
      <el-table 
        :data="cropList" 
        style="width: 100%" 
        v-loading="loading"
        row-key="id"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="作物名称" width="150" />
        <el-table-column prop="variety" label="品种" width="120" />
        <el-table-column prop="plantingSeason" label="种植季节" width="120" />
        <el-table-column prop="growthPeriod" label="生长期(天)" width="120">
          <template #default="{ row }">
            {{ row.growthPeriod }} 天
          </template>
        </el-table-column>
        <el-table-column prop="expectedYield" label="预期产量" width="120">
          <template #default="{ row }">
            {{ row.expectedYield }} kg
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
    
    <!-- 作物编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form 
        :model="cropForm" 
        :rules="cropRules" 
        ref="cropFormRef"
        label-width="120px"
      >
        <el-form-item label="作物名称" prop="name">
          <el-input v-model="cropForm.name" placeholder="请输入作物名称" />
        </el-form-item>
        <el-form-item label="作物品种" prop="variety">
          <el-input v-model="cropForm.variety" placeholder="请输入作物品种" />
        </el-form-item>
        <el-form-item label="种植季节" prop="plantingSeason">
          <el-input v-model="cropForm.plantingSeason" placeholder="请输入种植季节" />
        </el-form-item>
        <el-form-item label="生长期(天)" prop="growthPeriod">
          <el-input v-model.number="cropForm.growthPeriod" type="number" placeholder="请输入生长期" />
        </el-form-item>
        <el-form-item label="预期产量(kg)" prop="expectedYield">
          <el-input v-model.number="cropForm.expectedYield" type="number" placeholder="请输入预期产量" />
        </el-form-item>
        <el-form-item label="水分需求" prop="waterNeeds">
          <el-input v-model="cropForm.waterNeeds" placeholder="请输入水分需求" />
        </el-form-item>
        <el-form-item label="肥料需求" prop="fertilizerNeeds">
          <el-input v-model="cropForm.fertilizerNeeds" placeholder="请输入肥料需求" />
        </el-form-item>
        <el-form-item label="病虫害信息" prop="diseaseInfo">
          <el-input 
            v-model="cropForm.diseaseInfo" 
            type="textarea" 
            :rows="3"
            placeholder="请输入病虫害信息"
          />
        </el-form-item>
        <el-form-item label="作物描述" prop="description">
          <el-input 
            v-model="cropForm.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入作物描述"
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
import { getCropList, createCrop, updateCrop, deleteCrop } from '@/api/crop'

export default {
  name: 'CropManagement',
  setup() {
    const cropList = ref([])
    const loading = ref(false)
    const dialogVisible = ref(false)
    const dialogType = ref('add') // 'add' 或 'edit'
    const cropFormRef = ref(null)
    const searchKeyword = ref('')
    const seasonFilter = ref('')
    
    const cropForm = reactive({
      id: null,
      name: '',
      variety: '',
      plantingSeason: '',
      growthPeriod: null,
      expectedYield: null,
      waterNeeds: '',
      fertilizerNeeds: '',
      diseaseInfo: '',
      description: ''
    })
    
    const cropRules = {
      name: [
        { required: true, message: '请输入作物名称', trigger: 'blur' },
        { min: 2, max: 50, message: '作物名称长度在2-50个字符之间', trigger: 'blur' }
      ],
      variety: [
        { required: true, message: '请输入作物品种', trigger: 'blur' }
      ],
      plantingSeason: [
        { required: true, message: '请输入种植季节', trigger: 'blur' }
      ],
      growthPeriod: [
        { required: true, message: '请输入生长期', trigger: 'blur' },
        { type: 'number', message: '生长期必须为数字', trigger: 'blur' }
      ],
      expectedYield: [
        { required: true, message: '请输入预期产量', trigger: 'blur' },
        { type: 'number', message: '预期产量必须为数字', trigger: 'blur' }
      ]
    }
    
    const pagination = reactive({
      currentPage: 1,
      pageSize: 10,
      total: 0
    })
    
    // 获取作物列表
    const fetchCropList = async () => {
      loading.value = true
      try {
        const response = await getCropList({
          page: pagination.currentPage,
          size: pagination.pageSize
        })
        cropList.value = response.data
      } catch (error) {
        console.error('获取作物列表失败:', error)
        ElMessage.error('获取作物列表失败')
      } finally {
        loading.value = false
      }
    }
    
    // 添加作物
    const handleAddCrop = () => {
      dialogType.value = 'add'
      Object.assign(cropForm, {
        id: null,
        name: '',
        variety: '',
        plantingSeason: '',
        growthPeriod: null,
        expectedYield: null,
        waterNeeds: '',
        fertilizerNeeds: '',
        diseaseInfo: '',
        description: ''
      })
      dialogVisible.value = true
    }
    
    // 编辑作物
    const handleEdit = (crop) => {
      dialogType.value = 'edit'
      Object.assign(cropForm, { ...crop })
      dialogVisible.value = true
    }
    
    // 删除作物
    const handleDelete = (id) => {
      ElMessageBox.confirm('此操作将永久删除该作物, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteCrop(id)
          ElMessage.success('删除成功')
          fetchCropList() // 重新获取列表
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败: ' + (error.message || '网络错误'))
        }
      })
    }
    
    // 提交表单
    const submitForm = async () => {
      if (!cropFormRef.value) return
      
      await cropFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            if (dialogType.value === 'add') {
              await createCrop(cropForm)
              ElMessage.success('作物添加成功')
            } else {
              await updateCrop(cropForm.id, cropForm)
              ElMessage.success('作物更新成功')
            }
            dialogVisible.value = false
            fetchCropList() // 重新获取列表
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
      fetchCropList()
    }
    
    const handleCurrentChange = (page) => {
      pagination.currentPage = page
      fetchCropList()
    }
    
    const dialogTitle = () => {
      return dialogType.value === 'add' ? '添加作物' : '编辑作物'
    }
    
    onMounted(() => {
      fetchCropList()
    })
    
    return {
      cropList,
      loading,
      dialogVisible,
      dialogType,
      cropForm,
      cropFormRef,
      cropRules,
      pagination,
      handleAddCrop,
      handleEdit,
      handleDelete,
      submitForm,
      handleSizeChange,
      handleCurrentChange,
      dialogTitle,
      searchKeyword,
      seasonFilter,
      ArrowRight,
      ArrowDown,
      Plus,
      Download
    }
  }
}

// 由于我们还没有创建crop API，需要先创建它
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