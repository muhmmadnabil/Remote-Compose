package com.muhmmad.remotecompose.server.routes

import com.muhmmad.remotecompose.server.ui.RemoteUiDocumentFactory
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.remoteUiRoutes() {
    get("/") {
        call.respondText("Remote Compose server is running")
    }

    get("/get-ui") {
        // The Android client consumes a serialized Remote Compose document as raw bytes.
        call.respondBytes(
            bytes = RemoteUiDocumentFactory.buildDocument(),
            contentType = ContentType.Application.OctetStream,
        )
    }
}
