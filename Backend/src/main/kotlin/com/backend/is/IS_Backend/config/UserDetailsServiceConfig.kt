package com.backend.`is`.IS_Backend.config

import com.backend.`is`.IS_Backend.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Configuration
class UserDetailsConfig {
    
    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService = 
        UserDetailsService { username ->
            userRepository.findByEmail(username)
                .orElseThrow { UsernameNotFoundException("User not found") }
        }
}
