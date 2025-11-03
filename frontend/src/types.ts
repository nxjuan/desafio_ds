export interface ScheduleTransferInput {
  sourceAccount: string
  destinationAccount: string
  amount: number
  transferDate: string 
}

export type TransferStatus = 'SCHEDULED'

export interface TransferResponse {
  id: string
  sourceAccount: string
  destinationAccount: string
  amount: number
  fee: number
  total: number
  scheduledDate: string
  transferDate: string
  status: TransferStatus
}

export type ValidationFields = Record<string, string>
