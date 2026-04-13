package com.muhmmad.remotecompose.server.ui

import androidx.compose.remote.core.operations.layout.managers.BoxLayout
import androidx.compose.remote.core.operations.layout.managers.TextLayout
import androidx.compose.remote.creation.RemoteComposeWriter
import androidx.compose.remote.creation.modifiers.RecordingModifier

internal fun RemoteComposeWriter.chipRow(strings: DocumentStrings) {
    row(
        RecordingModifier()
            .componentId(nextId())
            .fillMaxWidth()
            .spacedBy(20f),
        BoxLayout.START,
        BoxLayout.CENTER,
    ) {
        chip(strings.badgeId, 0xFF1D4ED8.toInt(), 0xFFE0E7FF.toInt())
        chip(strings.statusId, 0xFF047857.toInt(), 0xFFD1FAE5.toInt())
    }
}

internal fun RemoteComposeWriter.statsRow() {
    row(
        RecordingModifier()
            .componentId(nextId())
            .fillMaxWidth()
            .spacedBy(20f),
        BoxLayout.START,
        BoxLayout.TOP,
    ) {
        statCard("Latency", "42 ms", 0xFFDBEAFE.toInt(), 0xFF1D4ED8.toInt())
        statCard("Payload", "2.9 KB", 0xFFFCE7F3.toInt(), 0xFFBE185D.toInt())
        statCard("Version", "alpha08", 0xFFECFCCB.toInt(), 0xFF4D7C0F.toInt())
    }
}

internal fun RemoteComposeWriter.insightsCard(strings: DocumentStrings) {
    column(
        RecordingModifier()
            .componentId(nextId())
            .fillMaxWidth()
            .background(0xFFFFFFFF.toInt())
            .padding(28f)
            .spacedBy(18f),
        BoxLayout.START,
        BoxLayout.TOP,
    ) {
        textBlock(
            textId = strings.insightsTitleId,
            fontSize = 34f,
            color = 0xFF111827.toInt(),
            fontWeight = 680f,
            maxLines = 1,
        )

        bulletRow(strings.insightOneId, 0xFF2563EB.toInt())
        bulletRow(strings.insightTwoId, 0xFF7C3AED.toInt())
        bulletRow(strings.insightThreeId, 0xFFEA580C.toInt())
    }
}

private fun RemoteComposeWriter.chip(textId: Int, textColor: Int, background: Int) {
    textComponent(
        RecordingModifier()
            .componentId(nextId())
            .background(background)
            .padding(20f, 12f, 20f, 12f),
        textId,
        textColor,
        22f,
        0,
        700f,
        null,
        TextLayout.TEXT_ALIGN_START,
        TextLayout.OVERFLOW_CLIP,
        1,
    ) {}
}

private fun RemoteComposeWriter.statCard(
    label: String,
    value: String,
    background: Int,
    accent: Int,
) {
    val labelId = textCreateId(label)
    val valueId = textCreateId(value)

    column(
        RecordingModifier()
            .componentId(nextId())
            .horizontalWeight(1f)
            .background(background)
            .padding(24f)
            .spacedBy(10f),
        BoxLayout.START,
        BoxLayout.TOP,
    ) {
        textBlock(labelId, 22f, 0xFF6B7280.toInt(), 500f, 1)
        textBlock(valueId, 38f, accent, 760f, 1)
    }
}

private fun RemoteComposeWriter.bulletRow(textId: Int, accent: Int) {
    val dotId = textCreateId("•")

    row(
        RecordingModifier()
            .componentId(nextId())
            .fillMaxWidth()
            .spacedBy(14f),
        BoxLayout.START,
        BoxLayout.TOP,
    ) {
        textComponent(
            RecordingModifier()
                .componentId(nextId()),
            dotId,
            accent,
            30f,
            0,
            800f,
            null,
            TextLayout.TEXT_ALIGN_START,
            TextLayout.OVERFLOW_CLIP,
            1,
        ) {}

        textComponent(
            RecordingModifier()
                .componentId(nextId())
                .horizontalWeight(1f),
            textId,
            0xFF374151.toInt(),
            26f,
            0,
            500f,
            null,
            TextLayout.TEXT_ALIGN_START,
            TextLayout.OVERFLOW_CLIP,
            2,
        ) {}
    }
}

internal fun RemoteComposeWriter.textBlock(
    textId: Int,
    fontSize: Float,
    color: Int,
    fontWeight: Float,
    maxLines: Int,
) {
    // We keep text creation centralized so the document layout reads like a UI tree.
    textComponent(
        RecordingModifier()
            .componentId(nextId())
            .fillMaxWidth(),
        textId,
        color,
        fontSize,
        0,
        fontWeight,
        null,
        TextLayout.TEXT_ALIGN_START,
        TextLayout.OVERFLOW_CLIP,
        maxLines,
    ) {}
}
