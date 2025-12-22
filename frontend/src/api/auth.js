import { mockAuthService } from '@/mock/mockService'

export function login(data) {
  return mockAuthService.login(data)
}

export function register(data) {
  return mockAuthService.register(data)
}