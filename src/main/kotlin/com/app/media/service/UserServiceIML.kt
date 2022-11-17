package com.app.media.service

import com.app.kotlinspringapp.entity.LoginProvider
import com.app.kotlinspringapp.entity.Role
import com.app.kotlinspringapp.entity.User
import com.app.kotlinspringapp.payload.UserRegisterRequest
import com.app.kotlinspringapp.repository.RoleRepository
import com.app.kotlinspringapp.repository.UserRepository
import com.app.kotlinspringapp.utils.Constant
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceIML(
	private val userRepository: UserRepository,
	private val roleRepository: RoleRepository,
	private val passwordEncoder: PasswordEncoder
) : UserService {

	@Transactional
	override fun createUser(userRequest: UserRegisterRequest): User? {
		if (userRepository.existsUserByUsername(userRequest.username)) {
			return null
		}

		val role = roleRepository
			.findByRoleName(Constant.defaultRole) ?: Role(Constant.defaultRole)

		return userRepository.save(
			User(
				username = userRequest.username,
				password = passwordEncoder.encode(userRequest.password),
				email = null,
				avatarImage = null,
				loginProvider = LoginProvider.getLoginProvider(userRequest.loginProvider),
				roles = setOf(role)
			)
		)


	}

}
