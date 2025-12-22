import { mockReportService } from '@/mock/mockService'

export function getReportList(params) {
  return mockReportService.getReportList(params)
}

export function getReportById(id) {
  return mockReportService.getReportById(id)
}

export function createReport(data) {
  return mockReportService.createReport(data)
}

export function updateReport(id, data) {
  return mockReportService.updateReport(id, data)
}

export function deleteReport(id) {
  return mockReportService.deleteReport(id)
}