<script setup lang="ts">
import { ref, onMounted } from 'vue'
import authRepo from '../repository/useAuthentication.ts'

defineProps<{ msg: string }>()

const count = ref(0)
const message = ref('')
const isAuthenticated = ref(false)

// Role ping UI state
const loadingReader = ref(false)
const loadingLibrarian = ref(false)
const loadingAdmin = ref(false)
const resultReader = ref<string>('')
const resultLibrarian = ref<string>('')
const resultAdmin = ref<string>('')

async function initCount() {
  try {
    const res = await authRepo.getCount()
    const value = (res as any)?.data?.content?.count ?? (res as any)?.data?.count
    if (typeof value === 'number') {
      count.value = value
      isAuthenticated.value = true
      message.value = ''
    } else {
      // If backend returns unexpected shape, treat as unauthenticated
      isAuthenticated.value = false
      message.value = 'Please log it to use the count'
    }
  } catch (e: any) {
    // Most likely not logged in (401) or network error
    isAuthenticated.value = false
    message.value = 'Please log it to use the count'
  }
}

onMounted(() => {
  initCount()
})

async function onCountClick() {
  if (!isAuthenticated.value) {
    message.value = 'you can only use the count if you are logged in'
    return
  }
  try {
    const res = await authRepo.makeCount()
    const value = (res as any)?.data?.content?.count ?? (res as any)?.data?.count
    if (typeof value === 'number') {
      count.value = value
    }
  } catch (e: any) {
    // Session might have expired
    isAuthenticated.value = false
    message.value = 'you can only use the count if you are logged in'
  }
}

async function pingReader() {
  await pingRole('reader')
}
async function pingLibrarian() {
  await pingRole('librarian')
}
async function pingAdmin() {
  await pingRole('admin')
}

async function pingRole(role: 'reader' | 'librarian' | 'admin') {
  // show loading and clear previous result for that role
  const setLoading = (val: boolean) => {
    if (role === 'reader') loadingReader.value = val
    if (role === 'librarian') loadingLibrarian.value = val
    if (role === 'admin') loadingAdmin.value = val
  }
  const setResult = (text: string) => {
    if (role === 'reader') resultReader.value = text
    if (role === 'librarian') resultLibrarian.value = text
    if (role === 'admin') resultAdmin.value = text
  }

  setResult('')
  setLoading(true)
  try {
    let res
    if (role === 'reader') res = await authRepo.pingReader()
    if (role === 'librarian') res = await authRepo.pingLibrarian()
    if (role === 'admin') res = await authRepo.pingAdmin()
    const status = (res as any)?.status
    if (status === 200) {
      setResult('Allowed (200 OK)')
    } else {
      setResult('Forbidden')
    }
  } catch (e: any) {
    const status = e?.response?.status
    if (status === 403) setResult('Forbidden')
    else if (status === 401) setResult('Unauthorized')
    else setResult('Error')
  } finally {
    setLoading(false)
  }
}
</script>

<template>
  <h1>{{ msg }}</h1>

  <div class="card">
    <button type="button" @click="onCountClick">count is {{ count }}</button>
  </div>
  <p v-if="message" style="text-align:center; margin-top: 8px;">{{ message }}</p>

  <!-- Role ping squares -->
  <div class="roles-grid">
    <div class="role-square reader" @click="pingReader">
      <div class="square-content">
        <div class="icon">📖</div>
        <div class="title">Reader</div>
        <div v-if="loadingReader" class="spinner"></div>
      </div>
    </div>
    <div class="role-square librarian" @click="pingLibrarian">
      <div class="square-content">
        <div class="icon">📚</div>
        <div class="title">Librarian</div>
        <div v-if="loadingLibrarian" class="spinner"></div>
      </div>
    </div>
    <div class="role-square admin" @click="pingAdmin">
      <div class="square-content">
        <div class="icon">🛡️</div>
        <div class="title">Admin</div>
        <div v-if="loadingAdmin" class="spinner"></div>
      </div>
    </div>
  </div>

  <div class="results">
    <div class="result" v-if="resultReader">Reader: {{ resultReader }}</div>
    <div class="result" v-if="resultLibrarian">Librarian: {{ resultLibrarian }}</div>
    <div class="result" v-if="resultAdmin">Admin: {{ resultAdmin }}</div>
  </div>
</template>

<style scoped>
.card {
  display: flex;
  justify-content: center;
}

.roles-grid {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.role-square {
  height: 110px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: transform 0.1s ease, box-shadow 0.2s ease;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.role-square:hover { transform: translateY(-2px); box-shadow: 0 6px 16px rgba(0,0,0,0.12); }

.reader { background: linear-gradient(135deg, #e0f2fe, #bfdbfe); border: 1px solid #93c5fd; }
.librarian { background: linear-gradient(135deg, #ecfccb, #d9f99d); border: 1px solid #a3e635; }
.admin { background: linear-gradient(135deg, #fee2e2, #fecaca); border: 1px solid #fca5a5; }

.square-content { text-align: center; }
.icon { font-size: 28px; }
.title { font-weight: 600; margin-top: 6px; color: #1f2937; }

.spinner {
  margin: 10px auto 0;
  width: 20px;
  height: 20px;
  border: 3px solid rgba(255,255,255,0.6);
  border-top-color: rgba(31,41,55,0.9);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.results {
  margin-top: 8px;
  text-align: center;
  color: #374151;
}
.result { margin-top: 4px; }
</style>
