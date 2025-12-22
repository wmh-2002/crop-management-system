// Mock服务
import { 
  mockLogin,
  mockGetUserList,
  mockGetCropList,
  mockGetFarmlandList,
  mockGetPlantingPlanList,
  mockGetGrowthMonitoringList,
  mockGetReportList,
  mockGetNotificationList
} from './mockData'

// 模拟HTTP响应
const mockResponse = (data) => {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve({
        data: data,
        status: 200,
        statusText: 'OK'
      })
    }, 300) // 模拟网络延迟
  })
}

// Mock API服务
export const mockAuthService = {
  login: (data) => mockResponse(mockLogin(data)),
  register: (data) => mockResponse({ code: 200, message: '注册成功', data })
}

export const mockUserService = {
  getUserList: (params) => mockResponse(mockGetUserList(params).data),
  getUserById: (id) => {
    // 查找用户
    const user = [
      { id: 1, username: 'admin', realName: '管理员', email: 'admin@example.com', phone: '13800138001', role: 'ADMIN', status: 1 },
      { id: 2, username: 'farmer1', realName: '张三', email: 'zhangsan@example.com', phone: '13800138002', role: 'FARMER', status: 1 },
      { id: 3, username: 'staff1', realName: '李四', email: 'lisi@example.com', phone: '13800138003', role: 'STAFF', status: 1 }
    ].find(u => u.id === parseInt(id))
    return mockResponse(user || null)
  },
  createUser: (data) => mockResponse(data),
  updateUser: (id, data) => mockResponse({ ...data, id }),
  deleteUser: (id) => mockResponse({ id })
}

export const mockCropService = {
  getCropList: (params) => mockResponse(mockGetCropList(params).data),
  getCropById: (id) => {
    // 查找作物
    const crop = [
      { id: 1, name: '番茄', variety: '樱桃番茄', plantingSeason: '春夏季', growthPeriod: 90, expectedYield: 5000.00 },
      { id: 2, name: '小麦', variety: '冬小麦', plantingSeason: '秋季', growthPeriod: 240, expectedYield: 6000.00 },
      { id: 3, name: '玉米', variety: '甜玉米', plantingSeason: '春夏季', growthPeriod: 120, expectedYield: 8000.00 }
    ].find(c => c.id === parseInt(id))
    return mockResponse(crop || null)
  },
  createCrop: (data) => mockResponse(data),
  updateCrop: (id, data) => mockResponse({ ...data, id }),
  deleteCrop: (id) => mockResponse({ id })
}

export const mockFarmlandService = {
  getFarmlandList: (params) => mockResponse(mockGetFarmlandList(params).data),
  getFarmlandById: (id) => {
    // 查找农田
    const farmland = [
      { id: 1, name: '一号农田', area: 5000.00, location: '东区第一排', status: 'AVAILABLE' },
      { id: 2, name: '二号农田', area: 8000.00, location: '西区第二排', status: 'PLANTED' },
      { id: 3, name: '三号农田', area: 3000.00, location: '南区第三排', status: 'MAINTENANCE' }
    ].find(f => f.id === parseInt(id))
    return mockResponse(farmland || null)
  },
  createFarmland: (data) => mockResponse(data),
  updateFarmland: (id, data) => mockResponse({ ...data, id }),
  deleteFarmland: (id) => mockResponse({ id })
}

export const mockPlantingPlanService = {
  getPlantingPlanList: (params) => mockResponse(mockGetPlantingPlanList(params).data),
  getPlantingPlanById: (id) => {
    // 查找种植计划
    const plan = [
      { 
        id: 1, 
        planName: '春季番茄种植计划', 
        farmland: { id: 1, name: '一号农田' },
        crop: { id: 1, name: '番茄' },
        plannedStartDate: '2023-03-01',
        plannedEndDate: '2023-05-30',
        expectedHarvestDate: '2023-06-15',
        status: 'IN_PROGRESS'
      },
      { 
        id: 2, 
        planName: '秋小麦种植计划', 
        farmland: { id: 2, name: '二号农田' },
        crop: { id: 2, name: '小麦' },
        plannedStartDate: '2023-10-15',
        plannedEndDate: '2024-06-15',
        expectedHarvestDate: '2024-06-20',
        status: 'PLANNED'
      }
    ].find(p => p.id === parseInt(id))
    return mockResponse(plan || null)
  },
  createPlantingPlan: (data) => mockResponse(data),
  updatePlantingPlan: (id, data) => mockResponse({ ...data, id }),
  deletePlantingPlan: (id) => mockResponse({ id })
}

export const mockGrowthMonitoringService = {
  getGrowthMonitoringList: (params) => mockResponse(mockGetGrowthMonitoringList(params).data),
  getGrowthMonitoringById: (id) => {
    // 查找生长监控
    const monitoring = [
      { 
        id: 1, 
        plan: { id: 1, planName: '春季番茄种植计划' },
        monitoringDate: '2023-03-15',
        heightMeasurement: 15.50,
        widthMeasurement: 10.20,
        healthStatus: 'GOOD',
        soilMoisture: 65.00,
        temperature: 22.50
      },
      { 
        id: 2, 
        plan: { id: 1, planName: '春季番茄种植计划' },
        monitoringDate: '2023-04-01',
        heightMeasurement: 35.20,
        widthMeasurement: 25.80,
        healthStatus: 'EXCELLENT',
        soilMoisture: 70.00,
        temperature: 25.00
      }
    ].find(m => m.id === parseInt(id))
    return mockResponse(monitoring || null)
  },
  createGrowthMonitoring: (data) => mockResponse(data),
  updateGrowthMonitoring: (id, data) => mockResponse({ ...data, id }),
  deleteGrowthMonitoring: (id) => mockResponse({ id })
}

export const mockReportService = {
  getReportList: (params) => mockResponse(mockGetReportList(params).data),
  getReportById: (id) => {
    // 查找报告
    const report = [
      { id: 1, title: '第一季度作物生长分析报告', reportType: 'CROP_GROWTH', startDate: '2023-01-01', endDate: '2023-03-31' },
      { id: 2, title: '农田利用率分析报告', reportType: 'FIELD_UTILIZATION', startDate: '2023-01-01', endDate: '2023-06-30' }
    ].find(r => r.id === parseInt(id))
    return mockResponse(report || null)
  },
  createReport: (data) => mockResponse(data),
  updateReport: (id, data) => mockResponse({ ...data, id }),
  deleteReport: (id) => mockResponse({ id })
}

export const mockNotificationService = {
  getNotificationList: (params) => mockResponse(mockGetNotificationList(params).data),
  getNotificationById: (id) => {
    // 查找通知
    const notification = [
      { id: 1, title: '番茄需要浇水', content: '根据土壤湿度监测，一号农田的番茄需要浇水', type: 'WATERING', priority: 'MEDIUM', status: 'READ' },
      { id: 2, title: '施肥提醒', content: '二号农田的小麦需要追肥', type: 'FERTILIZING', priority: 'HIGH', status: 'PENDING' }
    ].find(n => n.id === parseInt(id))
    return mockResponse(notification || null)
  },
  getPendingNotifications: () => mockResponse([
    { id: 2, title: '施肥提醒', content: '二号农田的小麦需要追肥', type: 'FERTILIZING', priority: 'HIGH', status: 'PENDING' }
  ]),
  createNotification: (data) => mockResponse(data),
  updateNotification: (id, data) => mockResponse({ ...data, id }),
  deleteNotification: (id) => mockResponse({ id })
}