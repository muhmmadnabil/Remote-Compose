package com.muhmmad.remotecompose.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

private const val REMOTE_UI_URL = "http://10.0.2.2:8080/get-ui"

class RemoteUiClient(
    private val endpoint: String = REMOTE_UI_URL,
) {
    suspend fun fetchDocument(): ByteArray = withContext(Dispatchers.IO) {
        val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 5_000
            readTimeout = 5_000
        }

        try {
            val responseCode = connection.responseCode
            check(responseCode in 200..299) {
                "Unexpected response $responseCode from $endpoint"
            }

            // The server returns a binary Remote Compose document, not JSON.
            connection.inputStream.use { it.readBytes() }
        } finally {
            connection.disconnect()
        }
    }
}
