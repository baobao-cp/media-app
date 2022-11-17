package com.app.media.security

import com.app.kotlinspringapp.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class UserPrincipal(
	private val id: String,
	private val username: String,
	private val password: String,
	private val email: String?,
	private val authorities: MutableCollection<out GrantedAuthority>
) : UserDetails, OAuth2User {

	private var attributes: MutableMap<String, Any> = LinkedHashMap()

	companion object {
		fun build(user: User): UserPrincipal {
			return UserPrincipal(
				id = user.id,
				username = user.username,
				password = user.password,
				email = user.email,
				authorities = user.roles.map { role -> SimpleGrantedAuthority(role.roleName) }.toMutableSet()
			)
		}

		fun build(user: User, attributes: MutableMap<String, Any>): UserPrincipal {
			val userPrincipal: UserPrincipal = build(user)
			userPrincipal.attributes = attributes
			return userPrincipal
		}
	}

	fun getId(): String {
		return id
	}

	override fun getName(): String? {
		return email
	}

	override fun getAttributes(): MutableMap<String, Any> {
		return attributes
	}

	override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
		return authorities
	}

	override fun getUsername(): String {
		return username
	}

	override fun getPassword(): String {
		return password
	}

	override fun isAccountNonExpired(): Boolean {
		return true
	}

	override fun isAccountNonLocked(): Boolean {
		return true
	}

	override fun isCredentialsNonExpired(): Boolean {
		return true
	}

	override fun isEnabled(): Boolean {
		return true
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as UserPrincipal

		if (username != other.username) return false

		return true
	}

	override fun hashCode(): Int {
		return username.hashCode()
	}


}