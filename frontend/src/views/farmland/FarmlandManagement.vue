<template>
  <div class="farmland-management-container">
    <div class="page-title">
      <h2>农田管理</h2>
    </div>
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="card-header-left">
            <el-button type="primary" @click="handleAddFarmland">
              <el-icon><Plus /></el-icon>
              新增
            </el-button>
            <el-input
              v-model="searchKeyword"
              placeholder="搜索农田名称"
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
              <el-option label="可用" value="AVAILABLE" />
              <el-option label="已种植" value="PLANTED" />
              <el-option label="维护中" value="MAINTENANCE" />
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
                  <el-dropdown-item>地图视图</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </template>
      
      <el-table 
        :data="farmlandList" 
        style="width: 100%" 
        v-loading="loading"
        row-key="id"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="农田名称" width="150" />
        <el-table-column prop="area" label="面积(㎡)" width="120">
          <template #default="{ row }">
            {{ row.area }} ㎡
          </template>
        </el-table-column>
        <el-table-column prop="location" label="位置" width="200" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" width="200" show-overflow-tooltip />
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
    
    <!-- 农田编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form 
        :model="farmlandForm" 
        :rules="farmlandRules" 
        ref="farmlandFormRef"
        label-width="120px"
      >
        <el-form-item label="农田名称" prop="name">
          <el-input v-model="farmlandForm.name" placeholder="请输入农田名称" />
        </el-form-item>
        <el-form-item label="农田面积(㎡)" prop="area">
          <el-input v-model.number="farmlandForm.area" type="number" placeholder="请输入农田面积" />
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="farmlandForm.location" placeholder="请输入农田位置（可选）" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="farmlandForm.status" placeholder="请选择农田状态" style="width: 100%">
            <el-option label="可用" value="AVAILABLE" />
            <el-option label="已种植" value="PLANTED" />
            <el-option label="维护中" value="MAINTENANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input 
            v-model="farmlandForm.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入农田描述"
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
import { getFarmlandList, createFarmland, updateFarmland, deleteFarmland } from '@/api/farmland'

export default {
  name: 'FarmlandManagement',
  setup() {
    const farmlandList = ref([])
    const loading = ref(false)
    const dialogVisible = ref(false)
    const dialogType = ref('add') // 'add' 或 'edit'
    const farmlandFormRef = ref(null)
    const searchKeyword = ref('')
    const statusFilter = ref('')
    
    const farmlandForm = reactive({
      id: null,
      name: '',
      area: null,
      location: '',
      status: 'AVAILABLE',
      description: ''
    })
    
    const farmlandRules = {
      name: [
        { required: true, message: '请输入农田名称', trigger: 'blur' },
        { min: 2, max: 50, message: '农田名称长度在2-50个字符之间', trigger: 'blur' }
      ],
      area: [
        { required: true, message: '请输入农田面积', trigger: 'blur' },
        { type: 'number', min: 0.01, message: '农田面积必须大于0', trigger: 'blur' }
      ]
    }
    
    const pagination = reactive({
      currentPage: 1,
      pageSize: 10,
      total: 0
    })
    
    // 获取农田列表
    const fetchFarmlandList = async () => {
      loading.value = true
      try {
        console.log('开始获取农田列表...')
        const params = {
          page: pagination.currentPage,
          size: pagination.pageSize
        }

        // 添加筛选参数
        if (searchKeyword.value.trim()) {
          params.name = searchKeyword.value.trim()
        }

        if (statusFilter.value) {
          params.status = statusFilter.value
        }

        console.log('请求参数:', params)
        const response = await getFarmlandList(params)
        console.log('农田列表响应:', response)

        // 后端返回的数据格式：{ code, message, data: { content, page, size, totalElements, ... } }
        if (response.data && response.data.code === 200 && response.data.data) {
          const pageData = response.data.data
          farmlandList.value = pageData.content || []
          pagination.total = pageData.totalElements || 0
          pagination.currentPage = pageData.page || 1
          pagination.pageSize = pageData.size || 10
          console.log('成功获取农田列表:', farmlandList.value.length, '条记录')
        } else {
          farmlandList.value = []
          pagination.total = 0
          console.log('农田列表响应格式不正确:', response.data)
        }
      } catch (error) {
        console.error('获取农田列表失败:', error)
        ElMessage.error('获取农田列表失败')
        farmlandList.value = []
        pagination.total = 0
      } finally {
        loading.value = false
      }
    }
    
    // 状态标签类型
    const getStatusTagType = (status) => {
      switch (status) {
        case 'AVAILABLE': return 'success'
        case 'PLANTED': return 'warning'
        case 'MAINTENANCE': return 'info'
        default: return 'info'
      }
    }
    
    // 状态文本
    const getStatusText = (status) => {
      switch (status) {
        case 'AVAILABLE': return '可用'
        case 'PLANTED': return '已种植'
        case 'MAINTENANCE': return '维护中'
        default: return status
      }
    }
    
    // 添加农田
    const handleAddFarmland = () => {
      dialogType.value = 'add'
      Object.assign(farmlandForm, {
        id: null,
        name: '',
        area: null,
        location: '',
        status: 'AVAILABLE',
        description: ''
      })
      dialogVisible.value = true
    }
    
    // 编辑农田
    const handleEdit = (farmland) => {
      dialogType.value = 'edit'
      Object.assign(farmlandForm, { ...farmland })
      dialogVisible.value = true
    }
    
    // 删除农田
    const handleDelete = (id) => {
      ElMessageBox.confirm('此操作将永久删除该农田, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteFarmland(id)
          ElMessage.success('删除成功')
          fetchFarmlandList() // 重新获取列表
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败: ' + (error.message || '网络错误'))
        }
      })
    }
    
    // 提交表单
    const submitForm = async () => {
      if (!farmlandFormRef.value) return

      await farmlandFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            if (dialogType.value === 'add') {
              // 创建农田 - 传递所有字段
              const createData = {
                name: farmlandForm.name,
                area: farmlandForm.area,
                location: farmlandForm.location || undefined,
                status: farmlandForm.status,
                description: farmlandForm.description || undefined
              }
              console.log('创建农田数据:', createData)
              await createFarmland(createData)
              ElMessage.success('农田添加成功')
            } else {
              // 更新农田 - 只传递有值的字段
              const updateData = {}
              if (farmlandForm.name) updateData.name = farmlandForm.name
              if (farmlandForm.area) updateData.area = farmlandForm.area
              if (farmlandForm.location !== undefined) updateData.location = farmlandForm.location || null
              if (farmlandForm.status) updateData.status = farmlandForm.status
              if (farmlandForm.description !== undefined) updateData.description = farmlandForm.description || null

              console.log('更新农田数据:', updateData)
              await updateFarmland(farmlandForm.id, updateData)
              ElMessage.success('农田更新成功')
            }
            dialogVisible.value = false
            fetchFarmlandList() // 重新获取列表
          } catch (error) {
              console.error('保存失败:', error)
              const errorMessage = error.response?.data?.message || error.message || '网络错误'
              ElMessage.error('保存失败: ' + errorMessage)
            }
        }
      })
    }
    
    // 搜索处理
    const handleSearch = () => {
      pagination.currentPage = 1 // 搜索时重置到第一页
      fetchFarmlandList()
    }

    // 筛选处理
    const handleFilter = () => {
      pagination.currentPage = 1 // 筛选时重置到第一页
      fetchFarmlandList()
    }

    // 重置筛选
    const handleReset = () => {
      searchKeyword.value = ''
      statusFilter.value = ''
      pagination.currentPage = 1
      fetchFarmlandList()
    }

    // 分页处理
    const handleSizeChange = (size) => {
      pagination.pageSize = size
      fetchFarmlandList()
    }

    const handleCurrentChange = (page) => {
      pagination.currentPage = page
      fetchFarmlandList()
    }
    
    const dialogTitle = computed(() => {
      return dialogType.value === 'add' ? '添加农田' : '编辑农田'
    })
    
    onMounted(() => {
      fetchFarmlandList()
    })
    
    return {
      farmlandList,
      loading,
      dialogVisible,
      dialogType,
      farmlandForm,
      farmlandFormRef,
      farmlandRules,
      pagination,
      getStatusTagType,
      getStatusText,
      handleAddFarmland,
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