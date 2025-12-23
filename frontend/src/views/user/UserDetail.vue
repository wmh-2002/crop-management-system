<template>
  <div class="user-detail-container">
    <div class="page-title">
      <h2>用户详情</h2>
      <el-button @click="goBack" type="primary" plain>返回列表</el-button>
    </div>

    <el-card v-loading="loading" class="detail-card">
      <template #header>
        <div class="card-header">
          <span>{{ user.realName || user.username }}</span>
          <el-tag :type="getRoleTagType(user.role)">
            {{ getRoleText(user.role) }}
          </el-tag>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="12">
          <div class="detail-section">
            <h3>基本信息</h3>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="用户名">
                {{ user.username || '暂无' }}
              </el-descriptions-item>
              <el-descriptions-item label="真实姓名">
                {{ user.realName || '暂无' }}
              </el-descriptions-item>
              <el-descriptions-item label="邮箱">
                {{ user.email || '暂无' }}
              </el-descriptions-item>
              <el-descriptions-item label="电话">
                {{ user.phone || '暂无' }}
              </el-descriptions-item>
              <el-descriptions-item label="角色">
                {{ getRoleText(user.role) }}
              </el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="user.status ? 'success' : 'danger'">
                  {{ user.status ? '启用' : '禁用' }}
                </el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-col>

        <el-col :span="12">
          <div class="detail-section">
            <h3>时间信息</h3>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="创建时间">
                {{ formatDate(user.createdAt) }}
              </el-descriptions-item>
              <el-descriptions-item label="更新时间">
                {{ formatDate(user.updatedAt) }}
              </el-descriptions-item>
            </el-descriptions>
          </div>

          <div class="detail-section">
            <h3>操作</h3>
            <el-space>
              <el-button type="primary" @click="handleEdit">编辑用户</el-button>
              <el-button
                :type="user.status ? 'warning' : 'success'"
                @click="handleToggleStatus"
              >
                {{ user.status ? '禁用用户' : '启用用户' }}
              </el-button>
              <el-button type="danger" @click="handleDelete">删除用户</el-button>
            </el-space>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserById, deleteUser, enableUser, disableUser } from '@/api/user'

export default {
  name: 'UserDetail',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const loading = ref(false)
    const user = ref({})

    // 获取用户详情
    const fetchUserDetail = async () => {
      const userId = route.params.id
      if (!userId) {
        ElMessage.error('用户ID不存在')
        router.push('/users')
        return
      }

      loading.value = true
      try {
        console.log('获取用户详情:', userId)
        const response = await getUserById(userId)
        console.log('用户详情响应:', response)

        if (response.data && response.data.code === 200 && response.data.data) {
          user.value = response.data.data
        } else {
          ElMessage.error('获取用户详情失败')
          router.push('/users')
        }
      } catch (error) {
        console.error('获取用户详情失败:', error)
        ElMessage.error('获取用户详情失败')
        router.push('/users')
      } finally {
        loading.value = false
      }
    }

    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return '暂无'
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    }

    // 角色标签类型
    const getRoleTagType = (role) => {
      switch (role) {
        case 'ADMIN': return 'danger'
        case 'FARMER': return 'warning'
        case 'STAFF': return 'primary'
        default: return 'info'
      }
    }

    // 角色文本
    const getRoleText = (role) => {
      switch (role) {
        case 'ADMIN': return '管理员'
        case 'FARMER': return '农场主'
        case 'STAFF': return '工作人员'
        default: return role
      }
    }

    // 返回列表
    const goBack = () => {
      router.push('/users')
    }

    // 编辑用户
    const handleEdit = () => {
      router.push(`/users/edit/${user.value.id}`)
    }

    // 切换用户状态
    const handleToggleStatus = async () => {
      const action = user.value.status ? '禁用' : '启用'
      try {
        const confirmResult = await ElMessageBox.confirm(
          `确定要${action}用户 ${user.value.realName || user.value.username} 吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )

        if (confirmResult) {
          if (user.value.status) {
            await disableUser(user.value.id)
            ElMessage.success('用户已禁用')
          } else {
            await enableUser(user.value.id)
            ElMessage.success('用户已启用')
          }
          // 重新获取用户详情
          await fetchUserDetail()
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('切换用户状态失败:', error)
          ElMessage.error('操作失败: ' + (error.response?.data?.message || error.message || '网络错误'))
        }
      }
    }

    // 删除用户
    const handleDelete = () => {
      ElMessageBox.confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteUser(user.value.id)
          ElMessage.success('删除成功')
          router.push('/users')
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message || '网络错误'))
        }
      })
    }

    onMounted(() => {
      fetchUserDetail()
    })

    return {
      loading,
      user,
      formatDate,
      getRoleTagType,
      getRoleText,
      goBack,
      handleEdit,
      handleToggleStatus,
      handleDelete
    }
  }
}
</script>

<style scoped>
.page-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.detail-card {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-section h3 {
  margin: 0 0 15px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

:deep(.el-descriptions__title) {
  font-size: 14px;
  font-weight: 600;
}
</style>
