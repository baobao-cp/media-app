package com.app.kotlinspringapp.event

import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class NotifyListener : ApplicationListener<NotifyEvent> {
	override fun onApplicationEvent(event: NotifyEvent) {
		println(event.message +" with " +"${event.resource}")
	}
}