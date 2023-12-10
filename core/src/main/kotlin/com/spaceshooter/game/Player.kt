package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Player(): GameObject() {

    private var hud: Hud? = null
    private var playerHealth = 100f
    private var hasTakenDamage = false


    fun setHud(hud: Hud) {
        this.hud = hud
    }

    override fun update(deltaTime: Float)
    {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime

        sprite.setCenter(position.x, position.y)
        hitBox.setCenter(position.x, position.y)

        limitMovement()

        hasTakenDamage = false

    }
    //override fun render(spriteBatch: SpriteBatch)
    //{
    //   sprite.draw(spriteBatch)
        //Gdx.app.log("SPRITE INFO: ", "Position x: " + sprite.x + " Position y: " + sprite.y + "Sprite size : " + sprite.width)
        //Gdx.app.log("SPRITE INFO: ", "Texture height: " + sprite.texture.height)
        //Gdx.app.log("Texture Info", "Width: ${sprite.texture.depth}, Height: ${sprite.texture.height}")
    //}

    private fun limitMovement()
    {
        if(position.x < 0.0f + size.x / 2)
            position.x = 0.0f + size.x / 2
        if(position.x > 1920.0f - size.x / 2)
            position.x = 1920.0f - size.x / 2
        if(position.y < 0.0f + size.y / 2)
            position.y = 0.0f + size.y / 2
        if(position.y > 1080.0f - size.y / 2)
            position.y = 1080.0f - size.y / 2
    }

    fun checkCollisionWithEnemy(enemy: Enemy): Boolean {
        var hasCollided = false // Default value

        if (hitBox.overlaps(enemy.hitBox)) {
            Gdx.app.log("Collision", "Player collided with enemy")
            val damageAmount = 10f // 10 is the amount of damage taken
            if (!hasTakenDamage) {
                takeDamage(damageAmount)
                hasTakenDamage = true // Set a flag to indicate damage has been taken in this frame
                hasCollided = true
            }
        }
        if (!hasCollided) {
            hasTakenDamage = false
        }
        return hasCollided
    }

    private fun takeDamage(damageAmount: Float) {
        playerHealth -= damageAmount

        if (playerHealth < 0f) {
            playerHealth = 0f // Ensure health doesn't go below 0
        }
        hud?.updateHealthBar(playerHealth) // Call the updateHealthBar function to refresh the health bar display
    }

    fun setPlayerHealth(health: Float) {
        playerHealth = health

        if (playerHealth > 100f) {
            playerHealth = 100f // Ensure health doesn't exceed the maximum value (100)
        }

        hud?.updateHealthBar(playerHealth) // Call the updateHealthBar function when the player's health is set
    }

    fun getPlayerHealth(): Float {
        return playerHealth
    }
}
