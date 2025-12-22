import { mockUserService } from '@/mock/mockService'

export function getUserList(params) {
  return mockUserService.getUserList(params)
}

export function getUserById(id) {
  return mockUserService.getUserById(id)
}

export function createUser(data) {
  return mockUserService.createUser(data)
}

export function updateUser(id, data) {
  return mockUserService.updateUser(id, data)
}

export function deleteUser(id) {
  return mockUserService.deleteUser(id)
}