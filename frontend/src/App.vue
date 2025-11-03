<template>
  <div class="app">
    <header class="nav">
      <h1>Transferências</h1>
      <nav>
        <RouterLink to="/transfers">Listar</RouterLink>
        <RouterLink to="/schedule">Agendar</RouterLink>
      </nav>
    </header>

    <main class="container">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.app { font-family: system-ui, -apple-system, Segoe UI, Roboto, Ubuntu, Cantarell, 'Helvetica Neue', Arial, 'Noto Sans', 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji', sans-serif; }
.nav {
  display: flex; align-items: center; justify-content: space-between;
  padding: 12px 16px; border-bottom: 1px solid #ececec; background: #fafafa;
}
.nav a { margin-left: 12px; text-decoration: none; color: #1e40af; }
.nav a.router-link-active { font-weight: 600; }
.container { max-width: 900px; margin: 16px auto; padding: 0 12px; }
.card { border: 1px solid #e5e7eb; border-radius: 8px; padding: 16px; background: #fff; }
.row { display: flex; gap: 12px; }
.row > * { flex: 1; }
label { display:block; font-size: 13px; color: #374151; margin-bottom: 4px; }
input { width:100%; padding: 10px; border:1px solid #d1d5db; border-radius: 6px; }
button { padding: 10px 14px; border-radius: 6px; border: 1px solid #1e40af; background: #1e40af; color:#fff; cursor:pointer; }
button:disabled { opacity:.6; cursor:not-allowed; }
.table { width: 100%; border-collapse: collapse; }
.table th, .table td { border-bottom:1px solid #eee; padding: 10px; text-align: left; }
.badge { display:inline-block; padding: 4px 8px; background:#eef2ff; color:#3730a3; border-radius:999px; font-size: 12px; }
.error { color: #b91c1c; font-size: 13px; margin-top: 4px; }
.success { color: #065f46; }
</style>
