<template>
  <div class="user-management-container">
    <div class="page-title">
      <h2>用户管理</h2>
    </div>
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="card-header-left">
            <el-button type="primary" @click="handleAddUser">
              <el-icon><Plus /></el-icon>
              新增
            </el-button>
            <el-input
              v-model="searchKeyword"
              placeholder="搜索用户名/姓名/邮箱"
              style="width: 200px; margin-left: 10px;"
              clearable
              @keyup.enter="handleSearch"
            />
            <el-select
              v-model="roleFilter"
              placeholder="角色筛选"
              style="margin-left: 10px; width: 120px;"
              clearable
              @change="handleFilter"
            >
              <el-option label="管理员" value="ADMIN" />
              <el-option label="农场主" value="FARMER" />
              <el-option label="工作人员" value="STAFF" />
            </el-select>
            <el-select
              v-model="statusFilter"
              placeholder="状态筛选"
              style="margin-left: 10px; width: 120px;"
              clearable
              @change="handleFilter"
            >
              <el-option label="启用" :value="true" />
              <el-option label="禁用" :value="false" />
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
                  <el-dropdown-item>卡片视图</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </template>
      
      <el-table 
        :data="userList" 
        style="width: 100%" 
        v-loading="loading"
        row-key="id"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="phone" label="电话" width="150" />
        <el-table-column prop="role" label="角色" width="120">
          <template #default="{ row }">
            <el-tag :type="getRoleTagType(row.role)">
              {{ getRoleText(row.role) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status ? 'success' : 'danger'">
              {{ row.status ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="250">
          <template #default="{ row }">
            <el-button size="small" type="info" @click="handleViewDetail(row)">详情</el-button>
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
    
    <!-- 用户编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form 
        :model="userForm" 
        :rules="userRules" 
        ref="userFormRef"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" :disabled="dialogType === 'edit'" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="userForm.realName" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="userForm.phone" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="userForm.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="农场主" value="FARMER" />
            <el-option label="工作人员" value="STAFF" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="userForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="启用" :value="true" />
            <el-option label="禁用" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item :label="dialogType === 'add' ? '密码' : '新密码（可选）'" prop="password">
          <el-input v-model="userForm.password" type="password" show-password :placeholder="dialogType === 'add' ? '请输入密码' : '留空表示不修改密码'" />
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
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowRight, ArrowDown, Plus, Download, Search } from '@element-plus/icons-vue'
import { getUserList, createUser, updateUser, deleteUser } from '@/api/user'

export default {
  name: 'UserManagement',
  setup() {
    const router = useRouter()
    const userList = ref([])
    const loading = ref(false)
    const dialogVisible = ref(false)
    const dialogType = ref('add') // 'add' 或 'edit'
    const userFormRef = ref(null)
    const searchKeyword = ref('')
    const roleFilter = ref('')
    const statusFilter = ref('')

    // 动态密码验证规则
    const passwordRules = computed(() => [
      {
        required: dialogType.value === 'add',
        message: dialogType.value === 'add' ? '请输入密码' : '如需修改密码，请输入新密码',
        trigger: 'blur'
      },
      {
        min: 6,
        message: '密码长度不能少于6位',
        trigger: 'blur'
      }
    ])

    const userForm = reactive({
      id: null,
      username: '',
      realName: '',
      email: '',
      phone: '',
      role: '',
      status: true,
      password: ''
    })

    const userRules = reactive({
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度在3-20个字符之间', trigger: 'blur' }
      ],
      realName: [
        { required: true, message: '请输入真实姓名', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
      ],
      role: [
        { required: true, message: '请选择角色', trigger: 'change' }
      ],
      password: passwordRules
    })
    
    const pagination = reactive({
      currentPage: 1,
      pageSize: 10,
      total: 0
    })

    // 获取用户列表
    const fetchUserList = async () => {
      // 检查是否有token
      const token = localStorage.getItem('token')
      if (!token) {
        console.log('没有找到token，跳过获取用户列表')
        ElMessage.warning('请先登录')
        return
      }

      loading.value = true
      try {
        console.log('开始获取用户列表...')
        const params = {
          page: pagination.currentPage,
          size: pagination.pageSize
        }

        // 添加筛选参数
        if (searchKeyword.value.trim()) {
          // 搜索关键字传递给username参数，后端会处理多字段搜索
          params.username = searchKeyword.value.trim()
        }

        if (roleFilter.value) {
          params.role = roleFilter.value
        }

        if (statusFilter.value !== '') {
          params.status = statusFilter.value
        }

        console.log('请求参数:', params)
        const response = await getUserList(params)
        console.log('用户列表响应:', response)

        // 后端返回的数据格式：{ code, message, data: { content, page, size, totalElements, ... } }
        if (response.data && response.data.code === 200 && response.data.data) {
          const pageData = response.data.data
          userList.value = pageData.content || []
          pagination.total = pageData.totalElements || 0
          pagination.currentPage = pageData.page || 1
          pagination.pageSize = pageData.size || 10
          console.log('成功获取用户列表:', userList.value.length, '条记录')
        } else {
          userList.value = []
          pagination.total = 0
          console.log('用户列表响应格式不正确:', response.data)
        }
      } catch (error) {
        console.error('获取用户列表失败:', error)
        ElMessage.error('获取用户列表失败')
        userList.value = []
        pagination.total = 0
      } finally {
        loading.value = false
      }
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
    
    // 查看用户详情
    const handleViewDetail = (user) => {
      router.push(`/users/${user.id}`)
    }

    // 添加用户
    const handleAddUser = () => {
      dialogType.value = 'add'
      Object.assign(userForm, {
        id: null,
        username: '',
        realName: '',
        email: '',
        phone: '',
        role: '',
        status: true,
        password: ''
      })
      dialogVisible.value = true
    }
    
    // 编辑用户
    const handleEdit = (user) => {
      dialogType.value = 'edit'
      Object.assign(userForm, {
        ...user,
        password: '' // 编辑时清空密码字段
      })
      dialogVisible.value = true
    }
    
    // 删除用户
    const handleDelete = (id) => {
      ElMessageBox.confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteUser(id)
          ElMessage.success('删除成功')
          fetchUserList() // 重新获取列表
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败: ' + (error.message || '网络错误'))
        }
      })
    }
    
    // 提交表单
    const submitForm = async () => {
      if (!userFormRef.value) return

      await userFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            if (dialogType.value === 'add') {
              // 创建用户 - 传递所有必需字段
              const createData = {
                username: userForm.username,
                password: userForm.password,
                realName: userForm.realName,
                email: userForm.email || undefined,
                phone: userForm.phone || undefined,
                role: userForm.role,
                status: userForm.status
              }
              console.log('创建用户数据:', createData)
              await createUser(createData)
              ElMessage.success('用户添加成功')
            } else {
              // 更新用户 - 只传递有值的字段
              const updateData = {}
              if (userForm.password) updateData.password = userForm.password
              if (userForm.realName) updateData.realName = userForm.realName
              if (userForm.email !== undefined) updateData.email = userForm.email || null
              if (userForm.phone !== undefined) updateData.phone = userForm.phone || null
              if (userForm.role) updateData.role = userForm.role
              if (userForm.status !== undefined) updateData.status = userForm.status

              console.log('更新用户数据:', updateData)
              await updateUser(userForm.id, updateData)
              ElMessage.success('用户更新成功')
            }
            dialogVisible.value = false
            fetchUserList() // 重新获取列表
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
      fetchUserList()
    }

    // 筛选处理
    const handleFilter = () => {
      pagination.currentPage = 1 // 筛选时重置到第一页
      fetchUserList()
    }

    // 重置筛选
    const handleReset = () => {
      searchKeyword.value = ''
      roleFilter.value = ''
      statusFilter.value = ''
      pagination.currentPage = 1
      fetchUserList()
    }

    // 分页处理
    const handleSizeChange = (size) => {
      pagination.pageSize = size
      fetchUserList()
    }

    const handleCurrentChange = (page) => {
      pagination.currentPage = page
      fetchUserList()
    }
    
    const dialogTitle = computed(() => {
      return dialogType.value === 'add' ? '添加用户' : '编辑用户'
    })
    
    onMounted(() => {
      fetchUserList()
    })
    
    return {
      userList,
      loading,
      dialogVisible,
      dialogType,
      userForm,
      userFormRef,
      userRules,
      pagination,
      getRoleTagType,
      getRoleText,
      handleViewDetail,
      handleAddUser,
      handleEdit,
      handleDelete,
      submitForm,
      handleSizeChange,
      handleCurrentChange,
      dialogTitle,
      searchKeyword,
      roleFilter,
      statusFilter,
      handleSearch,
      handleFilter,
      handleReset,
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
  flex-wrap: wrap;
  gap: 10px;
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