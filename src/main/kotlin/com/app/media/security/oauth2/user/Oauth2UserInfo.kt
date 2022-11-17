package com.app.kotlinspringapp.security.oauth2.user

abstract class Oauth2UserInfo(val attributes: MutableMap<String, Any>) {
	abstract fun getId(): Int
	abstract fun getName(): String
	abstract fun getEmail(): String?
	abstract fun getImageUrl(): String?
}