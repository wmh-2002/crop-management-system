import request from '@/utils/request'

export function getPlantingRecordList(params) {
  return request({
    url: '/planting-records',
    method: 'get',
    params
  })
}

export function getPlantingRecordById(id) {
  return request({
    url: `/planting-records/${id}`,
    method: 'get'
  })
}

export function createPlantingRecord(data) {
  return request({
    url: '/planting-records',
    method: 'post',
    data
  })
}

export function updatePlantingRecord(id, data) {
  return request({
    url: `/planting-records/${id}`,
    method: 'put',
    data
  })
}

export function deletePlantingRecord(id) {
  return request({
    url: `/planting-records/${id}`,
    method: 'delete'
  })
}

export function getMyPlantingRecords() {
  return request({
    url: '/planting-records/my',
    method: 'get'
  })
}

export function getPlantingRecordsByPlan(planId) {
  return request({
    url: `/planting-records/plan/${planId}`,
    method: 'get'
  })
}

export function getPlantingRecordsByOperationType(operationType) {
  return request({
    url: `/planting-records/operation-type/${operationType}`,
    method: 'get'
  })
}

export function getPlantingRecordsByOperator(operatorId) {
  return request({
    url: `/planting-records/operator/${operatorId}`,
    method: 'get'
  })
}

export function getPlantingRecordsByPlanAndOperationType(planId, operationType) {
  return request({
    url: `/planting-records/plan/${planId}/operation-type/${operationType}`,
    method: 'get'
  })
}

export function getPlantingRecordsByDateRange(startDate, endDate) {
  return request({
    url: '/planting-records/date-range',
    method: 'get',
    params: {
      startDate,
      endDate
    }
  })
}