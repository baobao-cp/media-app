package com.app.media.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

/**
 * data class not supported hibernate annotation like [fetch = FetchType. EAGER]
 * recommended to use class
 */
@Entity
class User(
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GenericGenerator(
		name = "user_id_generator",
		strategy = "com.elearning.kotlinspringapp.entity.IdGerenator",
	)
	@GeneratedValue(generator = "user_id_generator")
	val id: String = "KT",

	var username: String,
	var password: String,
	var email: String? = null,
	@Enumerated(EnumType.STRING)
	val loginProvider: LoginProvider,
	var avatarImage: String? = null,
	@ManyToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST], fetch = FetchType.EAGER)
	@JoinTable(
		name = "role_user",
		joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
		inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
	)
	val roles: Set<Role>
) {
	override fun toString(): String {
		return "User(id=$id, username='$username', password='$password', email=$email, loginProvider=$loginProvider, avatarImage=$avatarImage, roles=$roles)"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as User

		if (username != other.username) return false

		return true
	}

	override fun hashCode(): Int {
		return username.hashCode()
	}

}



