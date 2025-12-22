import { mockGrowthMonitoringService } from '@/mock/mockService'

export function getGrowthMonitoringList(params) {
  return mockGrowthMonitoringService.getGrowthMonitoringList(params)
}

export function getGrowthMonitoringById(id) {
  return mockGrowthMonitoringService.getGrowthMonitoringById(id)
}

export function createGrowthMonitoring(data) {
  return mockGrowthMonitoringService.createGrowthMonitoring(data)
}

export function updateGrowthMonitoring(id, data) {
  return mockGrowthMonitoringService.updateGrowthMonitoring(id, data)
}

export function deleteGrowthMonitoring(id) {
  return mockGrowthMonitoringService.deleteGrowthMonitoring(id)
}