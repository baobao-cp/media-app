package com.app.kotlinspringapp.security

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Override set timeout for session when authentication is successful
 */
class SessionTimeoutAuthSuccessHandler(
	authenticationSuccessUrl: String
) : SavedRequestAwareAuthenticationSuccessHandler() {

	init {
		defaultTargetUrl = authenticationSuccessUrl
	}

	/**
	 * Change the session timeout for authentication object
	 */
	override fun onAuthenticationSuccess(
		request: HttpServletRequest?,
		response: HttpServletResponse?,
		authentication: Authentication?
	) {
		val session = request?.getSession(false)

		session
			?. run { println("Session before set timout : ${session.maxInactiveInterval}") }
			?: run { println("Session is null") }

		/**
		 * session will change timeout when request is committed
		 */
		session?.maxInactiveInterval = 1000
		super.onAuthenticationSuccess(request, response, authentication)
	}
}