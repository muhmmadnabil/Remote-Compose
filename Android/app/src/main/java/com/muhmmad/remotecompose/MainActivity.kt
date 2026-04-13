package com.muhmmad.remotecompose

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.remote.player.view.RemoteComposePlayer
import androidx.lifecycle.lifecycleScope
import com.muhmmad.remotecompose.network.RemoteUiClient
import kotlinx.coroutines.launch

private const val TAG = "RemoteCompose"

class MainActivity : ComponentActivity() {
    private val remoteUiClient = RemoteUiClient()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val player = RemoteComposePlayer(this)
        setContentView(player)

        lifecycleScope.launch {
            runCatching {
                remoteUiClient.fetchDocument()
            }.onSuccess { bytes ->
                // RemoteComposePlayer renders the serialized document returned by the server.
                player.setDocument(bytes)
            }.onFailure { error ->
                Log.e(TAG, "Failed to load Remote Compose document", error)
            }
        }
    }
}
