<script setup lang="ts">
import useAuthentication from "../repository/useAuthentication.ts";
import type {RegisterInput, Role} from "../api/types.ts";
import {useRouter} from "vue-router";
import {ref} from "vue";
import EmailPassowrdFormatCheck from "../utilityFunctions/EmailPassowrdFormatCheck";
let email = "";
let password = "";
const role = ref<Role>('READER')
let emailConflictMessage = ref<string>("");
let emailFormatErrorMessage = ref<string>("");
let passwordFormatErrorMessage = ref<string>("");
const router = useRouter()

const isVerify = ref<boolean>(false)
const verifyCode = ref<string>("")
const infoMessage = ref<string>("")
const verifyErrorMessage = ref<string>("")


async function makeRegister() {
  const registerInput: RegisterInput = {
    email: email,
    password: password,
    role: role.value
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
    isVerify.value = true;
    infoMessage.value = `A verification code has been sent to ${email}. Please enter it below to activate your account.`
    emailConflictMessage.value = "";
  }
  catch (error) {
    // @ts-ignore
    console.log(error?.response?.data?.message)
    // @ts-ignore
    emailConflictMessage.value = error?.response?.data?.message || 'Registration failed';
  }
  finally {
    console.log("Register");
  }


}

async function verifyRegisterCode() {
  verifyErrorMessage.value = "";
  const numericCode = Number(verifyCode.value);
  if (!verifyCode.value || Number.isNaN(numericCode)) {
    verifyErrorMessage.value = "Please enter the 6-digit code.";
    return;
  }
  try {
    await useAuthentication.verifyRegister(email, numericCode);
    router.push('/login');
  } catch (e) {
    verifyErrorMessage.value = "Invalid or expired code.";
  }
}


</script>

<template>
  <div id="component-register-page">
    <div class="form-card">
      <h2>Register</h2>

      <template v-if="!isVerify">
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

        <div class="form-group">
          <label for="role">Role</label>
          <select id="role" v-model="role">
            <option value="READER">Reader</option>
            <option value="LIBRARIAN">Librarian</option>
            <option value="ADMIN">Admin</option>
          </select>
        </div>

        <button @click="makeRegister" class="submit-btn">Sign Up</button>
      </template>

      <template v-else>
        <div class="form-group">
          <label for="verify">Verification Code</label>
          <input v-model="verifyCode" id="verify" type="text" placeholder="Enter the code sent to your email" />
          <small class="info">{{infoMessage}}</small>
          <span style="margin-top: 5px; color: red">{{verifyErrorMessage}}</span>
        </div>
        <button @click="verifyRegisterCode" class="submit-btn">Verify & Continue to Login</button>
      </template>
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

select {
  padding: 10px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 14px;
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
