package com.example.test1.AccessAuth

import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

class HttpSessionListenerImpl : HttpSessionListener {

    override fun sessionCreated(se: HttpSessionEvent) {
        se.session.maxInactiveInterval = 1800
    }

    override fun sessionDestroyed(se: HttpSessionEvent?) {
    }
}