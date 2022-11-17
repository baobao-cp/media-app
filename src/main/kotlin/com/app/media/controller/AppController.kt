package com.app.media.controller

import com.app.kotlinspringapp.event.NotifyEvent
import com.app.kotlinspringapp.payload.UserRegisterRequest
import com.app.kotlinspringapp.security.UserPrincipal
import com.app.kotlinspringapp.service.UserService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.context.request.RequestContextHolder
import javax.servlet.http.HttpSession
import kotlin.reflect.cast


@Controller
class AppController(
	private val userService: UserService,
	private val sessionRegistry: SessionRegistry,
	private val session: HttpSession,
	private val applicationEventPublisher : ApplicationEventPublisher
) {

	@GetMapping("/")
	fun homePage(model: Model): String {

		println("Home Page")
		return "redirect:/user/profile"
	}

	@GetMapping("/login")
	fun loginPage(model: Model): String {
		val context = SecurityContextHolder.getContext()
		val auth: Authentication = SecurityContextHolder.getContext().authentication

		if (auth is AnonymousAuthenticationToken || !auth.isAuthenticated) {
			model.addAttribute("register", UserRegisterRequest("", ""))
			return "login"
		}
		return "redirect:/user/profile"
	}

	@PostMapping("/register")
	fun homePage(model: Model, @ModelAttribute userRegisterRequest: UserRegisterRequest): String {
		println(userRegisterRequest)
		model.addAttribute("register", UserRegisterRequest("", ""))
		val registerUser = userService.createUser(userRegisterRequest)
		if (registerUser == null) {
			model.addAttribute("register_failure", "Can not Register This UserName")
			return "login"
		}
		applicationEventPublisher.publishEvent(NotifyEvent(registerUser.username,"Register Success"))
		model.addAttribute("register_success", "Register Success , Please Login !!")
		return "login"
	}

	/**
	 * Authentication
	 *  if LOCAL -> UsernamePasswordAuthenticationToken
	 *  if OAUTH -> OAuth2AuthenticationToken
	 */
	@GetMapping("/user/profile")
	fun home(model: Model, user: Authentication): String {
		val auth = user.principal as UserPrincipal
		println("Processing in user profile")
//		session.setAttribute("role", auth.authorities.joinToString(" "))
		model.addAttribute("user", auth)
		model.addAttribute("role", auth.authorities.joinToString(" "))

		return "index"
	}

	@GetMapping("users")
	fun users(): String {
		val list = sessionRegistry.allPrincipals
			.filterIsInstance<UserPrincipal>()
			.map {
				println(it::class)
				UserDetails::class.cast(it)
			}
		session.setAttribute("token", RequestContextHolder.currentRequestAttributes().sessionId)

		val listAttributes = session.attributeNames

		println("session Id" + RequestContextHolder.currentRequestAttributes().sessionId)
		println("All Session : " + sessionRegistry.allPrincipals.size)
		println("Size : " + list.size)
		println("List User Authenticated")
		println("List : " + list.joinToString(" ", transform = { it.username }))

		return "redirect:/user/profile"
	}

}