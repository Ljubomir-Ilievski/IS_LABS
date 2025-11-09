<script setup lang="ts">
import useAuthentication from "../repository/useAuthentication";
import type {RegisterInput} from "../api/types.ts";
import {useRouter} from "vue-router";
import {ref} from "vue";
import EmailPassowrdFormatCheck from "../utilityFunctions/EmailPassowrdFormatCheck";
let email = "";
let password = "";
let emailConflictMessage = ref<string>("");
let emailFormatErrorMessage = ref<string>("");
let passwordFormatErrorMessage = ref<string>("");
const router = useRouter()


async function makeRegister() {
  const registerInput: RegisterInput = {
    email: email,
    password: password
  };

  if (!EmailPassowrdFormatCheck.validEmail(registerInput.email)){
    emailFormatErrorMessage.value = "Invalid email format"
    return;
  }
  if (!EmailPassowrdFormatCheck.validPassword(registerInput.password)){
    console.log("IS IT GETTING TO PASSORD CHECK")
    passwordFormatErrorMessage.value = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character"
    return;
  }


try {
 await useAuthentication.makeRegister(registerInput);
 router.push('/login')

}
catch (error) {
  console.log(error.response.data.message)
  emailConflictMessage.value = error.response.data.message;
}
finally {
  console.log("Register");
}


}


</script>

<template>
  <div id="component-register-page">
    <div class="form-card">
      <h2>Register</h2>

      <div class="form-group">
        <label for="username">Email</label>
        <input v-model="email" id="username" type="text" placeholder="Enter your username" />
        <span style="margin-top: 5px; color: red">{{emailConflictMessage}}</span>
        <span style="margin-top: 5px; color: red">{{emailFormatErrorMessage}}</span>

      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input v-model="password" id="password" type="password" placeholder="Enter your password" />
        <span style="margin-top: 5px; color: red">{{passwordFormatErrorMessage}}</span>

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
