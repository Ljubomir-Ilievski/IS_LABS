import http from "../api/api.ts";
import type {RegisterInput, AuthResponse, CountResponse} from "../api/types.ts";


async function makeLogin(body: RegisterInput) {
    return await http.post<AuthResponse>("/api/auth/login", body);
}
async function makeRegister(body: RegisterInput) {
    return await http.post<AuthResponse>('/api/auth/register', body);
}

async function makeCount() {
    return await http.post<CountResponse>('/api/auth/makeCount');
}
async function getCount() {
    return await http.get<CountResponse>('/api/auth/getCount');
}

// 2FA endpoints
async function sendTwoFactorCode(email: string) {
    // Backend expects @RequestParam email
    return await http.post<string>('/api/2fa/send', null, { params: { email } });
}

async function verifyTwoFactorCode(email: string, code: number) {
    // Backend expects @RequestParam email and code (Int)
    return await http.post<string>('/api/2fa/verify', null, { params: { email, code } });
}

export default { makeLogin, makeRegister, makeCount, getCount, sendTwoFactorCode, verifyTwoFactorCode };