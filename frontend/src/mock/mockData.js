// mock数据服务

// 模拟用户数据
const users = [
  {
    id: 1,
    username: 'admin',
    password: 'admin123', // 仅为演示，实际项目中密码不会明文存储
    realName: '管理员',
    email: 'admin@example.com',
    phone: '13800138001',
    role: 'ADMIN',
    status: 1
  },
  {
    id: 2,
    username: 'farmer1',
    password: 'farmer123',
    realName: '张三',
    email: 'zhangsan@example.com',
    phone: '13800138002',
    role: 'FARMER',
    status: 1
  },
  {
    id: 3,
    username: 'staff1',
    password: 'staff123',
    realName: '李四',
    email: 'lisi@example.com',
    phone: '13800138003',
    role: 'STAFF',
    status: 1
  }
]

// 模拟作物数据
const crops = [
  {
    id: 1,
    name: '番茄',
    variety: '樱桃番茄',
    plantingSeason: '春夏季',
    growthPeriod: 90,
    expectedYield: 5000.00,
    waterNeeds: '中等',
    fertilizerNeeds: '高',
    diseaseInfo: '易感染晚疫病',
    description: '樱桃番茄，甜度高，营养价值丰富',
    createdAt: '2023-01-15 10:30:00',
    updatedAt: '2023-01-15 10:30:00'
  },
  {
    id: 2,
    name: '小麦',
    variety: '冬小麦',
    plantingSeason: '秋季',
    growthPeriod: 240,
    expectedYield: 6000.00,
    waterNeeds: '适中',
    fertilizerNeeds: '中等',
    diseaseInfo: '易感染锈病',
    description: '冬小麦，耐寒性强',
    createdAt: '2023-01-16 09:15:00',
    updatedAt: '2023-01-16 09:15:00'
  },
  {
    id: 3,
    name: '玉米',
    variety: '甜玉米',
    plantingSeason: '春夏季',
    growthPeriod: 120,
    expectedYield: 8000.00,
    waterNeeds: '高',
    fertilizerNeeds: '高',
    diseaseInfo: '易感染玉米螟',
    description: '甜玉米，口感佳，营养丰富',
    createdAt: '2023-02-20 14:20:00',
    updatedAt: '2023-02-20 14:20:00'
  }
]

// 模拟农田数据
const farmlands = [
  {
    id: 1,
    name: '一号农田',
    area: 5000.00,
    location: '东区第一排',
    status: 'AVAILABLE',
    description: '土壤肥沃，适合种植多种作物',
    createdAt: '2023-01-10 08:30:00',
    updatedAt: '2023-01-10 08:30:00'
  },
  {
    id: 2,
    name: '二号农田',
    area: 8000.00,
    location: '西区第二排',
    status: 'PLANTED',
    description: '面积较大，适合大面积种植',
    createdAt: '2023-01-11 09:00:00',
    updatedAt: '2023-01-11 09:00:00'
  },
  {
    id: 3,
    name: '三号农田',
    area: 3000.00,
    location: '南区第三排',
    status: 'MAINTENANCE',
    description: '需要进行土壤改良',
    createdAt: '2023-01-12 10:15:00',
    updatedAt: '2023-01-12 10:15:00'
  }
]

// 模拟种植计划数据
const plantingPlans = [
  {
    id: 1,
    farmland: {
      id: 1,
      name: '一号农田'
    },
    crop: {
      id: 1,
      name: '番茄'
    },
    planName: '春季番茄种植计划',
    plannedStartDate: '2023-03-01',
    plannedEndDate: '2023-05-30',
    expectedHarvestDate: '2023-06-15',
    sowingDensity: '每平方米10株',
    notes: '注意病虫害防治',
    status: 'IN_PROGRESS',
    createdBy: {
      id: 2,
      username: 'farmer1',
      realName: '张三'
    },
    createdAt: '2023-02-28 10:00:00',
    updatedAt: '2023-02-28 10:00:00'
  },
  {
    id: 2,
    farmland: {
      id: 2,
      name: '二号农田'
    },
    crop: {
      id: 2,
      name: '小麦'
    },
    planName: '秋小麦种植计划',
    plannedStartDate: '2023-10-15',
    plannedEndDate: '2024-06-15',
    expectedHarvestDate: '2024-06-20',
    sowingDensity: '每亩15公斤',
    notes: '注意冬季防冻',
    status: 'PLANNED',
    createdBy: {
      id: 2,
      username: 'farmer1',
      realName: '张三'
    },
    createdAt: '2023-09-20 14:30:00',
    updatedAt: '2023-09-20 14:30:00'
  }
]

// 模拟生长监控数据
const growthMonitorings = [
  {
    id: 1,
    plan: {
      id: 1,
      planName: '春季番茄种植计划'
    },
    monitoringDate: '2023-03-15',
    heightMeasurement: 15.50,
    widthMeasurement: 10.20,
    healthStatus: 'GOOD',
    soilMoisture: 65.00,
    temperature: 22.50,
    humidity: 70.00,
    lightIntensity: 8000,
    phLevel: 6.50,
    notes: '生长正常',
    photoUrl: null,
    createdBy: {
      id: 3,
      username: 'staff1',
      realName: '李四'
    },
    createdAt: '2023-03-15 09:00:00',
    updatedAt: '2023-03-15 09:00:00'
  },
  {
    id: 2,
    plan: {
      id: 1,
      planName: '春季番茄种植计划'
    },
    monitoringDate: '2023-04-01',
    heightMeasurement: 35.20,
    widthMeasurement: 25.80,
    healthStatus: 'EXCELLENT',
    soilMoisture: 70.00,
    temperature: 25.00,
    humidity: 65.00,
    lightIntensity: 9000,
    phLevel: 6.70,
    notes: '生长良好，需注意通风',
    photoUrl: null,
    createdBy: {
      id: 3,
      username: 'staff1',
      realName: '李四'
    },
    createdAt: '2023-04-01 10:30:00',
    updatedAt: '2023-04-01 10:30:00'
  }
]

// 模拟报告数据
const reports = [
  {
    id: 1,
    title: '第一季度作物生长分析报告',
    reportType: 'CROP_GROWTH',
    startDate: '2023-01-01',
    endDate: '2023-03-31',
    content: '本季度作物生长情况良好，番茄产量较去年同期增长15%，小麦病虫害得到有效控制。',
    chartData: JSON.stringify({
      labels: ['番茄', '小麦', '玉米'],
      data: [5200, 5800, 7800]
    }),
    generatedBy: {
      id: 2,
      username: 'farmer1',
      realName: '张三'
    },
    createdAt: '2023-04-01 14:00:00',
    updatedAt: '2023-04-01 14:00:00'
  },
  {
    id: 2,
    title: '农田利用率分析报告',
    reportType: 'FIELD_UTILIZATION',
    startDate: '2023-01-01',
    endDate: '2023-06-30',
    content: '上半年农田利用率达到85%，其中一号农田利用率最高，达到95%。',
    chartData: JSON.stringify({
      labels: ['一号农田', '二号农田', '三号农田'],
      data: [95, 80, 60]
    }),
    generatedBy: {
      id: 2,
      username: 'farmer1',
      realName: '张三'
    },
    createdAt: '2023-07-01 10:00:00',
    updatedAt: '2023-07-01 10:00:00'
  }
]

// 模拟通知数据
const notifications = [
  {
    id: 1,
    title: '番茄需要浇水',
    content: '根据土壤湿度监测，一号农田的番茄需要浇水',
    type: 'WATERING',
    priority: 'MEDIUM',
    scheduledTime: '2023-04-05 10:00:00',
    sentTime: '2023-04-05 10:00:00',
    status: 'READ',
    recipientIds: '[2,3]',
    plan: {
      id: 1,
      planName: '春季番茄种植计划'
    },
    createdAt: '2023-04-04 16:00:00',
    updatedAt: '2023-04-05 10:00:00'
  },
  {
    id: 2,
    title: '施肥提醒',
    content: '二号农田的小麦需要追肥',
    type: 'FERTILIZING',
    priority: 'HIGH',
    scheduledTime: '2023-11-20 09:00:00',
    sentTime: null,
    status: 'PENDING',
    recipientIds: '[2,3]',
    plan: {
      id: 2,
      planName: '秋小麦种植计划'
    },
    createdAt: '2023-11-15 14:00:00',
    updatedAt: '2023-11-15 14:00:00'
  }
]

// 模拟登录响应
export const mockLogin = (params) => {
  const { username, password } = params
  const user = users.find(u => u.username === username && u.password === password)
  
  if (user) {
    return {
      code: 200,
      message: '登录成功',
      data: {
        token: `mock-token-${user.id}`,
        id: user.id,
        username: user.username,
        realName: user.realName,
        role: user.role
      }
    }
  } else {
    return {
      code: 401,
      message: '用户名或密码错误',
      data: null
    }
  }
}

// 模拟获取用户列表
export const mockGetUserList = (params) => {
  const { page = 1, size = 10 } = params || {}
  const start = (page - 1) * size
  const end = start + size
  const paginatedUsers = users.slice(start, end)
  
  return {
    code: 200,
    message: '获取成功',
    data: paginatedUsers,
    total: users.length
  }
}

// 模拟获取作物列表
export const mockGetCropList = (params) => {
  const { page = 1, size = 10 } = params || {}
  const start = (page - 1) * size
  const end = start + size
  const paginatedCrops = crops.slice(start, end)
  
  return {
    code: 200,
    message: '获取成功',
    data: paginatedCrops,
    total: crops.length
  }
}

// 模拟获取农田列表
export const mockGetFarmlandList = (params) => {
  const { page = 1, size = 10 } = params || {}
  const start = (page - 1) * size
  const end = start + size
  const paginatedFarmlands = farmlands.slice(start, end)
  
  return {
    code: 200,
    message: '获取成功',
    data: paginatedFarmlands,
    total: farmlands.length
  }
}

// 模拟获取种植计划列表
export const mockGetPlantingPlanList = (params) => {
  const { page = 1, size = 10 } = params || {}
  const start = (page - 1) * size
  const end = start + size
  const paginatedPlans = plantingPlans.slice(start, end)
  
  return {
    code: 200,
    message: '获取成功',
    data: paginatedPlans,
    total: plantingPlans.length
  }
}

// 模拟获取生长监控列表
export const mockGetGrowthMonitoringList = (params) => {
  const { page = 1, size = 10 } = params || {}
  const start = (page - 1) * size
  const end = start + size
  const paginatedMonitorings = growthMonitorings.slice(start, end)
  
  return {
    code: 200,
    message: '获取成功',
    data: paginatedMonitorings,
    total: growthMonitorings.length
  }
}

// 模拟获取报告列表
export const mockGetReportList = (params) => {
  const { page = 1, size = 10 } = params || {}
  const start = (page - 1) * size
  const end = start + size
  const paginatedReports = reports.slice(start, end)
  
  return {
    code: 200,
    message: '获取成功',
    data: paginatedReports,
    total: reports.length
  }
}

// 模拟获取通知列表
export const mockGetNotificationList = (params) => {
  const { page = 1, size = 10 } = params || {}
  const start = (page - 1) * size
  const end = start + size
  const paginatedNotifications = notifications.slice(start, end)
  
  return {
    code: 200,
    message: '获取成功',
    data: paginatedNotifications,
    total: notifications.length
  }
}