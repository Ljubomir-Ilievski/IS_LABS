package com.backend.`is`.IS_Backend.service.intf

interface TwoFactorAuthService {

    fun sendCode(email: String);
    fun verifyCode(email: String, code: Int): Boolean;
}