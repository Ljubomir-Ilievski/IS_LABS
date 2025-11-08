import axios from 'axios';
console.log("LOGGING HERE",import.meta.env.VITE_API_ENDPOINT)
const instance = axios.create({

    baseURL: import.meta.env.VITE_API_ENDPOINT,
    withCredentials: true,
});
export default instance