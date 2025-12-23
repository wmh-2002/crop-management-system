<template>
  <div class="notifications-container">
    <div class="page-title">
      <h2>通知与提醒</h2>
    </div>
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="card-header-left">
            <el-button type="primary" @click="handleAddNotification">
              <el-icon><Plus /></el-icon>
              新增
            </el-button>
            <el-input 
              v-model="searchKeyword" 
              placeholder="搜索通知" 
              style="width: 200px; margin-left: 10px;" 
              clearable
            />
            <el-select 
              v-model="typeFilter" 
              placeholder="类型筛选" 
              style="margin-left: 10px; width: 150px;"
              clearable
            >
              <el-option label="施肥提醒" value="FERTILIZING" />
              <el-option label="浇水提醒" value="WATERING" />
              <el-option label="病虫害防治提醒" value="PESTICIDING" />
              <el-option label="生长监控提醒" value="MONITORING" />
              <el-option label="收获提醒" value="HARVESTING" />
              <el-option label="其他提醒" value="OTHER" />
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
                  <el-dropdown-item>日历视图</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </template>
      
      <el-table 
        :data="notificationList" 
        style="width: 100%" 
        v-loading="loading"
        row-key="id"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip />
        <el-table-column prop="content" label="内容" width="250" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">
              {{ getTypeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="{ row }">
            <el-tag :type="getPriorityTagType(row.priority)">
              {{ getPriorityText(row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="scheduledTime" label="计划时间" width="160" />
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
    
    <!-- 通知编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form 
        :model="notificationForm" 
        :rules="notificationRules" 
        ref="notificationFormRef"
        label-width="120px"
      >
        <el-form-item label="提醒标题" prop="title">
          <el-input v-model="notificationForm.title" placeholder="请输入提醒标题" />
        </el-form-item>
        <el-form-item label="提醒内容" prop="content">
          <el-input 
            v-model="notificationForm.content" 
            type="textarea" 
            :rows="3"
            placeholder="请输入提醒内容"
          />
        </el-form-item>
        <el-form-item label="提醒类型" prop="type">
          <el-select v-model="notificationForm.type" placeholder="请选择提醒类型" style="width: 100%">
            <el-option label="施肥提醒" value="FERTILIZING" />
            <el-option label="浇水提醒" value="WATERING" />
            <el-option label="病虫害防治提醒" value="PESTICIDING" />
            <el-option label="生长监控提醒" value="MONITORING" />
            <el-option label="收获提醒" value="HARVESTING" />
            <el-option label="其他提醒" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="notificationForm.priority" placeholder="请选择优先级" style="width: 100%">
            <el-option label="低" value="LOW" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="高" value="HIGH" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划时间" prop="scheduledTime">
          <el-date-picker
            v-model="notificationForm.scheduledTime"
            type="datetime"
            placeholder="选择计划时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="关联种植计划" prop="planId">
          <el-select 
            v-model="notificationForm.planId" 
            placeholder="请选择关联的种植计划" 
            style="width: 100%"
            clearable
          >
            <el-option 
              v-for="plan in planList" 
              :key="plan.id" 
              :label="plan.planName" 
              :value="plan.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="接收人员" prop="recipientIds">
          <el-select 
            v-model="notificationForm.recipientIds" 
            multiple
            placeholder="请选择接收人员" 
            style="width: 100%"
          >
            <el-option 
              v-for="user in userList" 
              :key="user.id" 
              :label="user.realName" 
              :value="user.id"
            />
          </el-select>
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
  getNotificationList, 
  createNotification, 
  updateNotification, 
  deleteNotification 
} from '@/api/notification'
import { getPlantingPlanList } from '@/api/plantingPlan'
import { getUserList } from '@/api/user'

export default {
  name: 'Notifications',
  setup() {
    const notificationList = ref([])
    const planList = ref([])
    const userList = ref([])
    const loading = ref(false)
    const dialogVisible = ref(false)
    const dialogType = ref('add') // 'add' 或 'edit'
    const notificationFormRef = ref(null)
    const searchKeyword = ref('')
    const typeFilter = ref('')
    
    const notificationForm = reactive({
      id: null,
      title: '',
      content: '',
      type: 'WATERING',
      priority: 'MEDIUM',
      scheduledTime: '',
      status: 'PENDING', // 默认状态
      planId: null,
      recipientIds: []
    })
    
    const notificationRules = {
      title: [
        { required: true, message: '请输入提醒标题', trigger: 'blur' },
        { min: 2, max: 100, message: '提醒标题长度在2-100个字符之间', trigger: 'blur' }
      ],
      content: [
        { required: true, message: '请输入提醒内容', trigger: 'blur' }
      ],
      type: [
        { required: true, message: '请选择提醒类型', trigger: 'change' }
      ],
      priority: [
        { required: true, message: '请选择优先级', trigger: 'change' }
      ],
      scheduledTime: [
        { required: true, message: '请选择计划时间', trigger: 'change' }
      ]
    }
    
    const pagination = reactive({
      currentPage: 1,
      pageSize: 10,
      total: 0
    })
    
    // 获取通知列表
    const fetchNotificationList = async () => {
      loading.value = true
      try {
        const response = await getNotificationList({
          page: pagination.currentPage,
          size: pagination.pageSize
        })
        notificationList.value = response.data
      } catch (error) {
        console.error('获取通知列表失败:', error)
        ElMessage.error('获取通知列表失败')
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
    
    // 获取用户列表
    const fetchUserList = async () => {
      try {
        const response = await getUserList()
        userList.value = response.data
      } catch (error) {
        console.error('获取用户列表失败:', error)
      }
    }
    
    // 类型标签类型
    const getTypeTagType = (type) => {
      switch (type) {
        case 'FERTILIZING': return 'warning'
        case 'WATERING': return 'primary'
        case 'PESTICIDING': return 'danger'
        case 'MONITORING': return 'success'
        case 'HARVESTING': return 'info'
        case 'OTHER': return 'info'
        default: return 'info'
      }
    }
    
    // 类型文本
    const getTypeText = (type) => {
      switch (type) {
        case 'FERTILIZING': return '施肥提醒'
        case 'WATERING': return '浇水提醒'
        case 'PESTICIDING': return '病虫害防治提醒'
        case 'MONITORING': return '生长监控提醒'
        case 'HARVESTING': return '收获提醒'
        case 'OTHER': return '其他提醒'
        default: return type
      }
    }
    
    // 优先级标签类型
    const getPriorityTagType = (priority) => {
      switch (priority) {
        case 'LOW': return 'info'
        case 'MEDIUM': return 'warning'
        case 'HIGH': return 'danger'
        default: return 'info'
      }
    }
    
    // 优先级文本
    const getPriorityText = (priority) => {
      switch (priority) {
        case 'LOW': return '低'
        case 'MEDIUM': return '中'
        case 'HIGH': return '高'
        default: return priority
      }
    }
    
    // 状态标签类型
    const getStatusTagType = (status) => {
      switch (status) {
        case 'PENDING': return 'info'
        case 'SENT': return 'warning'
        case 'READ': return 'success'
        default: return 'info'
      }
    }
    
    // 状态文本
    const getStatusText = (status) => {
      switch (status) {
        case 'PENDING': return '待发送'
        case 'SENT': return '已发送'
        case 'READ': return '已读'
        default: return status
      }
    }
    
    // 添加通知
    const handleAddNotification = () => {
      dialogType.value = 'add'
      Object.assign(notificationForm, {
        id: null,
        title: '',
        content: '',
        type: 'WATERING',
        priority: 'MEDIUM',
        scheduledTime: '',
        status: 'PENDING',
        planId: null,
        recipientIds: []
      })
      dialogVisible.value = true
    }
    
    // 编辑通知
    const handleEdit = (notification) => {
      dialogType.value = 'edit'
      Object.assign(notificationForm, { 
        ...notification,
        planId: notification.plan ? notification.plan.id : null,
        recipientIds: notification.recipientIds ? JSON.parse(notification.recipientIds) : []
      })
      dialogVisible.value = true
    }
    
    // 删除通知
    const handleDelete = (id) => {
      ElMessageBox.confirm('此操作将永久删除该通知, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteNotification(id)
          ElMessage.success('删除成功')
          fetchNotificationList() // 重新获取列表
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败: ' + (error.message || '网络错误'))
        }
      })
    }
    
    // 提交表单
    const submitForm = async () => {
      if (!notificationFormRef.value) return
      
      await notificationFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            // 创建或更新数据对象
            const formData = {
              ...notificationForm,
              plan: notificationForm.planId ? { id: notificationForm.planId } : null,
              recipientIds: JSON.stringify(notificationForm.recipientIds) // 转换为JSON字符串存储
            }
            
            if (dialogType.value === 'add') {
              await createNotification(formData)
              ElMessage.success('通知添加成功')
            } else {
              await updateNotification(notificationForm.id, formData)
              ElMessage.success('通知更新成功')
            }
            dialogVisible.value = false
            fetchNotificationList() // 重新获取列表
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
      fetchNotificationList()
    }
    
    const handleCurrentChange = (page) => {
      pagination.currentPage = page
      fetchNotificationList()
    }
    
    const dialogTitle = computed(() => {
      return dialogType.value === 'add' ? '添加提醒' : '编辑提醒'
    })
    
    onMounted(() => {
      fetchNotificationList()
      fetchPlanList()
      fetchUserList()
    })
    
    return {
      notificationList,
      planList,
      userList,
      loading,
      dialogVisible,
      dialogType,
      notificationForm,
      notificationFormRef,
      notificationRules,
      pagination,
      getTypeTagType,
      getTypeText,
      getPriorityTagType,
      getPriorityText,
      getStatusTagType,
      getStatusText,
      handleAddNotification,
      handleEdit,
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