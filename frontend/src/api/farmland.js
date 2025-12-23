import request from '@/utils/request'

export function getFarmlandList(params) {
  return request({
    url: '/farmlands',
    method: 'get',
    params
  })
}

export function getFarmlandById(id) {
  return request({
    url: `/farmlands/${id}`,
    method: 'get'
  })
}

export function createFarmland(data) {
  return request({
    url: '/farmlands',
    method: 'post',
    data
  })
}

export function updateFarmland(id, data) {
  return request({
    url: `/farmlands/${id}`,
    method: 'put',
    data
  })
}

export function deleteFarmland(id) {
  return request({
    url: `/farmlands/${id}`,
    method: 'delete'
  })
}

export function getMyFarmlands() {
  return request({
    url: '/farmlands/my',
    method: 'get'
  })
}

export function getFarmlandsByStatus(status) {
  return request({
    url: `/farmlands/status/${status}`,
    method: 'get'
  })
}