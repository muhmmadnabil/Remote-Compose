# Ktor Remote Compose Server

This module is a small Ktor server that generates a Remote Compose document on the JVM and returns it as a `ByteArray` from `GET /get-ui`.

The Android app does not receive JSON or Compose source code from this server. It receives a binary Remote Compose document that `RemoteComposePlayer` can render directly.

## How It Works

The flow is:

1. Ktor receives an HTTP request at `/get-ui`.
2. The route calls `RemoteUiDocumentFactory.buildDocument()`.
3. `RemoteUiDocumentFactory` creates a `RemoteComposeWriter`.
4. The writer builds a UI tree using rows, columns, text, and images.
5. The writer serializes that UI tree to bytes with `encodeToByteArray()`.
6. Ktor returns those bytes as `application/octet-stream`.

## Project Structure

```text
src/main/kotlin/com/muhmmad/remotecompose/server
├── Application.kt
├── routes
│   └── RemoteUiRoutes.kt
└── ui
    ├── HeroImageFactory.kt
    ├── RemoteUiComponents.kt
    └── RemoteUiDocumentFactory.kt
```

## File-by-File Explanation

### `Application.kt`

This is the server entrypoint.

- `main()`
  Starts the Ktor Netty server on `0.0.0.0:8080`.
  We keep this function small so boot logic stays easy to read.

- `Application.module()`
  Registers the server routes.
  This is separated from `main()` because Ktor commonly uses `module()` as the application setup function.

### `routes/RemoteUiRoutes.kt`

This file contains HTTP routing only.

- `remoteUiRoutes()`
  Defines:
  - `GET /` as a small health/status endpoint.
  - `GET /get-ui` as the main endpoint that returns Remote Compose bytes.

Why this file exists:

- It keeps transport concerns separate from UI generation.
- If you later add more endpoints like `/get-ui/v2` or `/health`, you can keep them together without touching UI-building code.

### `ui/RemoteUiDocumentFactory.kt`

This file builds the full document.

- `RemoteUiDocumentFactory.buildDocument()`
  This is the main function that creates the Remote Compose payload.

  It does a few important things:

  - Creates `RemoteComposeWriter`
    This is the object that records the Remote Compose operations.

  - Creates text IDs
    Strings are stored once and then referenced in the UI tree.
    This is why you see `writer.textCreateId(...)`.

  - Creates the hero image ID
    The generated image is stored as bitmap data once and reused in the layout.

  - Builds the root layout
    The function uses `writer.root { ... }` and then adds rows, columns, text, and images inside it.

  - Serializes the result
    `writer.encodeToByteArray()` returns the final binary document sent to the Android app.

- `DocumentStrings`
  A small data class that groups all text IDs.
  I added this to avoid a long list of loose variables moving through helper functions.

Why this file exists:

- It acts as the orchestration layer.
- It keeps the screen structure readable at a high level.
- It lets the reusable UI helper functions stay focused on small pieces.

### `ui/RemoteUiComponents.kt`

This file contains reusable builder helpers for the document.

- `chipRow(strings)`
  Builds the top row of status chips.

- `statsRow()`
  Builds the row of stat cards.

- `insightsCard(strings)`
  Builds the white content card with bullet rows.

- `chip(...)`
  Builds a single chip.

- `statCard(...)`
  Builds one statistic card.

- `bulletRow(...)`
  Builds one bullet item in the insights section.

- `textBlock(...)`
  A shared helper for common text rendering.
  This reduces duplication because the same `textComponent(...)` call shape appears many times.

Why this file exists:

- It keeps the main document file from turning into one very long function.
- It makes repeated patterns reusable and easier to change.
- It reads more like UI composition, even though it is built with low-level writer calls.

### `ui/HeroImageFactory.kt`

This file generates the bitmap used by the document.

- `HeroImageFactory.buildHeroImage()`
  Creates a `BufferedImage` with Java AWT, paints the background and decorative shapes, then draws the title text.

Why this file exists:

- Image generation is a separate concern from layout generation.
- Keeping it isolated makes the document factory easier to scan.
- If you later want to replace the generated image with a downloaded image or file-based image, the change is localized.

## Why `RemoteComposeWriter` Is Used

On Android, you used `captureSingleRemoteDocument(...)` earlier. That API depends on Android `Context`, so it is not the right choice for a plain Ktor/JVM server.

For a real JVM server, we instead use:

- `JvmRcPlatformServices()`
- `RemoteComposeWriter(...)`

This is the lower-level server-friendly approach. It lets the JVM create the Remote Compose document directly, then serialize it to bytes.

## Why The Endpoint Returns `application/octet-stream`

The response is not JSON, HTML, or XML.

It is a binary Remote Compose document, so `application/octet-stream` is the correct and simplest content type.

## How the Android App Uses This

The Android app:

1. Calls `GET /get-ui`
2. Reads the raw bytes
3. Passes them to `RemoteComposePlayer.setDocument(bytes)`

That means the server is fully responsible for producing the UI payload, and the app is responsible for displaying it.

## Run the Server

```bash
cd "/Users/mohamednabil/RemoteCompose/Ktor Server"
./gradlew run
```

The server will start on:

```text
http://0.0.0.0:8080
```

Useful endpoints:

- `GET /`
- `GET /get-ui`

## Summary

This server is intentionally split into:

- startup code
- route definitions
- document orchestration
- reusable UI helpers
- image generation

That structure makes it much easier to explain, extend, test, and publish.
