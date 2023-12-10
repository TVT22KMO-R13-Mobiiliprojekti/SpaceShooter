package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Hud(private val batch: SpriteBatch) {

    private val player: Player? = null

    private val font: BitmapFont = BitmapFont()
    private val fontScale = 3f
    private var playerHealth = 100f // Max health 100
    private val healthBarFrameTexture: Texture = Texture("healthBar.png") // Frame picture
    private val healthContentTexture: Texture = Texture("healthContent.png") // Healthbar content picture
    private val healthFrameWidth = 440f // Frame Width
    private val healthFrameHeight = 82f // Frame Height
    private var contentWidth = 420f * (playerHealth / 100f)// Health width
    private var contentHeight = 40f // Health height
    private val frameX = 32f
    private val frameY = 1080f - 82f
    private var SCREEN_HEIGHT = 1080f
    private var SCREEN_WIDTH = 1920f
    private var score: Int = 0
    private var scoreText = "Score: 0"

    fun draw() {
        // Draw score
        font.data.setScale(fontScale)
        font.draw(batch, scoreText, SCREEN_WIDTH/2 - 128f, SCREEN_HEIGHT - 20f)

        // Draw healthbar frame first
        batch.draw(
            healthBarFrameTexture,
            frameX,
            frameY,
            healthFrameWidth,
            healthFrameHeight
        )

        // Draw healthbar content within the frame
        batch.draw(
            healthContentTexture,
            frameX + 10f,
            frameY + 20f,
            contentWidth,
            contentHeight
        )
    }

    fun updateHealthBar(playerHealth: Float) {
        player?.setPlayerHealth(playerHealth) // Update the player's health explicitly
        contentWidth = 420f * (playerHealth / 100f)
    }

    fun addScore(score: Int)
    {
        this.score += score
        this.scoreText = "Score: " + this.score.toString()
    }

    fun dispose() {
        font.dispose()
        healthBarFrameTexture.dispose()
        healthContentTexture.dispose()
    }
}
