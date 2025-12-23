<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="login-title">农业管理系统</h2>
      <el-form 
        :model="loginForm" 
        :rules="loginRules" 
        ref="loginFormRef"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入用户名"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="Lock"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleLogin" 
            :loading="loading"
            class="login-button"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/auth'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const loading = ref(false)
    const loginFormRef = ref(null)

    const loginForm = reactive({
      username: 'admin',
      password: 'admin123'
    })

    const loginRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
      ]
    }

    const handleLogin = async () => {
      if (!loginFormRef.value) return
      
      await loginFormRef.value.validate((valid) => {
        if (valid) {
          loading.value = true
          login(loginForm)
            .then(response => {
              // 检查响应格式并处理
              if (response.data && response.data.code === 200) {
                // 存储token
                console.log(response.data.data.data.token)
                localStorage.setItem('token', response.data.data.data.token)
                localStorage.setItem('userInfo', JSON.stringify(response.data.data))
                
                ElMessage.success('登录成功')
                router.push('/dashboard')
              } else {
                ElMessage.error(response.data?.message || '登录失败')
              }
            })
            .catch(error => {
              console.error('登录失败:', error)
              ElMessage.error('登录失败: ' + (error.message || '网络错误'))
            })
            .finally(() => {
              loading.value = false
            })
        }
      })
    }

    return {
      loginForm,
      loginRules,
      loginFormRef,
      loading,
      handleLogin
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.login-card {
  width: 400px;
  padding: 20px;
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
}

.login-form {
  margin-top: 20px;
}

.login-button {
  width: 100%;
}
</style>