package com.muhmmad.remotecompose.server.ui

import androidx.compose.remote.core.CoreDocument
import androidx.compose.remote.core.RcProfiles
import androidx.compose.remote.core.operations.layout.managers.BoxLayout
import androidx.compose.remote.core.operations.utilities.ImageScaling
import androidx.compose.remote.creation.JvmRcPlatformServices
import androidx.compose.remote.creation.RemoteComposeWriter
import androidx.compose.remote.creation.modifiers.RecordingModifier

object RemoteUiDocumentFactory {
    fun buildDocument(): ByteArray {
        val writer = RemoteComposeWriter(
            1080,
            1920,
            "Remote Compose document from Ktor",
            CoreDocument.DOCUMENT_API_LEVEL,
            RcProfiles.PROFILE_ANDROIDX,
            JvmRcPlatformServices(),
        )

        val strings = DocumentStrings(
            badgeId = writer.textCreateId("REMOTE PAYLOAD"),
            statusId = writer.textCreateId("Live from Ktor"),
            titleId = writer.textCreateId("Server-driven Remote Compose"),
            subtitleId = writer.textCreateId(
                "The layout, cards, image, and text are all generated on the JVM server and streamed as a byte document."
            ),
            insightsTitleId = writer.textCreateId("What this payload includes"),
            insightOneId = writer.textCreateId("Generated hero image"),
            insightTwoId = writer.textCreateId("Nested rows and columns"),
            insightThreeId = writer.textCreateId("Reusable card blocks"),
            footerId = writer.textCreateId("Endpoint: GET /get-ui"),
        )
        val heroImageId = writer.addBitmap(HeroImageFactory.buildHeroImage())

        writer.root {
            writer.column(
                RecordingModifier()
                    .componentId(writer.nextId())
                    .fillMaxSize()
                    .background(0xFFF3F4F6.toInt())
                    .padding(48f)
                    .spacedBy(28f),
                BoxLayout.START,
                BoxLayout.TOP,
            ) {
                writer.chipRow(strings)

                writer.textBlock(
                    textId = strings.titleId,
                    fontSize = 68f,
                    color = 0xFF111827.toInt(),
                    fontWeight = 760f,
                    maxLines = 2,
                )

                writer.textBlock(
                    textId = strings.subtitleId,
                    fontSize = 31f,
                    color = 0xFF4B5563.toInt(),
                    fontWeight = 420f,
                    maxLines = 4,
                )

                writer.image(
                    RecordingModifier()
                        .componentId(writer.nextId())
                        .fillMaxWidth()
                        .height(420f),
                    heroImageId,
                    ImageScaling.SCALE_CROP,
                    1f,
                )

                writer.statsRow()
                writer.insightsCard(strings)

                writer.textBlock(
                    textId = strings.footerId,
                    fontSize = 24f,
                    color = 0xFF2563EB.toInt(),
                    fontWeight = 560f,
                    maxLines = 1,
                )
            }
        }

        return writer.encodeToByteArray()
    }
}

data class DocumentStrings(
    val badgeId: Int,
    val statusId: Int,
    val titleId: Int,
    val subtitleId: Int,
    val insightsTitleId: Int,
    val insightOneId: Int,
    val insightTwoId: Int,
    val insightThreeId: Int,
    val footerId: Int,
)
