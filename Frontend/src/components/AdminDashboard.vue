<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue'
import type {UserSummary, Role, JitAuthorisationRequest} from '../api/types'
import {getNonAdminUsers, createJitForRoleChange} from '../repository/useAuthentication'

type RowState = {
  from: string
  to: string
  role: Exclude<Role, 'ADMIN'>
  loading: boolean
  message: string
  error: boolean
}

const users = ref<UserSummary[]>([])
const states = reactive<Record<string, RowState>>({})
const pageLoading = ref<boolean>(false)
const pageError = ref<string>('')

function keyForUser(u: UserSummary) {
  return String(u.id ?? u.email)
}

function initStateForUser(u: UserSummary) {
  const k = keyForUser(u)
  if (!states[k]) {
    const now = new Date()
    const in30 = new Date(now.getTime() + 30 * 60 * 1000)
    states[k] = {
      from: toLocalInput(now),
      to: toLocalInput(in30),
      // Default the select to the user's current role as required
      role: u.role as Exclude<Role, 'ADMIN'>,
      loading: false,
      message: '',
      error: false,
    }
  }
}

function toLocalInput(d: Date) {
  const pad = (n: number) => String(n).padStart(2, '0')
  const yyyy = d.getFullYear()
  const mm = pad(d.getMonth() + 1)
  const dd = pad(d.getDate())
  const hh = pad(d.getHours())
  const mi = pad(d.getMinutes())
  return `${yyyy}-${mm}-${dd}T${hh}:${mi}`
}

function toIso(s: string) {
  // Treat input as local time and convert to ISO string
  const d = new Date(s)
  return d.toISOString()
}

function normalizeRole(role: string): Role {
  const r = role.toUpperCase().replace(/^ROLE_/, '')
  if (r === 'LIBRARIAN') return 'LIBRARIAN'
  if (r === 'ADMIN') return 'ADMIN'
  return 'READER'
}

async function loadUsers() {
  pageLoading.value = true
  pageError.value = ''
  try {
    const res = await getNonAdminUsers()
    // Support either array at data or data.content
    const payload = (res as any)?.data?.content ?? (res as any)?.data ?? []
    const arr = Array.isArray(payload) ? payload : []
    users.value = arr.map((u: any) => ({
      ...u,
      role: normalizeRole(u.role)
    }))
    users.value.forEach(initStateForUser)
  } catch (e: any) {
    pageError.value = e?.response?.data?.message || 'Failed to load users.'
  } finally {
    pageLoading.value = false
  }
}

async function applyJit(u: UserSummary) {
  const k = keyForUser(u)
  const st = states[k]
  st.loading = true
  st.message = ''
  st.error = false
  try {
    const payload: JitAuthorisationRequest = {
      targetEmail: u.email,
      targetRole: st.role,
      validFrom: toIso(st.from),
      validTo: toIso(st.to)
    }
    const res = await createJitForRoleChange(payload)
    const ok = (res as any)?.status === 200
    st.message = ok ? 'JIT issued and cookie set (jit).' : 'Request sent.'
  } catch (e: any) {
    st.error = true
    const status = e?.response?.status
    if (status === 403) st.message = 'Forbidden (backend policy)'
    else if (status === 401) st.message = 'Unauthorized — login required'
    else st.message = e?.response?.data?.message || 'Error applying change'
  } finally {
    st.loading = false
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<template>
  <div class="admin-dashboard">
    <h2>Admin Dashboard</h2>
    <p class="hint">Temporary role changes via JIT. Admin roles cannot be granted or removed (enforced by backend).</p>

    <div v-if="pageLoading" class="page-spinner"></div>
    <div v-if="pageError" class="error">{{ pageError }}</div>

    <table v-if="!pageLoading && users.length" class="users-table">
      <thead>
      <tr>
        <th>Email</th>
        <th>Current role</th>
        <th>From</th>
        <th>To</th>
        <th>New Role</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="u in users" :key="keyForUser(u)">
        <td>{{ u.email }}</td>
        <td>{{ u.role }}</td>
        <td>
          <input type="datetime-local" v-model="states[keyForUser(u)].from" :disabled="states[keyForUser(u)].loading"/>
        </td>
        <td>
          <input type="datetime-local" v-model="states[keyForUser(u)].to" :disabled="states[keyForUser(u)].loading"/>
        </td>
        <td>
          <select v-model="states[keyForUser(u)].role" :disabled="states[keyForUser(u)].loading">
            <option value="ROLE_READER">Reader</option>
            <option value="ROLE_LIBRARIAN">Librarian</option>
          </select>
        </td>
        <td class="action-cell">
          <button @click="applyJit(u)" :disabled="states[keyForUser(u)].loading">Apply</button>
          <div v-if="states[keyForUser(u)].loading" class="row-spinner"></div>
          <div v-if="states[keyForUser(u)].message" :class="['msg', { error: states[keyForUser(u)].error }]">
            {{ states[keyForUser(u)].message }}
          </div>
        </td>
      </tr>
      </tbody>
    </table>

    <div v-if="!pageLoading && !users.length" class="empty">No non-admin users found.</div>
  </div>
</template>

<style scoped>
.admin-dashboard {
  padding: 24px;
}

h2 {
  margin-bottom: 8px;
}

.hint {
  color: #6b7280;
  margin-bottom: 16px;
}

.error {
  color: #b91c1c;
  margin: 8px 0;
}

.empty {
  color: #6b7280;
}

.users-table {
  width: 100%;
  border-collapse: collapse;
}

.users-table th, .users-table td {
  border: 1px solid #e5e7eb;
  padding: 8px;
  text-align: left;
}

.users-table th {
  background: #f9fafb;
}

.action-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 260px;
}

button {
  padding: 6px 12px;
  border: 1px solid #1f2937;
  background: #111827;
  color: white;
  border-radius: 6px;
}

/* Simple spinners */
.page-spinner, .row-spinner {
  width: 20px;
  height: 20px;
  border: 3px solid #e5e7eb;
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.page-spinner {
  margin: 12px 0;
}

@keyframes spin {
  from {
    transform: rotate(0deg)
  }
  to {
    transform: rotate(360deg)
  }
}
</style>
