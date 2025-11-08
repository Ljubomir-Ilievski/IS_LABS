<script setup lang="ts">
import {useRouter, useRoute} from 'vue-router';
import { onMounted, ref, watch } from 'vue';

const router = useRouter()
const route = useRoute()

function hasJwtCookie(): boolean {
  // Checks if a cookie named "jwt" exists in document.cookie
  // This assumes the cookie is not httpOnly or that the presence is detectable.
  return typeof document !== 'undefined' && document.cookie.split(';').some(c => c.trim().startsWith('jwt='));
}

function clearJwtCookie() {
  // Attempt to clear the JWT cookie by expiring it.
  // Use path=/ to match most setups; domain is omitted to avoid mismatches.
  // If the backend sets a specific domain, consider aligning it here as well.
  document.cookie = 'jwt=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT; SameSite=Lax';
}

const loggedIn = ref<boolean>(hasJwtCookie())

function refreshAuthState() {
  loggedIn.value = hasJwtCookie()
}

onMounted(() => {
  refreshAuthState()
})

watch(() => route.fullPath, () => {
  // Re-evaluate on route changes (e.g., after login/register redirects)
  refreshAuthState()
})

async function onLogoutClick() {
  try {
    clearJwtCookie()
  } catch (e) {
    // noop – best-effort client-side clear
  } finally {
    refreshAuthState()
    // Force a full reload so all components re-evaluate auth-sensitive logic
    if (typeof window !== 'undefined') window.location.reload()
  }
}
</script>

<template>

  <nav>
    <span @click="router.push('/')">
      LOGO HERE
    </span>
    <div id="ButtonsLoginRegister">
      <template v-if="loggedIn">
        <button @click="onLogoutClick">Logout</button>
      </template>
      <template v-else>
        <button @click="router.push('/login')">
          Login
        </button>
        <button @click="router.push('/register')">
          Register
        </button>
      </template>
    </div>
  </nav>

</template>

<style scoped>
nav {
  margin: 0;
  padding-right: 100px;
  padding-left: 100px;
  height: 100px;
  background-color: #FFE347;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
button  {
  border: black solid 1px;
  padding: 10px;
  border-radius: 10px;
  background-color: black;
  color: white;
  transition: box-shadow 0.2s;
}
button:hover {
  box-shadow: blue 0 0 7px;
}
#ButtonsLoginRegister{
  display: flex;
  justify-content: center;
  gap: 10px;
}
span{
  font-size: 5rem;
  color: white;
}
</style>