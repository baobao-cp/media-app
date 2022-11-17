package com.app.media.repository

import com.app.kotlinspringapp.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User,Int>{
	fun findByUsername(username: String) : User?
	fun existsUserByUsername(username : String) : Boolean
}