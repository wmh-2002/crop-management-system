import { mockFarmlandService } from '@/mock/mockService'

export function getFarmlandList(params) {
  return mockFarmlandService.getFarmlandList(params)
}

export function getFarmlandById(id) {
  return mockFarmlandService.getFarmlandById(id)
}

export function createFarmland(data) {
  return mockFarmlandService.createFarmland(data)
}

export function updateFarmland(id, data) {
  return mockFarmlandService.updateFarmland(id, data)
}

export function deleteFarmland(id) {
  return mockFarmlandService.deleteFarmland(id)
}