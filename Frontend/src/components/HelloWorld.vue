<script setup lang="ts">
import { ref, onMounted } from 'vue'
import authRepo, { listBooks, pingBook } from '../repository/useAuthentication.ts'
import type { BookDto, BookAccessResultDto } from '../api/types.ts'

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

// Books state
const books = ref<BookDto[]>([])
const booksLoading = ref<boolean>(false)
const booksError = ref<string>('')
const bookLoading: Record<number, boolean> = {}
const bookResult: Record<number, string> = {}

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
  fetchBooks()
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

async function fetchBooks() {
  booksLoading.value = true
  booksError.value = ''
  try {
    const res = await listBooks()
    const data = (res as any)?.data
    // backend may wrap or return plain array
    const arr = Array.isArray(data?.content) ? data.content : (Array.isArray(data) ? data : [])
    books.value = arr as BookDto[]
  } catch (e: any) {
    const status = e?.response?.status
    if (status === 401) booksError.value = 'Unauthorized — please login to see books'
    else booksError.value = e?.response?.data?.message || 'Failed to load books'
  } finally {
    booksLoading.value = false
  }
}

function permissionText(result: BookAccessResultDto): string {
  // Translate flags into human-friendly text
  if (result.canReadAndWrite || (result.canRead && result.canWrite)) return 'Read & Write'
  if (result.canWrite) return 'Write only'
  if (result.canRead) return 'Read only'
  return 'No permissions'
}

async function onBookClick(book: BookDto) {
  bookResult[book.id] = ''
  bookLoading[book.id] = true
  try {
    const res = await pingBook(book.id)
    const payload = (res as any)?.data?.content ?? (res as any)?.data
    if (payload && typeof payload === 'object') {
      bookResult[book.id] = permissionText(payload as BookAccessResultDto)
    } else {
      bookResult[book.id] = 'Unknown response'
    }
  } catch (e: any) {
    const status = e?.response?.status
    if (status === 401) bookResult[book.id] = 'Unauthorized'
    else if (status === 403) bookResult[book.id] = 'Forbidden'
    else bookResult[book.id] = e?.response?.data?.message || 'Error'
  } finally {
    bookLoading[book.id] = false
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

  <!-- Books section -->
  <div class="books-section">
    <h2>Books</h2>
    <div v-if="booksLoading" class="page-spinner"></div>
    <div v-if="booksError" class="error">{{ booksError }}</div>
    <div class="books-list" v-if="!booksLoading && books.length">
      <div class="book-item" v-for="b in books" :key="b.id">
        <button class="book-button" @click="onBookClick(b)" :disabled="bookLoading[b.id]">
          <span class="book-title">{{ b.title }}</span>
        </button>
        <div class="book-result">
          <span v-if="bookLoading[b.id]" class="row-spinner"></span>
          <span v-else-if="bookResult[b.id]">{{ bookResult[b.id] }}</span>
        </div>
      </div>
    </div>
    <div v-else-if="!booksLoading && !books.length" class="empty">No books.</div>
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

.books-section { margin-top: 20px; }
.books-list { display: flex; flex-direction: column; gap: 10px; }
.book-item { display: flex; align-items: center; gap: 12px; }
.book-button { width: 100%; text-align: left; padding: 10px 12px; border-radius: 8px; border: 1px solid #e5e7eb; background: #f9fafb; cursor: pointer; }
.book-button:hover { background: #f3f4f6; }
.book-title { font-weight: 600; color: #111827; }
.book-result { min-width: 140px; color: #374151; }
.error { color: #b91c1c; margin-top: 8px; }
.empty { color: #6b7280; }
.page-spinner { margin: 8px 0; width: 24px; height: 24px; border: 3px solid #e5e7eb; border-top-color: #374151; border-radius: 50%; animation: spin 0.9s linear infinite; }
.row-spinner { display: inline-block; width: 16px; height: 16px; border: 2px solid #e5e7eb; border-top-color: #374151; border-radius: 50%; animation: spin 0.9s linear infinite; }
</style>
