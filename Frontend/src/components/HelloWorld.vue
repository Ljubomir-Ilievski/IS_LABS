<script setup lang="ts">
import { ref, onMounted } from 'vue'
import authRepo from '../repository/useAuthentication.ts'

defineProps<{ msg: string }>()

const count = ref(0)
const message = ref('')
const isAuthenticated = ref(false)

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
</script>

<template>
  <h1>{{ msg }}</h1>

  <div class="card">
    <button type="button" @click="onCountClick">count is {{ count }}</button>
  </div>
  <p v-if="message" style="text-align:center; margin-top: 8px;">{{ message }}</p>

</template>

<style scoped>
.card {
  display: flex;
  justify-content: center;
}
</style>
