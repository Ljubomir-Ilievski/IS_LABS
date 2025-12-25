import http from "../api/api.ts";
import type {RegisterInput, LoginInput, AuthResponse, CountResponse, Role, UserSummary, JitAuthorisationRequest, BookDto, BookAccessResultDto} from "../api/types.ts";


async function makeLogin(body: LoginInput) {
    return await http.post<AuthResponse>("/api/auth/login", body);
}
async function makeRegister(body: RegisterInput) {
    // Backend now exposes role-specific registration endpoints
    // /api/auth/register/admin | /librarian | /reader
    const path = getRegisterPathByRole(body.role);
    // Backend expects AuthRequestDTO with email & password in body
    const payload = { email: body.email, password: body.password };
    return await http.post<AuthResponse>(path, payload);
}

function getRegisterPathByRole(role: Role): string {
    switch (role) {
        case 'ADMIN':
            return '/api/auth/register/admin';
        case 'LIBRARIAN':
            return '/api/auth/register/librarian';
        case 'READER':
        default:
            return '/api/auth/register/reader';
    }
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

async function verifyRegister(email: string, code: number) {
    return await http.post<string>('/api/2fa/verify/register', null, { params: { email, code } });
}

async function pingReader() {
    return await http.get<string>('/api/ping/reader');
}
async function pingLibrarian() {
    return await http.get<string>('/api/ping/librarian');
}
async function pingAdmin() {
    return await http.get<string>('/api/ping/admin');
}

export default { makeLogin, makeRegister, makeCount, getCount, sendTwoFactorCode, verifyTwoFactorCode, verifyRegister, pingReader, pingLibrarian, pingAdmin };

// New admin-facing repository functions
export async function getNonAdminUsers() {
    // Adjust this path to match backend list endpoint if different
    return await http.get<UserSummary[]>('/api/users/non-admins');
}

export async function createJitForRoleChange(payload: JitAuthorisationRequest) {
    // Backend endpoint per issue description
    return await http.post('/api/authorization/jit-db', payload);
}

// Books repository
export async function listBooks() {
    return await http.get<BookDto[]>('/api/books');
}

export async function pingBook(id: number) {
    return await http.post<BookAccessResultDto>(`/api/books/${id}/ping`);
}