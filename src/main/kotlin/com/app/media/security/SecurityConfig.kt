package com.app.media.security

import com.app.kotlinspringapp.security.filter.ProxyFilter
import com.app.kotlinspringapp.security.oauth2.Oauth2AuthenticationService
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.session.HttpSessionEventPublisher


@EnableWebSecurity
class SecurityConfig(
	val oauth2AuthenticationService: Oauth2AuthenticationService,
	val proxyFilter: ProxyFilter
) {
	/**
	 * instance is DefaultSecurityFilterChain
	 * default config HttpSecurityConfiguration
	 */
	@Bean
	fun filterChain(http: HttpSecurity): SecurityFilterChain {
		http
			/**
			 * When csrf is not disabled , /logout processing will change to POST method(default GET)
			 * For Render csrf token need use th:action for form login
			 */
			.csrf { csrf ->
//				csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			}

			.authorizeRequests { auth ->
				auth
					.antMatchers("/static/**", "/css/**", "/js/**", "/img/**", "/icon/**", "/favicon.ico").permitAll()
					.antMatchers("/login", "/register", "/oauth/**", "/error").permitAll()
					.anyRequest().authenticated()
			}

			.sessionManagement { session ->
				session
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
					.maximumSessions(1)
					/**
					 *	when maximumSessions , maxSessionsPreventsLogin() ->
					 * 	if is login other browser:
					 * 	false -> can log in but remove before login
					 * 	true  -> can not log in before logout in other browser
					 */
					.maxSessionsPreventsLogin(false)
					.expiredUrl("/login?expired")
					.sessionRegistry(sessionRegistry())
			}

			.formLogin { formLogin ->
				formLogin
					.loginPage("/login")
					.loginProcessingUrl("/form/login")
					.successHandler(SessionTimeoutAuthSuccessHandler("/user/profile"))
//					.defaultSuccessUrl("/user/profile")
					.failureUrl("/login?error")
			}
			/**
			 * DEFAULT_FILTER_PROCESSES_URI = "/login/oauth2/code/{registrationId}"
			 * DEFAULT_AUTHORIZATION_REQUEST_BASE_URI = "/oauth2/authorization"
			 */
			.oauth2Login { oauth ->
				oauth
					.loginPage("/login")
					.successHandler(SessionTimeoutAuthSuccessHandler("/user/profile"))
//					.defaultSuccessUrl("/user/profile")
					.userInfoEndpoint().userService(oauth2AuthenticationService)
//					.authorizationEndpoint { authEndpoint ->
//						authEndpoint.baseUri("/oauth/authorization/login")
//					}
			}
			/**
			 * default /logout processing method is GET
			 * But csrf token is valid , processing method will change to POST
			 */
			.logout { logout ->
				logout.logoutUrl("/logout")
					.deleteCookies("SESSION")
					.clearAuthentication(true)
					.invalidateHttpSession(true)
			}

			.addFilterBefore(proxyFilter, UsernamePasswordAuthenticationFilter::class.java)


		/**
		 *  build() processing logic :
		 *  config() method will call in AbstractConfiguredSecurityBuilder to change [**Config] class
		 *  to [**Filter] class , and add to Lists Filter for FilterChain with method addFilter()
		 *  in HttpSecurity
		 */
		return http.build()
	}

	@Bean
	fun clientRegistrationRepository(): ClientRegistrationRepository {
		return InMemoryClientRegistrationRepository(this.oauthClientRegistration())
	}


	fun oauthClientRegistration(): ClientRegistration {
		return CommonOAuth2Provider.GITHUB
			.getBuilder("github")
			.clientId("022479a02dc175034a4d")
			.clientSecret("f0aa62b01505f87a32039c392a9bc2dc3e0f85d4")
			.build()
	}

	@Bean
	fun sessionRegistry(): SessionRegistry? {
		return SessionRegistryImpl()
	}

	@Bean
	fun httpSessionEventPublisher(): ServletListenerRegistrationBean<HttpSessionEventPublisher>? {
		return ServletListenerRegistrationBean(HttpSessionEventPublisher())
	}


}