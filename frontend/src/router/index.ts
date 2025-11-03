import { createRouter, createWebHistory } from 'vue-router'
import TransfersList from '@/pages/TransfersList.vue'
import ScheduleTransfer from '@/pages/ScheduleTransfer.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/transfers' },
    { path: '/transfers', name: 'transfers', component: TransfersList },
    { path: '/schedule', name: 'schedule', component: ScheduleTransfer },
  ],
})

export default router
