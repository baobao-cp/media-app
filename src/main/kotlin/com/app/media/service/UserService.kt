package com.app.kotlinspringapp.service

import com.app.kotlinspringapp.entity.User
import com.app.kotlinspringapp.payload.UserRegisterRequest

interface UserService {
	fun createUser(userRequest: UserRegisterRequest) : User?
}