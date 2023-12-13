package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import java.util.*

class Player(): GameObject() {

    private var hud: Hud? = null // Initialize it as nullable
    private var playerHealth = 100f // Initial max health 100
    private var hasTakenDamage = false // Flag to track if damage has been taken in the current frame

    private var playerBullets: Vector<Bullet> = Vector<Bullet>()

    private var projectileTimer: Float = 0.0f
    private var beamTimer: Float = 0.0f
    private var rocketTimer: Float = 0.0f
    private var projectileInterval: Float = 0.33f
    private var beamInterval: Float = 0.3f
    private var rocketInterval: Float = 0.5f

    private var invincibilityTime: Float = 0.0f
    private var blinkTimer = 0.0f
    private var blinkInterval = 0.16f
    private var visible : Boolean = true


    fun setHud(hud: Hud) {
        this.hud = hud
    }

    override fun update(deltaTime: Float)
    {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime

        sprite.setCenter(position.x, position.y)
        hitBox.setCenter(position.x, position.y)

        updateAnimation(deltaTime)

        limitMovement()

        hasTakenDamage = false

        if(invincibilityTime >= 0.0f) {
            invincibilityTime -= deltaTime


            if (blinkTimer >= blinkInterval) {
                visible = !visible
                blinkTimer = 0f
            }
        }
        else
        {
            visible = true
        }
        blinkTimer += deltaTime

        updateWeapons(deltaTime)

        for(b in playerBullets)
        {
            b.update(deltaTime)
        }

    }

    override fun render(spriteBatch: SpriteBatch) {
        if(visible) {
            sprite.draw(spriteBatch)
        }
        for(b in playerBullets)
        {
            b.render(spriteBatch)
        }
    }

    override fun updateAnimation(deltaTime: Float)
    {
        animationTimer += deltaTime

        // Separate idle animation frames from moving frames
        val idleFrameCount = 2 // Number of frames for the idle animation

        if (animationTimer >= animationInterval) {
            if (speed.y != 0.0f) {
                // Moving state - use frames 2 and 3
                if (currentFrame < frameCount - 2) {
                    currentFrame += 1
                } else {
                    currentFrame = idleFrameCount // Skip idle frames
                }
            } else {
                // Idle state - use frames 0 and 1
                if (currentFrame < idleFrameCount - 1) {
                    currentFrame += 1
                } else {
                    currentFrame = 0
                }
            }
            animationTimer = 0.0f
        }
        // Update the Sprite's texture region
        val newRegion = textureRegions[currentFrame]
        sprite.setRegion(newRegion)
        // Flip sprite based on movement direction
        if (speed.y > 0.0f) {
            sprite.flip(false, false)
        } else if (speed.y < 0.0f) {
            sprite.flip(true, false)
        }
    }

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

    fun initialize()
    {
        var posX : Int; var posY : Int; var sizeX : Int; var sizeY : Int;
        frameCount = 4
        animationInterval = 0.2f
        posX = 0
        posY = 0
        sizeX = 32
        sizeY = 44
        for (i in 0 until frameCount) {
            addAnimationFrame(posX + sizeX * i, posY, sizeX, sizeY)
        }
        val newRegion = textureRegions[currentFrame]
        sprite.setRegion(newRegion)

        rotation = 270.0f

        sprite.rotate(rotation)

        updateHitBox()

        setArea(this.size)
    }

    fun updateWeapons(deltaTime: Float)
    {
        projectileTimer += deltaTime
        beamTimer += deltaTime
        rocketTimer += deltaTime

        if (projectileTimer >= projectileInterval) {
            var bullet: Bullet = Bullet()

            bullet.setTexture(Content.getTexture("animbullet.png"))
            bullet.initialize()
            bullet.setArea(Vector2(8.0f, 8.0f))
            bullet.setPos(
                Vector2(
                    this.getPos().x + this.getArea().x / 2,
                    this.getPos().y - bullet.getArea().y / 2)
            )
            bullet.setHitBoxSize(bullet.getArea().x, bullet.getArea().y)

            bullet.setSpeed(Vector2(600.0f, 0.0f))
            //bullet.setSpeed(Vector2(400.0f, 100.0f - random(200.0f)))
            playerBullets.add(bullet)
            projectileTimer = 0.0f
        }
    }

    public fun damage(damage: Int)
    {
        if(invincibilityTime <= 0.0f) {
            playerHealth -= damage
            invincibilityTime = 1.5f
        }
    }

    public fun getBullets() : Vector<Bullet>
    {
        return playerBullets
    }

    //Use at the end of update-loop for cleaning up dead objects in Game.kt
    public fun cleanUp()
    {
        for (b in playerBullets.indices.reversed()) {
            if (playerBullets[b].isDead()) {
                playerBullets[b] = null
                playerBullets.removeAt(b)
            }
        }
    }

    public fun remove()
    {
        sprite.texture.dispose()
    }
}
