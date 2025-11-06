<script setup lang="ts">
import {useRouter} from 'vue-router';
const router = useRouter()
</script>

<template>

  <nav>
    <span @click="router.push('/')">
      LOGO HERE
    </span>
    <div id="ButtonsLoginRegister">
      <button @click="router.push('/login')">
        Login
      </button>
      <button @click="router.push('/register')">
        Register
      </button>
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