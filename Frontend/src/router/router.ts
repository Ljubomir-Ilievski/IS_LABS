import { createMemoryHistory, createRouter } from 'vue-router'
import RegisterPage from "../components/RegisterPage.vue";
import HelloWorld from "../components/HelloWorld.vue";
import loginPage from "../components/loginPage.vue";

const routes: any[] = [
    {path: '/register', component: RegisterPage},
    {path: '/login', component: loginPage},
    { path: '/', component: HelloWorld },

]


export const router = createRouter({
    history: createMemoryHistory(),
    routes,
})