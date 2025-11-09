const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/
const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z\d]).{8,}$/
function validEmail(email: string) {
    return emailRegex.test(email)
}
function validPassword(password: string) {
    return passwordRegex.test(password)
}


export default { validEmail, validPassword }