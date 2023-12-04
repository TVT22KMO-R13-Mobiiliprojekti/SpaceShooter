package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch


class Hud(val batch: SpriteBatch) {
    private val font: BitmapFont = BitmapFont()
    val fontScale = 3f

    private val healthBarTexture: Texture = Texture("healthBar.png")
    private val maxHealthWidth = 1000f //
    private val healthBarHeight = 800f
    private var playerHealth = 100f //
    var score = 0

    fun draw() {
        // Draw score
        font.data.setScale(fontScale)
        font.draw(batch, "Score: $score", 20f, Gdx.graphics.height - 20f)

        // Draw healthbar
        batch.draw(
            healthBarTexture,
            -100f,
            Gdx.graphics.height - 500f,
            maxHealthWidth * (playerHealth / 100f),
            healthBarHeight
        )
    }

    fun updateHealth(health: Float) {
        playerHealth = health
    }

    fun dispose() {
        font.dispose()
        healthBarTexture.dispose()
    }
}

