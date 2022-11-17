package com.app.kotlinspringapp.security.filter

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ProxyFilter : OncePerRequestFilter() {
	companion object {
		private var filterTime = 1
	}

	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
		val requestPath = request.servletPath
		val filterMessage = "$filterTime Time ProxyFilter is running with {$requestPath} "

		/**
		 * session instance :
		 *  -- StandardSessionFacade : when session store in Tomcat --
		 *  -- HttpServletRequestWrapper : when session store in database/redis
		 */
		val session = request.getSession(false)
		session
			?.run { println("$filterMessage  And Session is already") }
			?: run { println("$filterMessage And session is null") }

		++filterTime
		try {
			filterChain.doFilter(request, response)
		} finally {
			println("Filter Processing is End")
		}
	}


}