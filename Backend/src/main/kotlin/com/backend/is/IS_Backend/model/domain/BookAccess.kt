package com.backend.`is`.IS_Backend.model.domain

import com.backend.`is`.IS_Backend.enumerations.AccessLevel
import jakarta.persistence.*

@Entity
@Table(name = "book_access", uniqueConstraints = [
    UniqueConstraint(columnNames = ["user_id", "book_id"]) // a user has a single access entry per book
])
class BookAccess(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    var book: Book,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var access: AccessLevel = AccessLevel.READ
)
