# Remote Compose Guide

This guide explains the main idea behind Remote Compose, how it works in this project, and what terms like `document`, `ByteArray`, and `RemoteComposePlayer` mean in practice.

## What Remote Compose Is

Remote Compose is a way to describe UI in one place and render it in another place.

That means:

- one side creates the UI description
- another side receives that description
- the receiving side renders it with a player

In this project:

- the Ktor server is the producer
- the Android app is the renderer

The key idea is that the server is not sending Jetpack Compose source code to the phone.

Instead, the server builds a Remote Compose document, serializes it, and sends the serialized result to the app.

## What A Remote Compose Document Is

A Remote Compose document is the binary representation of a UI.

You can think of it as:

- a packaged UI description
- a sequence of layout, text, image, and rendering instructions
- a format the player understands

The document can contain things like:

- rows
- columns
- text nodes
- image nodes
- layout sizing rules
- styling and drawing instructions

It is not:

- a `.kt` file
- a Compose function source file
- JSON UI schema in this sample

## Why The Server Returns `ByteArray`

The server builds the document and then serializes it into bytes.

That is why the Android app reads:

```kotlin
connection.inputStream.use { it.readBytes() }
```

Those bytes are the full Remote Compose document.

So when we say the app receives a `ByteArray`, we mean:

"the app has received the serialized UI document from the server."

## Why `ByteArray` Matters

`ByteArray` is important because it is the transport format between the server and the Android app.

The flow is:

1. server builds UI
2. server serializes UI into bytes
3. bytes travel over HTTP
4. app gives those bytes to the player

This is why the app does not parse JSON and does not compile code from the server.

## What `RemoteComposePlayer` Is

`RemoteComposePlayer` is the Android-side renderer for the Remote Compose document.

Its role is:

- accept the serialized document
- interpret the operations inside it
- draw the final UI on screen

In this project, the most important line is:

```kotlin
player.setDocument(bytes)
```

That line means:

"Here is a Remote Compose document. Please render it."

So `RemoteComposePlayer` is the thing that turns the raw bytes back into a visible UI.

## Producer vs Renderer

Remote Compose becomes much easier to understand when you split it into two roles.

### Producer

The producer creates the document.

In this project, the producer is the Ktor server.

The server uses:

- `RemoteComposeWriter`
- JVM platform services
- helper functions that describe rows, columns, text, and images

The server decides what UI exists.

### Renderer

The renderer displays the document.

In this project, the renderer is the Android app.

The app uses:

- `RemoteUiClient` to download the bytes
- `RemoteComposePlayer` to render those bytes

The Android app does not decide the screen structure in this sample. It mostly hosts and renders what the server sends.

## How It Works In This Project

The exact flow is:

1. The Android app starts.
2. `MainActivity` creates `RemoteComposePlayer`.
3. The app calls the Ktor server at `/get-ui`.
4. The Ktor server builds a Remote Compose document.
5. The server serializes that document to `ByteArray`.
6. The server returns the bytes in the HTTP response.
7. The app reads the response bytes.
8. The app calls `player.setDocument(bytes)`.
9. `RemoteComposePlayer` renders the UI on screen.

## What The App Does Not Do

The Android app does not:

- execute Compose source code received from the server
- rebuild the server UI locally using Jetpack Compose
- parse a JSON layout description in this sample
- generate the document itself

That separation is intentional.

## What The Server Does

The server is responsible for:

- creating the UI structure
- creating text IDs
- generating images
- arranging rows and columns
- serializing everything into the final document

That means changes to the server’s document generation change what the Android app renders.

## Why This Pattern Is Useful

This design can be useful when you want:

- server-controlled UI payloads
- a small rendering client
- a transport-friendly binary format
- separation between UI generation and UI rendering

It also makes it easier to think about the system as:

- build once on the server
- render on the client

## Relation To Jetpack Compose

Remote Compose is related in spirit to Compose, but it is not the same runtime model as sending normal `@Composable` Kotlin functions across the network.

Jetpack Compose source code is compiled Kotlin code in your app.

Remote Compose is a document format consumed by a player.

So in this project:

- the server does not send Kotlin source
- the app does not compile or evaluate source
- the app only renders a document

## Mental Model

The easiest mental model is:

- `RemoteComposeWriter` writes a UI document
- `ByteArray` is the serialized document
- `RemoteComposePlayer` reads and renders that document

If you keep those three roles in mind, the whole architecture becomes much easier to follow.

## Where To Look In This Project

If you want to connect the theory to the code:

- Ktor server README:
  [Ktor Server README](./Ktor%20Server/README.md)
- Android README:  
  [Android README](./Android/README.md)
- Android app entrypoint:
  `app/src/main/java/com/muhmmad/remotecompose/MainActivity.kt`
- Android network client:
  `app/src/main/java/com/muhmmad/remotecompose/network/RemoteUiClient.kt`

## Summary

Remote Compose in this sample works like this:

- the server creates a document
- the document is serialized to `ByteArray`
- the app downloads the bytes
- `RemoteComposePlayer` renders the result

That is the core idea behind the whole project.
