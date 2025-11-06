import http from "../api/api.ts";
import type { APIResponse } from "../api/types.ts";


async function makeLogin() {
    return await http.post<APIResponse<string>>("school");
}
async function makeRegister() {
    return await http.post<APIResponse<string>>("school");
}

export default { makeLogin, makeRegister };