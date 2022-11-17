package com.app.kotlinspringapp.payload

data class UserRegisterRequest(
	val username: String,
	var password: String,
	val email: String = "",
	val loginProvider: String = "LOCAL"
)