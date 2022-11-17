package com.app.media.repository

import com.app.kotlinspringapp.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role,Int> {
	fun findByRoleName(roleName: String) : Role?
}