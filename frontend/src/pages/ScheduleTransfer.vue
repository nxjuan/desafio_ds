<template>
  <section class="card">
    <div class="card-header">
      <h2 style="margin:0">Agendar transferência</h2>
      <p class="muted">Preencha os campos abaixo e clique em Agendar.</p>
    </div>

    <form @submit.prevent="onSubmit">
      <div class="form-grid">
        <div class="field col-6">
          <label for="source">Conta origem</label>
          <input
            id="source"
            class="input"
            v-model="form.sourceAccount"
            maxlength="10"
            inputmode="numeric"
            placeholder="Ex.: 1234567890"
            :aria-invalid="Boolean(errors.sourceAccount)"
            aria-describedby="source-error source-help"
            required
          />
          <div id="source-help" class="help">10 dígitos numéricos.</div>
          <div v-if="errors.sourceAccount" id="source-error" class="error">{{ errors.sourceAccount }}</div>
        </div>

        <div class="field col-6">
          <label for="dest">Conta destino</label>
          <input
            id="dest"
            class="input"
            v-model="form.destinationAccount"
            maxlength="10"
            inputmode="numeric"
            placeholder="Ex.: 9999999999"
            :aria-invalid="Boolean(errors.destinationAccount)"
            aria-describedby="dest-error dest-help"
            required
          />
          <div id="dest-help" class="help">10 dígitos numéricos.</div>
          <div v-if="errors.destinationAccount" id="dest-error" class="error">{{ errors.destinationAccount }}</div>
        </div>

        <div class="field col-6">
          <label for="amount">Valor (R$)</label>
          <input
            id="amount"
            class="input"
            v-model.number="form.amount"
            type="number"
            min="0.01"
            step="0.01"
            placeholder="Ex.: 1000,00"
            :aria-invalid="Boolean(errors.amount)"
            aria-describedby="amount-error"
            required
          />
          <div v-if="errors.amount" id="amount-error" class="error">{{ errors.amount }}</div>
        </div>

        <div class="field col-6">
          <label for="date">Data da transferência</label>
          <input
            id="date"
            class="input"
            v-model="form.transferDate"
            type="date"
            :min="today"
            :aria-invalid="Boolean(errors.transferDate)"
            aria-describedby="date-error"
            required
          />
          <div v-if="errors.transferDate" id="date-error" class="error">{{ errors.transferDate }}</div>
        </div>
      </div>

      <div style="margin-top: 14px; display:flex; gap:10px;">
        <button class="btn" type="submit" :disabled="submitting">
          <span v-if="submitting" class="spinner" aria-hidden="true"></span>
          {{ submitting ? 'Agendando…' : 'Agendar' }}
        </button>
        <button class="btn btn-ghost" type="button" @click="resetForm" :disabled="submitting">Limpar</button>
      </div>
    </form>

    <div v-if="apiError" class="alert error" style="margin-top:12px;">{{ apiError }}</div>

    <div v-if="result" class="alert" style="margin-top:12px;">
      ✅ Transferência agendada! Total: <strong>{{ toBRL(result.total) }}</strong>
      (taxa: {{ toBRL(result.fee) }}) — ID: {{ result.id.slice(0,8) }}…
    </div>
  </section>
</template>

<script setup lang="ts">
import { reactive, ref, computed } from 'vue'
import { scheduleTransfer } from '@/api/transfers'
import type { ScheduleTransferInput, TransferResponse, ValidationFields } from '@/types'
import { toBRL } from '@/utils/format'

const today = computed(() => {
  const d = new Date()
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
})

const form = reactive<ScheduleTransferInput>({
  sourceAccount: '',
  destinationAccount: '',
  amount: 0,
  transferDate: today.value,
})

const errors = reactive<ValidationFields>({})
const submitting = ref(false)
const apiError = ref<string | null>(null)
const result = ref<TransferResponse | null>(null)

function validateLocal(): boolean {
  errors.sourceAccount = /^\d{10}$/.test(form.sourceAccount) ? '' : 'Informe 10 dígitos.'
  errors.destinationAccount = /^\d{10}$/.test(form.destinationAccount) ? '' : 'Informe 10 dígitos.'
  errors.amount = form.amount > 0 ? '' : 'Valor deve ser maior que zero.'
  errors.transferDate = form.transferDate ? '' : 'Informe a data.'
  if (!errors.transferDate) {
    const chosen = new Date(form.transferDate)
    const min = new Date(today.value)
    if (chosen < min) errors.transferDate = 'Data não pode ser no passado.'
  }
  if (!errors.sourceAccount && !errors.destinationAccount && form.sourceAccount === form.destinationAccount) {
    errors.destinationAccount = 'Contas de origem e destino não podem ser iguais.'
  }
  return !Object.values(errors).some(Boolean)
}

function clearServerErrors() {
  for (const k of Object.keys(errors)) (errors as any)[k] = ''
  apiError.value = null
}

function resetForm() {
  form.sourceAccount = ''
  form.destinationAccount = ''
  form.amount = 0
  form.transferDate = today.value
  clearServerErrors()
  result.value = null
}

async function onSubmit() {
  clearServerErrors()
  result.value = null
  if (!validateLocal()) return

  submitting.value = true
  try {
    const data = await scheduleTransfer(form)
    result.value = data
    form.amount = 0
  } catch (e: any) {
    const status = e?.response?.status
    const payload = e?.response?.data
    if (status === 400 && payload?.fields) {
      Object.assign(errors, payload.fields)
      apiError.value = payload.error || 'Problema de validação.'
    } else if (status === 422 && payload?.error) {
      apiError.value = payload.error
    } else {
      apiError.value = 'Não foi possível agendar. Verifique sua conexão e tente novamente.'
    }
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>

</style>
