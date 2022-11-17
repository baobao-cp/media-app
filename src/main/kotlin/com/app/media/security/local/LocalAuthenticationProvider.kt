package com.app.media.security.local

import com.app.kotlinspringapp.entity.LoginProvider
import com.app.kotlinspringapp.entity.User
import com.app.kotlinspringapp.repository.UserRepository
import com.app.kotlinspringapp.security.UserPrincipal
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class LocalAuthenticationProvider(
	val userRepository: UserRepository,
	val passwordEncoder: PasswordEncoder
) : AuthenticationProvider {

	override fun authenticate(authentication: Authentication?): Authentication {
		val auth = authentication ?: throw Exception("Authentication object is null")
		val formInputUsername: String = auth.name
		val formInputPassword: String = auth.credentials.toString()
		val user: User = userRepository
			.findByUsername(formInputUsername) ?: throw BadCredentialsException("Can not found this user")

		println(user)
		if (user.loginProvider != LoginProvider.LOCAL) {
			throw BadCredentialsException("This user only support for login provider ${user.loginProvider}")
		}
		if (!passwordEncoder.matches(formInputPassword, user.password)) {
			throw BadCredentialsException("Password is not correct")
		}
		val userPrincipal = UserPrincipal.build(user)
		return UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.authorities)
	}

	override fun supports(authentication: Class<*>?): Boolean {
		return authentication!!.let {
			UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(it)
		}
	}
}