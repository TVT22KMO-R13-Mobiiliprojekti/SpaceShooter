package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Player(): GameObject() {

    private var hud: Hud? = null // Initialize it as nullable

    private var playerHealth = 100f // Initial max health 100

    private var hasTakenDamage = false // Flag to track if damage has been taken in the current frame


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

    fun checkCollisionWithEnemy(enemy: Enemy) {
        if (hitBox.overlaps(enemy.hitBox)) {
            // Collision detected between player and enemy
            Gdx.app.log("Collision", "Player collided with enemy")

            val damageAmount = 10f // Assuming 10 is the amount of damage taken
            if (!hasTakenDamage) {
                takeDamage(damageAmount)
                hasTakenDamage = true // Set a flag to indicate damage has been taken in this frame
            }
        } else {
            hasTakenDamage = false // Reset the flag if there's no collision
        }
    }


    private fun takeDamage(damageAmount: Float) {
        playerHealth -= damageAmount
        hud?.updateHealthBar() // Call the updateHealthBar function to refresh the health bar display
    }

    fun setPlayerHealth(health: Float) {
        playerHealth = health
        Gdx.app.log("Player Health", "Player health set to: $health")
        hud?.updateHealthBar() // Call the updateHealthBar function when the player's health is set
    }


    fun getPlayerHealth(): Float {

        Gdx.app.log("Player Health", "Current player health: $playerHealth")

        return playerHealth
    }
}
