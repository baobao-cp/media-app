package com.app.media.entity

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.io.Serializable
import java.sql.SQLException

class IdGerenator : IdentifierGenerator {
	private val prefix = "KT"

	override fun generate(session: SharedSessionContractImplementor?, `object`: Any?): Serializable? {
		val user: User = `object` as User
		val provider = user.loginProvider
		return null
	}

	fun generateId(session: SharedSessionContractImplementor?): String {
		val connection = session?.connection() ?: throw Error("Can not connect to database")

		try {
			val query = "select count(id) as Id from User"
			val statement = connection.createStatement()
			val resultSet = statement.executeQuery(query)
			if (resultSet.next()) {
				val id = resultSet.getInt(1)
				return prefix + id
			}
		} catch (exception: SQLException) {
			exception.printStackTrace()
		}
		return ""
	}
}