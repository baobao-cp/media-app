package com.app.kotlinspringapp.event

import org.springframework.context.ApplicationEvent

data class NotifyEvent(val resource: Any, val message: String) : ApplicationEvent(resource)