import request from '@/utils/request'

// 获取作物生长分析数据
export function getCropGrowthAnalysis() {
  return request({
    url: '/analysis/crop-growth',
    method: 'get'
  })
}

// 获取农田利用率分析数据
export function getFieldUtilizationAnalysis() {
  return request({
    url: '/analysis/field-utilization',
    method: 'get'
  })
}

// 获取综合统计数据
export function getComprehensiveStatistics() {
  return request({
    url: '/analysis/comprehensive',
    method: 'get'
  })
}

// 获取所有分析数据
export function getAllAnalysisData() {
  return request({
    url: '/analysis/all',
    method: 'get'
  })
}
