package com.muhmmad.remotecompose.server.ui

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.GradientPaint
import java.awt.RenderingHints
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage

object HeroImageFactory {
    fun buildHeroImage(): BufferedImage {
        val width = 1600
        val height = 900
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics = image.createGraphics()

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
        )

        graphics.paint = GradientPaint(
            0f,
            0f,
            Color(0x0F172A),
            width.toFloat(),
            height.toFloat(),
            Color(0x1D4ED8),
        )
        graphics.fillRect(0, 0, width, height)

        graphics.color = Color(255, 255, 255, 22)
        graphics.fillOval(1100, -120, 420, 420)
        graphics.fillOval(-80, 520, 360, 360)

        graphics.color = Color(255, 255, 255, 28)
        graphics.stroke = BasicStroke(3f)
        graphics.draw(RoundRectangle2D.Float(130f, 120f, 1340f, 660f, 48f, 48f))

        graphics.color = Color(255, 255, 255, 34)
        graphics.fillRoundRect(190, 220, 300, 120, 32, 32)
        graphics.fillRoundRect(520, 220, 410, 120, 32, 32)
        graphics.fillRoundRect(190, 390, 740, 56, 28, 28)
        graphics.fillRoundRect(190, 470, 610, 56, 28, 28)
        graphics.fillRoundRect(190, 610, 210, 76, 32, 32)
        graphics.fillRoundRect(430, 610, 210, 76, 32, 32)
        graphics.fillRoundRect(670, 610, 210, 76, 32, 32)

        graphics.color = Color.WHITE
        graphics.font = Font("SansSerif", Font.BOLD, 82)
        graphics.drawString("Remote Compose", 190, 180)

        graphics.font = Font("SansSerif", Font.PLAIN, 34)
        graphics.drawString("Rendered from a Ktor endpoint as a binary UI document", 190, 560)
        graphics.dispose()

        return image
    }
}
