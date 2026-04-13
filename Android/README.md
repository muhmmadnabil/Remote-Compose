# Android App README

This module is the Android client for the sample. Its job is simple:

1. request a Remote Compose document from the Ktor server
2. receive that document as raw bytes
3. render it with `RemoteComposePlayer`

If you want the conceptual explanation first, start here:

- [Remote Compose Guide](../README.md)

That guide explains:

- what Remote Compose is
- what a document is
- why the server returns `ByteArray`
- what the player does
- how the producer/player model works

## App Structure

```text
app/src/main/java/com/muhmmad/remotecompose
├── MainActivity.kt
└── network
    └── RemoteUiClient.kt
```

## Files

### `MainActivity.kt`

This file coordinates the Android-side rendering flow.

- creates `RemoteComposePlayer`
- starts the fetch operation
- passes the returned bytes to `player.setDocument(bytes)`

The activity stays intentionally small so the Remote Compose flow is easy to follow.

### `network/RemoteUiClient.kt`

This file contains the HTTP client that downloads the document from the Ktor server.

It reads the response as bytes because the server returns a binary Remote Compose document, not JSON.

## URL Notes

The app currently uses:

```text
http://10.0.2.2:8080/get-ui
```

That URL is correct for the Android emulator, where `10.0.2.2` points to the host machine.

For a real phone, replace it with your computer's LAN IP.

## Run

1. Start the Ktor server.
2. Run the Android app.
3. The app will fetch `/get-ui` and render the document.
