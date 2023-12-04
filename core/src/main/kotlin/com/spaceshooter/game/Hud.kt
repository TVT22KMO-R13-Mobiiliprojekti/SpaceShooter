package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Hud(val batch: SpriteBatch) {
    private val font: BitmapFont = BitmapFont()
    val fontScale = 3f
    private var playerHealth = 100f // Max health 100
    private val healthBarFrameTexture: Texture = Texture("healthBar.png") // Frame picture
    private val healthContentTexture: Texture = Texture("healthContent.png") // Healthbar content picture
    private val healthFrameWidth = 2400f // Frame Width
    private val healthFrameHeight = 800f // Frame Height
    private val contentWidth = 450f * (playerHealth / 100f)// Health width
    private val contentHeight = 40f // Health height
    private val frameX = -500f
    private val frameY = Gdx.graphics.height - 500f
    var score = 0

    fun draw() {
        // Draw score
        font.data.setScale(fontScale)
        font.draw(batch, "Score: $score", 20f, Gdx.graphics.height - 20f)

        // Draw healthbar frame first
        batch.draw(
            healthBarFrameTexture,
            frameX,
            frameY,
            healthFrameWidth,
            healthFrameHeight
        )

//         Draw healthbar content within the frame
        batch.draw(
            healthContentTexture,
            frameX + 950f,
            frameY + 425,
            contentWidth,
            contentHeight
        )
    }

    fun updateHealth(health: Float) {
        playerHealth = health
    }

    fun dispose() {
        font.dispose()
        healthBarFrameTexture.dispose()
        healthContentTexture.dispose()
    }
}
