import request from '@/utils/request'

export function getPlantingPlanList(params) {
  return request({
    url: '/planting-plans',
    method: 'get',
    params
  })
}

export function getPlantingPlanById(id) {
  return request({
    url: `/planting-plans/${id}`,
    method: 'get'
  })
}

export function createPlantingPlan(data) {
  return request({
    url: '/planting-plans',
    method: 'post',
    data
  })
}

export function updatePlantingPlan(id, data) {
  return request({
    url: `/planting-plans/${id}`,
    method: 'put',
    data
  })
}

export function deletePlantingPlan(id) {
  return request({
    url: `/planting-plans/${id}`,
    method: 'delete'
  })
}

export function getMyPlantingPlans() {
  return request({
    url: '/planting-plans/my',
    method: 'get'
  })
}

export function getPlantingPlansByFarmland(farmlandId) {
  return request({
    url: `/planting-plans/farmland/${farmlandId}`,
    method: 'get'
  })
}

export function getPlantingPlansByCrop(cropId) {
  return request({
    url: `/planting-plans/crop/${cropId}`,
    method: 'get'
  })
}

export function getPlantingPlansByStatus(status) {
  return request({
    url: `/planting-plans/status/${status}`,
    method: 'get'
  })
}