package com.backend.`is`.IS_Backend.model.domain

import com.backend.`is`.IS_Backend.enumerations.Roles
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
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


    @Enumerated(EnumType.STRING)
    private var userRole: Roles,
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    private var jitChange: JitChange? = null
) : UserDetails {

    private var count: Int = 0

    constructor(email: String, password: String) : this(null, email, password, Roles.ROLE_READER)

    constructor(email: String, password: String, role: Roles) : this(null, email, password, role)

    override fun getAuthorities(): Collection<out GrantedAuthority> =
        listOf(SimpleGrantedAuthority(userRole.name))

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
