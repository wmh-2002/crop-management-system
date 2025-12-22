import { mockPlantingPlanService } from '@/mock/mockService'

export function getPlantingPlanList(params) {
  return mockPlantingPlanService.getPlantingPlanList(params)
}

export function getPlantingPlanById(id) {
  return mockPlantingPlanService.getPlantingPlanById(id)
}

export function createPlantingPlan(data) {
  return mockPlantingPlanService.createPlantingPlan(data)
}

export function updatePlantingPlan(id, data) {
  return mockPlantingPlanService.updatePlantingPlan(id, data)
}

export function deletePlantingPlan(id) {
  return mockPlantingPlanService.deletePlantingPlan(id)
}