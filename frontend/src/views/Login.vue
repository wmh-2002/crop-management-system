<template>
  <div class="login-container">
    <!-- 背景装饰元素 -->
    <div class="background-decoration">
      <div class="floating-leaves">
        <div class="leaf leaf-1">🌱</div>
        <div class="leaf leaf-2">🌾</div>
        <div class="leaf leaf-3">🌽</div>
        <div class="leaf leaf-4">🍅</div>
        <div class="leaf leaf-5">🥕</div>
      </div>
      <div class="sun-light"></div>
    </div>

    <div class="login-wrapper">
      <!-- 左侧介绍区域 -->
      <div class="login-intro">
        <div class="intro-content">
          <h1 class="intro-title">
            <span class="title-icon">🌾</span>
            农作物管理系统
          </h1>
          <p class="intro-description">
            智能化的农业生产管理解决方案<br>
            为您的农田带来科技的力量
          </p>
          <div class="feature-list">
            <div class="feature-item">
              <span class="feature-icon">📊</span>
              <span>数据驱动决策</span>
            </div>
            <div class="feature-item">
              <span class="feature-icon">🌱</span>
              <span>生长周期跟踪</span>
            </div>
            <div class="feature-item">
              <span class="feature-icon">📈</span>
              <span>产量预测分析</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧登录表单 -->
      <div class="login-form-section">
        <div class="login-card">
          <div class="login-header">
            <h2 class="login-title">
              <span class="welcome-icon">👋</span>
              欢迎登录
            </h2>
            <p class="login-subtitle">请输入您的账号和密码</p>
          </div>

          <el-form
            :model="loginForm"
            :rules="loginRules"
            ref="loginFormRef"
            class="login-form"
          >
            <el-form-item prop="username">
              <div class="input-wrapper">
                <el-input
                  v-model="loginForm.username"
                  placeholder="请输入用户名"
                  prefix-icon="User"
                  size="large"
                  class="login-input"
                />
              </div>
            </el-form-item>

            <el-form-item prop="password">
              <div class="input-wrapper">
                <el-input
                  v-model="loginForm.password"
                  type="password"
                  placeholder="请输入密码"
                  prefix-icon="Lock"
                  size="large"
                  class="login-input"
                  @keyup.enter="handleLogin"
                />
              </div>
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                @click="handleLogin"
                :loading="loading"
                class="login-button"
                size="large"
              >
                <span class="button-text">立即登录</span>
                <span class="button-icon">🚀</span>
              </el-button>
            </el-form-item>
          </el-form>

          <div class="login-footer">
            <p class="footer-text">
              选择农业，选择未来
            </p>
          </div>
        </div>
      </div>
    </div>
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
  position: relative;
  min-height: 100vh;
  background: linear-gradient(135deg, #4ade80 0%, #22c55e 50%, #16a34a 100%);
  overflow: hidden;
}

/* 背景装饰元素 */
.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.floating-leaves {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.leaf {
  position: absolute;
  font-size: 2rem;
  animation: float 8s ease-in-out infinite;
  opacity: 0.7;
}

.leaf-1 {
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.leaf-2 {
  top: 20%;
  right: 15%;
  animation-delay: 2s;
}

.leaf-3 {
  top: 60%;
  left: 20%;
  animation-delay: 4s;
}

.leaf-4 {
  bottom: 20%;
  right: 10%;
  animation-delay: 6s;
}

.leaf-5 {
  bottom: 10%;
  left: 30%;
  animation-delay: 1s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(10deg);
  }
}

.sun-light {
  position: absolute;
  top: -50%;
  right: -20%;
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  animation: glow 4s ease-in-out infinite alternate;
}

@keyframes glow {
  0% {
    opacity: 0.5;
    transform: scale(1);
  }
  100% {
    opacity: 0.8;
    transform: scale(1.1);
  }
}

.login-wrapper {
  display: flex;
  min-height: 100vh;
  position: relative;
  z-index: 1;
}

/* 左侧介绍区域 */
.login-intro {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  color: white;
}

.intro-content {
  max-width: 500px;
  text-align: center;
}

.intro-title {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.title-icon {
  font-size: 3.5rem;
  animation: bounce 2s ease-in-out infinite;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}

.intro-description {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  opacity: 0.9;
  line-height: 1.6;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.feature-item:hover {
  transform: translateX(5px);
  background: rgba(255, 255, 255, 0.15);
}

.feature-icon {
  font-size: 1.5rem;
}

/* 右侧登录表单区域 */
.login-form-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  min-height: 100vh;
}

.login-card {
  width: 100%;
  max-width: 400px;
  background: white;
  border-radius: 20px;
  padding: 2.5rem;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.8);
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.login-title {
  font-size: 2rem;
  font-weight: 600;
  color: #16a34a;
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.welcome-icon {
  font-size: 2rem;
  animation: wave 2s ease-in-out infinite;
}

@keyframes wave {
  0%, 100% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(5deg);
  }
  75% {
    transform: rotate(-5deg);
  }
}

.login-subtitle {
  color: #6b7280;
  font-size: 0.95rem;
}

.login-form {
  margin-top: 1rem;
}

.input-wrapper {
  position: relative;
  margin-bottom: 1rem;
  width: 100%;
}

.login-input {
  width: 100% !important;
  border-radius: 12px;
  border: 2px solid #e5e7eb;
  transition: all 0.3s ease;
}

.login-input :deep(.el-input__inner) {
  width: 100%;
  border-radius: 12px;
}

.login-input:hover {
  border-color: #16a34a;
}

.login-input:focus {
  border-color: #16a34a;
  box-shadow: 0 0 0 3px rgba(22, 163, 74, 0.1);
}

.login-button {
  width: 100%;
  height: 50px;
  border-radius: 12px;
  background: linear-gradient(135deg, #16a34a 0%, #22c55e 100%);
  border: none;
  font-size: 1.1rem;
  font-weight: 600;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(22, 163, 74, 0.3);
}

.login-button:active {
  transform: translateY(0);
}

.button-text {
  margin-right: 0.5rem;
}

.button-icon {
  font-size: 1.2rem;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

.login-footer {
  margin-top: 2rem;
  text-align: center;
}

.footer-text {
  color: #6b7280;
  font-size: 0.9rem;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-wrapper {
    flex-direction: column;
  }

  .login-intro {
    padding: 1rem;
  }

  .intro-title {
    font-size: 2rem;
  }

  .intro-description {
    font-size: 1rem;
  }

  .feature-list {
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: center;
  }

  .feature-item {
    flex: 1;
    min-width: 45%;
    text-align: center;
  }

  .login-form-section {
    padding: 1rem;
    background: white;
  }

  .login-card {
    padding: 2rem;
    margin: 0;
  }
}
</style>