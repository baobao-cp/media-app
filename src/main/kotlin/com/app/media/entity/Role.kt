package com.app.media.entity

import javax.persistence.*

@Entity
class Role(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Int = 0,
	val roleName: String,
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	val users: Set<User>
) {

	constructor(roleName: String) : this(0, roleName, setOf<User>())

	override fun toString(): String {
		return "Role(id=$id, roleName='$roleName')"
	}

}