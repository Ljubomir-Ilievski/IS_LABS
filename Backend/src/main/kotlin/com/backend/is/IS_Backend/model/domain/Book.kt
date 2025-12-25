package com.backend.`is`.IS_Backend.model.domain

import jakarta.persistence.*

@Entity
@Table(name = "book")
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var title: String = "",

    @Column(length = 2000)
    var description: String = ""
)
