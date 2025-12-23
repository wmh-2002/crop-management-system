import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:8080/api', // 本地后端API地址
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    const token = localStorage.getItem('token')
    console.log('发送请求:', config.url, 'Token:', token ? '存在' : '不存在')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
      console.log('添加Authorization头:', `Bearer ${token}`)
    } else {
      console.log('没有找到token，请求将不携带认证信息')
    }
    config.headers['Content-Type'] = 'application/json'
    return config
  },
  error => {
    // 对请求错误做些什么
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 对响应数据做点什么
    return response
  },
  error => {
    // 对响应错误做点什么
    if (error.response && error.response.status === 401) {
      // token过期或未授权，跳转到登录页
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      router.push('/login')
      ElMessage.error('登录已过期，请重新登录')
    } else if (error.response && error.response.data && error.response.data.message) {
      ElMessage.error(error.response.data.message)
    } else {
      ElMessage.error('网络错误，请稍后重试')
    }
    return Promise.reject(error)
  }
)

export default service