<script setup lang="ts">
import useAuthentication from "../repository/useAuthentication";

 function makeRegister() {

 /* useAuthentication.makeRegister();*/
console.log("USING THIS METHOD")
}


</script>

<template>
  <div id="component-register-page">
    <div class="form-card">
      <h2>Register</h2>

      <div class="form-group">
        <label for="username">Username</label>
        <input id="username" type="text" placeholder="Enter your username" />
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input id="password" type="password" placeholder="Enter your password" />
      </div>

      <button @click="makeRegister" class="submit-btn">Sign Up</button>
    </div>
  </div>
</template>

<style scoped>
#component-register-page {
  display: flex;
  justify-content: center;

}

.form-card {
  background: white;
  border: 1px solid #d1d5db;
  border-radius: 12px;
  padding: 30px 40px;
  width: 350px; /* not full width */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  text-align: center;
}

h2 {
  margin-bottom: 20px;
  color: #111827;
}

.form-group {
  display: flex;
  flex-direction: column;
  text-align: left;
  margin-bottom: 16px;
}

label {
  font-weight: 600;
  color: #374151;
  margin-bottom: 6px;
}

input {
  padding: 10px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.2s, box-shadow 0.2s;
}

input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
}

.submit-btn {
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 10px 16px;
  font-size: 15px;
  cursor: pointer;
  width: 100%;
  transition: background-color 0.2s;
}

.submit-btn:hover {
  background-color: #2563eb;
}
</style>
