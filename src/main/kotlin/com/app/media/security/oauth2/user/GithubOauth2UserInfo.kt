package com.app.kotlinspringapp.security.oauth2.user

class GithubOauth2UserInfo(attributes: MutableMap<String, Any>) : Oauth2UserInfo(attributes) {

	override fun getId(): Int {
		return attributes["id"].toString().toInt()
	}

	override fun getName(): String {
		return attributes["login"].toString()
	}

	override fun getEmail(): String? {
		return attributes["email"]?.toString()
	}

	override fun getImageUrl(): String? {
		return attributes["avatar_url"]?.toString()
	}
}