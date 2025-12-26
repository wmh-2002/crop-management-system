import request from '@/utils/request'

export function getGrowthMonitoringList(params) {
  return request({
    url: '/growth-monitoring',
    method: 'get',
    params
  })
}

export function getGrowthMonitoringById(id) {
  return request({
    url: `/growth-monitoring/${id}`,
    method: 'get'
  })
}

export function createGrowthMonitoring(data) {
  return request({
    url: '/growth-monitoring',
    method: 'post',
    data
  })
}

export function updateGrowthMonitoring(id, data) {
  return request({
    url: `/growth-monitoring/${id}`,
    method: 'put',
    data
  })
}

export function deleteGrowthMonitoring(id) {
  return request({
    url: `/growth-monitoring/${id}`,
    method: 'delete'
  })
}

export function getMyGrowthMonitoring() {
  return request({
    url: '/growth-monitoring/my',
    method: 'get'
  })
}

export function getGrowthMonitoringByPlan(planId) {
  return request({
    url: `/growth-monitoring/plan/${planId}`,
    method: 'get'
  })
}

export function getGrowthMonitoringByPlanAndDateRange(planId, startDate, endDate) {
  return request({
    url: `/growth-monitoring/plan/${planId}/date-range`,
    method: 'get',
    params: {
      startDate,
      endDate
    }
  })
}

export function getGrowthMonitoringByPlanAndHealthStatus(planId, healthStatus) {
  return request({
    url: `/growth-monitoring/plan/${planId}/health-status/${healthStatus}`,
    method: 'get'
  })
}

export function getGrowthMonitoringByHealthStatus(healthStatus) {
  return request({
    url: `/growth-monitoring/health-status/${healthStatus}`,
    method: 'get'
  })
}

export function getGrowthMonitoringByDateRange(startDate, endDate) {
  return request({
    url: '/growth-monitoring/date-range',
    method: 'get',
    params: {
      startDate,
      endDate
    }
  })
}

export function getGrowthMonitoringByTemperatureRange(minTemp, maxTemp) {
  return request({
    url: '/growth-monitoring/temperature-range',
    method: 'get',
    params: {
      minTemp,
      maxTemp
    }
  })
}

export function getGrowthMonitoringBySoilMoistureRange(minMoisture, maxMoisture) {
  return request({
    url: '/growth-monitoring/soil-moisture-range',
    method: 'get',
    params: {
      minMoisture,
      maxMoisture
    }
  })
}

export function getGrowthMonitoringByPhRange(minPh, maxPh) {
  return request({
    url: '/growth-monitoring/ph-range',
    method: 'get',
    params: {
      minPh,
      maxPh
    }
  })
}

export function collectGrowthMonitoring(planId) {
  return request({
    url: `/growth-monitoring/collect/${planId}`,
    method: 'post'
  })
}

export function collectAllGrowthMonitoringData() {
  return request({
    url: '/growth-monitoring/collect',
    method: 'post'
  })
}