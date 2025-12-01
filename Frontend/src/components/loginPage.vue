<script setup lang="ts">
import type {RegisterInput} from "../api/types.ts";
import useAuthentication from "../repository/useAuthentication.ts";
import {useRouter} from "vue-router";
import {ref} from "vue";
const router = useRouter()
let email = "";
let password = "";
let emailOrPasswordNotCorrectMessage = ref<string>("")
const isTwoFA = ref<boolean>(false)
const twoFACode = ref<string>("")
const infoMessage = ref<string>("")
const twoFAErrorMessage = ref<string>("")

async function makeLogin() {

  try {
    const registerInput: RegisterInput = {
      email: email,
      password: password
    };

    await useAuthentication.makeLogin(registerInput);
    isTwoFA.value = true;
    infoMessage.value = `A verification code has been sent to ${email}. Please enter it above.`
    emailOrPasswordNotCorrectMessage.value = "";
    try {
      await useAuthentication.sendTwoFactorCode(email);
    } catch (e) {
      twoFAErrorMessage.value = "Failed to send the code. Please click 'Resend code'.";
    }



  } catch (error) {
    // @ts-ignore - error may be AxiosError
    emailOrPasswordNotCorrectMessage.value = error?.response?.data?.message || 'Login failed';
  } finally {
    console.log("Login");
  }

}

async function resendCode() {
  twoFAErrorMessage.value = "";
  infoMessage.value = `A new verification code has been sent to ${email}.`;
  try {
    await useAuthentication.sendTwoFactorCode(email);
  } catch (e) {
    twoFAErrorMessage.value = "Could not resend the code. Please try again later.";
  }
}

async function verifyCode() {
  twoFAErrorMessage.value = "";
  try {
    const numericCode = Number(twoFACode.value);
    if (!twoFACode.value || Number.isNaN(numericCode)) {
      twoFAErrorMessage.value = "Please enter the 6-digit code.";
      return;
    }
    await useAuthentication.verifyTwoFactorCode(email, numericCode);
    router.push('/');
  } catch (e) {
    twoFAErrorMessage.value = "Invalid or expired code.";
  }
}
</script>

<template>
  <div id="component-register-page">
    <div class="form-card">
      <h2>Login</h2>

      <div class="form-group">
        <label for="username">Email</label>
        <input v-model="email" id="username" type="text" placeholder="Enter your email" :disabled="isTwoFA" />
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input v-model="password" id="password" type="password" placeholder="Enter your password" :disabled="isTwoFA" />
        <span style="margin-top: 5px; color: red">{{emailOrPasswordNotCorrectMessage}}</span>

      </div>

      <template v-if="!isTwoFA">
        <button @click="makeLogin" class="submit-btn">Login</button>
      </template>

      <template v-else>
        <div class="form-group">
          <label for="twofa">Two-Factor Code</label>
          <input v-model="twoFACode" id="twofa" type="text" placeholder="Enter the code sent to your email" />
          <small class="info">{{infoMessage}}</small>
          <span style="margin-top: 5px; color: red">{{twoFAErrorMessage}}</span>
        </div>
        <div class="twofa-actions">
          <button @click="resendCode" type="button" class="secondary-btn">Resend code</button>
          <button @click="verifyCode" class="submit-btn">Verify</button>
        </div>
      </template>
    </div>
  </div>
</template>

<style scoped>
#component-register-page {
  display: flex;
  justify-content: center;

}

/* Card container */
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

.secondary-btn {
  background-color: #e5e7eb;
  color: #111827;
  border: none;
  border-radius: 6px;
  padding: 10px 16px;
  font-size: 15px;
  cursor: pointer;
  width: 100%;
  transition: background-color 0.2s;
  margin-right: 8px;
}

.secondary-btn:hover {
  background-color: #d1d5db;
}

.twofa-actions {
  display: flex;
  gap: 8px;
}

.info {
  color: #6b7280;
}
</style>
