import { mockCropService } from '@/mock/mockService'

export function getCropList(params) {
  return mockCropService.getCropList(params)
}

export function getCropById(id) {
  return mockCropService.getCropById(id)
}

export function createCrop(data) {
  return mockCropService.createCrop(data)
}

export function updateCrop(id, data) {
  return mockCropService.updateCrop(id, data)
}

export function deleteCrop(id) {
  return mockCropService.deleteCrop(id)
}