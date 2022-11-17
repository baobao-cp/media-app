package com.app.kotlinspringapp.service

import com.app.kotlinspringapp.entity.LoginProvider
import com.app.kotlinspringapp.entity.User
import com.app.kotlinspringapp.repository.RoleRepository
import com.app.kotlinspringapp.repository.UserRepository
import com.app.kotlinspringapp.utils.Constant
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
internal class UserServiceIMLTest {
	@Autowired
	lateinit var userRepository: UserRepository

	@Autowired
	lateinit var roleRepository: RoleRepository

	@Test
	@Transactional
	fun getUser() {
		val role = roleRepository.findByRoleName(Constant.defaultRole)!!

		val user = User(username = "123",
			password = "123",
			loginProvider = LoginProvider.LOCAL,
			roles = setOf(role)
		)
		println(userRepository.save(user))
	}

}