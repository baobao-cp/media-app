package com.app.media.entity

enum class LoginProvider {
	LOCAL, GITHUB, FACEBOOK, GOOGLE;
	companion object{

		fun getLoginProvider(provider: String): LoginProvider {
			return when(provider.uppercase()){
				"GITHUB" ->  GITHUB
				"FACEBOOK" -> FACEBOOK
				"GOOGLE" -> GOOGLE
				else -> LOCAL
			}
		}
	}

}