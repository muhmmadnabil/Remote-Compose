package com.muhmmad.remotecompose.server

import com.muhmmad.remotecompose.server.routes.remoteUiRoutes
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, host = "0.0.0.0", port = 8080, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    routing {
        remoteUiRoutes()
    }
}
