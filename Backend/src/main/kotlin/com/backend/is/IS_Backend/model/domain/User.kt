package com.backend.`is`.IS_Backend.model.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "app_user")
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @Column(unique = true, nullable = false)
    open var email: String = "",

    @get:JsonIgnore
    @Column(name = "password", nullable = false)
    private var passwordValue: String = "",

    private var count: Int = 0
) : UserDetails {

    constructor(email: String, password: String) : this(null, email, password)

    override fun getAuthorities(): Collection<out GrantedAuthority> = emptyList()

    override fun getPassword(): String = passwordValue

    override fun getUsername(): String = email


    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
    fun getCount(): Int = count

    fun setCount(count: Int) {
        this.count = count
    }
}
