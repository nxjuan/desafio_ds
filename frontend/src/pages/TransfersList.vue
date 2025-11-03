<template>
  <section class="card">
    <h2 style="margin:0">Lista de Transferências</h2>
    <!-- Cabeçalho + toolbar -->
    <div class="filters">
  <input
    class="input input--sm filters__search"
    v-model="q"
    type="search"
    placeholder="Buscar por ID, origem, destino…"
    @keydown.enter.prevent="fetchData"
  />

  <div class="filters__field">
    <label class="toolbar__label">Ordenar por</label>
    <select class="input input--sm" v-model="sortBy">
      <option value="scheduledDate">Agendado em</option>
      <option value="transferDate">Transferência</option>
      <option value="amount">Valor</option>
      <option value="fee">Taxa</option>
      <option value="total">Total</option>
    </select>
  </div>

  <button class="btn btn-ghost btn--sm filters__btn" @click="toggleDir" :title="dirTitle">
    {{ sortDir === 'desc' ? '↓' : '↑' }}
  </button>

  <button class="btn btn-ghost btn--sm filters__btn" @click="fetchData" :disabled="loading">
    <span v-if="loading" class="spinner" aria-hidden="true"></span>
    Atualizar
  </button>
</div>

    <!-- Erro -->
    <div v-if="error" class="alert error" style="margin-bottom:12px;">{{ error }}</div>

    <!-- Tabela compacta -->
    <div class="table-wrap" v-if="!loading && !error && filteredSorted.length">
      <table class="table">
        <thead>
          <tr>
            <th style="width:42px"></th>
            <th>Origem</th>
            <th>Destino</th>
            <th class="num">Valor</th>
            <th class="num">Taxa</th>
            <th>Data Transferência</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="t in filteredSorted" :key="t.id">
            <!-- linha principal (compacta) -->
            <tr class="row-main">
              <td class="toggle">
                <button
                  class="row-toggle"
                  :aria-expanded="isOpen(t.id)"
                  :title="isOpen(t.id) ? 'Recolher' : 'Ver detalhes'"
                  @click="toggle(t.id)"
                >
                  {{ isOpen(t.id) ? '▾' : '▸' }}
                </button>
              </td>
              <td class="mono">{{ t.sourceAccount }}</td>
              <td class="mono">{{ t.destinationAccount }}</td>
              <td class="num">{{ toBRL(t.amount) }}</td>
              <td class="num">{{ toBRL(t.fee) }}</td>
              <td>{{ fmtDate(t.transferDate) }}</td>
            </tr>

            <!-- detalhes -->
            <tr v-if="isOpen(t.id)" class="details-row">
              <td :colspan="6">
                <div class="details">
                  <div class="details__item">
                    <span class="details__label">ID</span>
                    <div class="mono">{{ t.id }}</div>
                    <button class="linklike" @click="copyId(t.id)">copiar</button>
                  </div>

                  <div class="details__item">
                    <span class="details__label">Agendado em</span>
                    <div>{{ fmtDate(t.scheduledDate) }}</div>
                  </div>

                  <div class="details__item">
                    <span class="details__label">Total</span>
                    <div class="num strong">{{ toBRL(t.total) }}</div>
                  </div>

                  <div class="details__item">
                    <span class="details__label">Status</span>
                    <span class="badge">{{ t.status }}</span>
                  </div>
                </div>
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>

    <!-- Loading -->
    <div v-else-if="loading" class="skeleton">
      <div v-for="n in 6" :key="n" class="skeleton__row"></div>
    </div>

    <!-- Vazio -->
    <div v-else class="empty">
      <p>Nenhuma transferência encontrada.</p>
      <button class="btn btn-ghost btn--sm" @click="fetchData">Recarregar</button>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { listTransfers } from '@/api/transfers'
import type { TransferResponse } from '@/types'
import { toBRL } from '@/utils/format'

const transfers = ref<TransferResponse[]>([])
const loading = ref(false)
const error = ref<string | null>(null)

const q = ref('')
const sortBy = ref<'scheduledDate'|'transferDate'|'amount'|'fee'|'total'>('scheduledDate')
const sortDir = ref<'asc'|'desc'>('desc')

function fmtDate(iso: string) { try { return new Date(iso).toLocaleDateString('pt-BR') } catch { return iso } }
function toggleDir() { sortDir.value = sortDir.value === 'desc' ? 'asc' : 'desc' }
const dirTitle = computed(() => sortDir.value === 'desc' ? 'Ordem: decrescente' : 'Ordem: crescente')

const filteredSorted = computed(() => {
  const needle = q.value.trim().toLowerCase()
  let arr = transfers.value
  if (needle) {
    arr = arr.filter(t =>
      t.id.toLowerCase().includes(needle) ||
      t.sourceAccount.includes(needle) ||
      t.destinationAccount.includes(needle) ||
      t.status.toLowerCase().includes(needle),
    )
  }
  const dir = sortDir.value === 'desc' ? -1 : 1
  return [...arr].sort((a, b) => {
    if (sortBy.value === 'scheduledDate' || sortBy.value === 'transferDate') {
      const av = new Date(a[sortBy.value]).getTime()
      const bv = new Date(b[sortBy.value]).getTime()
      return av < bv ? dir : av > bv ? -dir : 0
    }
    const av = (a as any)[sortBy.value] as number
    const bv = (b as any)[sortBy.value] as number
    return av < bv ? dir : av > bv ? -dir : 0
  })
})

const totalLabel = computed(() =>
  `${filteredSorted.value.length} registro${filteredSorted.value.length === 1 ? '' : 's'}`
)

async function fetchData() {
  loading.value = true
  error.value = null
  try {
    const items = await listTransfers()
    transfers.value = items.sort((a, b) => b.scheduledDate.localeCompare(a.scheduledDate))
  } catch (e: any) {
    error.value = e?.response?.data?.error || e?.message || 'Erro ao buscar transferências.'
  } finally {
    loading.value = false
  }
}

/** --- expand/collapse --- */
const open = ref(new Set<string>())
function toggle(id: string) {
  const s = new Set(open.value)
  s.has(id) ? s.delete(id) : s.add(id)
  open.value = s
}
function isOpen(id: string) { return open.value.has(id) }

async function copyId(id: string) { try { await navigator.clipboard.writeText(id) } catch {  } }

onMounted(fetchData)
</script>

<style scoped>

.toggle { text-align: center; }
.row-toggle {
  width: 28px; height: 28px; border: 1px solid var(--border);
  border-radius: 8px; background: #fff; cursor: pointer;
  line-height: 1; font-size: 14px;
}
.row-toggle:hover { box-shadow: 0 0 0 3px rgba(147,197,253,.25); border-color: var(--ring); }


.details-row td {
  background: #fbfdff;
  padding: 8px 12px 14px;
}
.details {
  display: grid;
  grid-template-columns: repeat(12, 1fr);
  gap: 10px 16px;
}
.details__item { grid-column: span 6; }
@media (max-width: 900px) { .details__item { grid-column: span 12; } }
.details__label { display:block; font-size: 12px; color: var(--muted); margin-bottom: 2px; }

.linklike { background:none; border:0; padding:0; color: var(--primary-600); cursor:pointer; }
.linklike:hover { text-decoration: underline; }
.mono { font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace; }
.strong { font-weight: 700; }

.filters{
  display: flex !important;          
  flex-wrap: wrap;
  gap: 8px 10px;
  align-items: flex-end;
}


.filters__search{
  width: clamp(260px, 38vw, 420px);  
}


.filters__field{
  display: grid;
  gap: 6px;
  width: 220px;                     
}
.filters__field > .input{ width: 100%; } 

.filters__btn{ flex: 0 0 auto; }

@media (max-width: 480px){
  .filters{ flex-direction: column; align-items: stretch; }
  .filters__search, .filters__field{ width: 100%; }
}
</style>
