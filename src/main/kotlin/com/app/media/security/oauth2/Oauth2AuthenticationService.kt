package com.app.media.security.oauth2

import com.app.kotlinspringapp.entity.LoginProvider
import com.app.kotlinspringapp.entity.Role
import com.app.kotlinspringapp.entity.User
import com.app.kotlinspringapp.repository.RoleRepository
import com.app.kotlinspringapp.repository.UserRepository
import com.app.kotlinspringapp.security.UserPrincipal
import com.app.kotlinspringapp.security.oauth2.user.Oauth2UserFactory
import com.app.kotlinspringapp.security.oauth2.user.Oauth2UserInfo
import com.app.kotlinspringapp.utils.Constant
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class Oauth2AuthenticationService(
	private val roleRepository: RoleRepository,
	private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

	@Throws(OAuth2AuthenticationException::class)
	override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
		val oauth2User = super.loadUser(userRequest)
		val registrationId = userRequest!!.clientRegistration.registrationId
		val oauth2UserInfo: Oauth2UserInfo = Oauth2UserFactory.createOauth2UserInfo(
			registrationId,
			oauth2User.attributes
		)
		val user = processUser(oauth2UserInfo, registrationId)


		println("In Oauth Authenticate Processing ${user.username} $registrationId")
		return UserPrincipal.build(user, oauth2User.attributes)
	}

	/**
	 * processing user if user is existing database, update user.
	 * otherwise , create user
	 */
	@Transactional
	fun processUser(oauth2UserInfo: Oauth2UserInfo, registrationId: String): User {
		var user = userRepository.findByUsername(oauth2UserInfo.getName())
		if (user == null) {
			val role = roleRepository
				.findByRoleName(Constant.defaultRole) ?: Role(Constant.defaultRole)

			user = User(
				username = oauth2UserInfo.getName(),
				password = "",
				email = oauth2UserInfo.getEmail(),
				loginProvider = LoginProvider.getLoginProvider(registrationId),
				avatarImage = oauth2UserInfo.getImageUrl(),
				roles = setOf(role)
			)
			return userRepository.save(user)
		}

		if (user.loginProvider != LoginProvider.getLoginProvider(registrationId)) {
			throw BadCredentialsException("This user can only login with $registrationId")
		}
		return updateUser(user, oauth2UserInfo)

	}

	private fun updateUser(user: User, oauth2UserInfo: Oauth2UserInfo): User {
		user.username = oauth2UserInfo.getName()
		user.email = oauth2UserInfo.getEmail()
		user.avatarImage = oauth2UserInfo.getImageUrl()
		return userRepository.save(user)
	}

}

