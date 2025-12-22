import { mockNotificationService } from '@/mock/mockService'

export function getNotificationList(params) {
  return mockNotificationService.getNotificationList(params)
}

export function getNotificationById(id) {
  return mockNotificationService.getNotificationById(id)
}

export function createNotification(data) {
  return mockNotificationService.createNotification(data)
}

export function updateNotification(id, data) {
  return mockNotificationService.updateNotification(id, data)
}

export function deleteNotification(id) {
  return mockNotificationService.deleteNotification(id)
}

export function getPendingNotifications() {
  return mockNotificationService.getPendingNotifications()
}