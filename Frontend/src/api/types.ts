
export type APIResponse<T> = {
    success: boolean
    content: T;
    status?: number;
}

export type AuthResponse = APIResponse<{email : string, token: string}>;
export type Role = 'READER' | 'LIBRARIAN' | 'ADMIN';
export type RegisterInput = {email: string, password: string, role: Role};
export type LoginInput = {email: string, password: string};

export type CountResponse = {count: number};