import { api } from './client'
import type { ScheduleTransferInput, TransferResponse } from '@/types'

export async function scheduleTransfer(input: ScheduleTransferInput) {
  const { data } = await api.post<TransferResponse>('/v1/transfers', input)
  return data
}

export async function listTransfers() {
  const { data } = await api.get<TransferResponse[]>('/v1/transfers')
  return data
}
