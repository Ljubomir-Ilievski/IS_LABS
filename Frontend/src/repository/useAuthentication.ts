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


export default { makeLogin, makeRegister, makeCount, getCount };