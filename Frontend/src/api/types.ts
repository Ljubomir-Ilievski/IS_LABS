
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

// Admin-related types
export type UserSummary = {
    id?: string | number;
    email: string;
    role: Role;
}

export type JitAuthorisationRequest = {
    targetEmail: string;
    targetRole: Exclude<Role, 'ADMIN'>; // admin privileges cannot be granted via JIT
    validFrom: string; // ISO timestamp
    validTo: string;   // ISO timestamp
}

// Books
export type BookDto = {
    id: number;
    title: string;
    description: string;
}

export type BookAccessResultDto = {
    bookId: number;
    canRead: boolean;
    canWrite: boolean;
    canReadAndWrite: boolean;
}