package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Vector2
import java.util.*

class Player(): GameObject() {

    private var hud: Hud? = null // Initialize it as nullable
    private var playerHealth = 100f // Initial max health 100
    private var hasTakenDamage = false // Flag to track if damage has been taken in the current frame

    private var playerBullets: Vector<Bullet> = Vector<Bullet>()

    private var shootingSound: Sound? = null
    var soundId: Long = -1
    var soundVolume: Float = 0.4f

    private var projectileTimer: Float = 0.0f
    private var scatterTimer: Float = 0.0f
    private var rocketTimer: Float = 0.0f
    private var projectileInterval: Float = 0.33f
    private var scatterInterval: Float = 0.03f
    private var rocketInterval: Float = 3.0f
    private var scatterLeft: Float = 3.0f

    private var hasRocket: Boolean = false
    private var hasScatter: Boolean = false

    private var closestEnemy: Enemy? = null

    private var invincibilityTime: Float = 0.0f
    private var blinkTimer = 0.0f
    private var blinkInterval = 0.16f
    private var visible : Boolean = true

    private lateinit var thrusterEffectPool : ParticleEffectPool
    private var thrusterEffect : ParticleEffect = ParticleEffect()
    private var effects: Vector<PooledEffect> = Vector<PooledEffect>()


    fun setHud(hud: Hud) {
        this.hud = hud
    }
    fun setSFXVolume(volume: Float) {
        soundVolume = volume
    }

    fun playShootingSound() {
        soundId = shootingSound?.play(soundVolume) ?: -1
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

            if(!b.getHoming())
            {
                b.setTarget(closestEnemy)
            }
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

        shootingSound = Gdx.audio.newSound(Gdx.files.internal("alienshoot3.wav"))
    }

    fun updateWeapons(deltaTime: Float)
    {
        projectileTimer += deltaTime
        scatterTimer += deltaTime
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

            //soundId = shootingSound?.play() ?: -1
            playShootingSound()
            //shootingSound?.setVolume(soundId,0.4f)

            bullet.setSpeed(Vector2(600.0f, 0.0f))
            //bullet.setSpeed(Vector2(400.0f, 100.0f - random(200.0f)))
            playerBullets.add(bullet)
            projectileTimer = 0.0f
        }
        if (rocketTimer >= rocketInterval && hasRocket) {
            var bullet: Bullet = Bullet()
            bullet.setType(BulletType.ROCKET)
            bullet.setTarget(closestEnemy)

            bullet.setTexture(Content.getTexture("rocket.png"))
            bullet.initialize()
            bullet.setArea(Vector2(48.0f, 24.0f))
            bullet.setPos(
                Vector2(
                    this.getPos().x - bullet.getArea().x,
                    this.getPos().y - bullet.getArea().y)
            )
            bullet.setHitBoxSize(bullet.getArea().x, bullet.getArea().y)

            bullet.setSpeed(Vector2(400.0f, 0.0f))
            //bullet.setSpeed(Vector2(400.0f, 100.0f - random(200.0f)))
            playerBullets.add(bullet)
            rocketTimer = 0.0f
            spawnThrusterEffect(bullet.getPos().x, bullet.getPos().y, 0.5f, bullet)
        }
        if (scatterTimer >= scatterInterval && hasScatter && scatterLeft >= 0.0f) {
            var bullet: Bullet = Bullet()

            bullet.setType(BulletType.SCATTER)
            bullet.setTexture(Content.getTexture("animbullet.png"))
            bullet.initialize()
            bullet.setArea(Vector2(8.0f, 8.0f))
            bullet.setPos(
                Vector2(
                    this.getPos().x + this.getArea().x / 2,
                    this.getPos().y - bullet.getArea().y / 2)
            )
            bullet.setHitBoxSize(bullet.getArea().x, bullet.getArea().y)

            bullet.setSpeed(Vector2(600.0f, random(300).toFloat() - 150.0f))
            //bullet.setSpeed(Vector2(400.0f, 100.0f - random(200.0f)))
            playerBullets.add(bullet)
            scatterTimer = 0.0f
        }
        scatterLeft -= deltaTime
        if(scatterLeft <= 0.0f)
        {
            hasScatter = false
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

    public fun setTargetEnemy(target: Enemy?)
    {
        closestEnemy = target
    }

    public fun setThrusterParticlePool(thrusterEffectPool: ParticleEffectPool, thrusterEffect: ParticleEffect)
    {
        this.thrusterEffectPool = thrusterEffectPool
        this.thrusterEffect = thrusterEffect
    }

    public fun setPooledEffects(effects: Vector<PooledEffect>)
    {
        this.effects = effects
    }

    fun spawnThrusterEffect(posX: Float, posY: Float, scale: Float, bullet: Bullet)
    {
        var pooledEffect : ParticleEffectPool.PooledEffect = thrusterEffectPool.obtain()
        pooledEffect.setPosition(posX, posY)
        pooledEffect.scaleEffect(scale)
        effects.add(pooledEffect)
        bullet.setThruster(pooledEffect)
    }

    public fun rocketUpgrade()
    {
        hasRocket = true
    }

    public fun scatterUpgrade()
    {
        scatterLeft = 3.0f
        hasScatter = true
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

    public fun heal(health: Int)
    {
        this.playerHealth += health
        if(this.playerHealth >= 100.0f)
        {
            this.playerHealth = 100.0f
        }
    }

    public fun remove()
    {
        sprite.texture.dispose()
    }
}
