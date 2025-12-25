package com.backend.`is`.IS_Backend.repository

import com.backend.`is`.IS_Backend.model.domain.JitChange
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface JitChangeRepository : JpaRepository<JitChange, Long> {

    fun findJitChangeByUserId(userid: Long?): JitChange?
}