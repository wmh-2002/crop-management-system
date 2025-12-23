import request from '@/utils/request'

export function getCropList(params) {
  return request({
    url: '/crops',
    method: 'get',
    params
  })
}

export function getCropById(id) {
  return request({
    url: `/crops/${id}`,
    method: 'get'
  })
}

export function createCrop(data) {
  return request({
    url: '/crops',
    method: 'post',
    data
  })
}

export function updateCrop(id, data) {
  return request({
    url: `/crops/${id}`,
    method: 'put',
    data
  })
}

export function deleteCrop(id) {
  return request({
    url: `/crops/${id}`,
    method: 'delete'
  })
}

export function getAllCrops() {
  return request({
    url: '/crops/all',
    method: 'get'
  })
}

export function getCropsBySeason(season) {
  return request({
    url: `/crops/season/${season}`,
    method: 'get'
  })
}

export function getCropsByGrowthPeriod(params) {
  return request({
    url: '/crops/growth-period',
    method: 'get',
    params
  })
}