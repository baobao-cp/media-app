package com.app.media.security.oauth2.user

import org.springframework.security.authentication.BadCredentialsException

class Oauth2UserFactory {
	companion object {
		fun createOauth2UserInfo(registrationId: String, attributes: MutableMap<String, Any>): Oauth2UserInfo {

			return when (registrationId) {
				"github" -> GithubOauth2UserInfo(attributes);
				else -> throw BadCredentialsException("can not support this provider")
			}
		}
	}
}