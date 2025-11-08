
export type APIResponse<T> = {
    success: boolean
    content: T;
    status?: number;
}

export type AuthResponse = APIResponse<{email : string, token: string}>;
export type RegisterInput = {email: string, password: string};

export type CountResponse = {count: number};