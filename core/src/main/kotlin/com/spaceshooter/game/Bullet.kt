package com.spaceshooter.game

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

enum class BulletType
{
    PROJECTILE, SCATTER, ROCKET
}
class Bullet() : GameObject() {



    private var bulletType : BulletType = BulletType.PROJECTILE
    private var bulletDamage: Int = 10
    private var rocketHomingTimer : Float = 0.0f
    private var isHoming: Boolean = false
    private val rotationSpeed = 3f
    private var target : Enemy? = null
    private val targetPosition: Vector2 = Vector2()

    private var rocketSpeed : Float = 600f
    private lateinit var thruster: PooledEffect
    private var thrusterSet: Boolean = false

    private val velocity: Vector2 = Vector2(200f, 200f)
    private val homingDuration: Float = 2.0f


    override fun update(deltaTime : Float)
    {
        when(bulletType)
        {
            BulletType.PROJECTILE ->  projectileUpdate(deltaTime)
            BulletType.SCATTER ->  beamUpdate(deltaTime)
            BulletType.ROCKET ->  rocketUpdate(deltaTime)
        }

        sprite.setPosition(position.x, position.y)
        updateHitBox()
        updateAnimation(deltaTime)
    }

    fun initialize()
    {
        textureRegions.clear()
        var posX : Int; var posY : Int; var sizeX : Int; var sizeY : Int;
        animationInterval = 0.1f

        if(bulletType == BulletType.PROJECTILE)
        {
            bulletDamage = 10
            posX = 0
            posY = 0
            sizeX = 16
            sizeY = 16
            frameCount = 4
            for (i in 0 until frameCount) {
                addAnimationFrame(posX + sizeX * i, posY, sizeX, sizeY)
            }
            rotation = 0.0f
        }
        else if(bulletType == BulletType.ROCKET)
        {

            bulletDamage = 50
            posX = 0
            posY = 0
            sizeX = 64
            sizeY = 32
            frameCount = 1
            //for (i in 0 until frameCount) {
            addAnimationFrame(posX, posY, sizeX, sizeY)
            //}
            rotation = 270.0f
        }
        else if(bulletType == BulletType.SCATTER)
        {

            bulletDamage = 5
            posX = 0
            posY = 0
            sizeX = 16
            sizeY = 16
            frameCount = 4
            for (i in 0 until frameCount) {
                addAnimationFrame(posX + sizeX * i, posY, sizeX, sizeY)
            }
            rotation = 0.0f
        }
        sprite.rotate(rotation)
        val newRegion = textureRegions[currentFrame]
        sprite.setRegion(newRegion)
        updateHitBox()
    }

    private fun rocketUpdate(deltaTime: Float) {
        rocketHomingTimer += deltaTime

        if (rocketHomingTimer >= 0.3f && target != null) {
            isHoming = true
        }

        if (!isHoming) {
            // Initial phase: Move straight down
            position.y -= 250f * deltaTime
            rotation = 270f
            sprite.rotation = rotation
        } else if (target != null && !target!!.isDead()) {
            // Homing phase: Gradually rotate towards the targetAngle and move towards it
            val direction = target?.getCenter()!!.cpy().sub(position).nor()

            val targetAngle = direction.angleDeg()
            val currentAngle = rotation
            val newAngle = MathUtils.lerpAngleDeg(currentAngle, targetAngle, rotationSpeed * deltaTime)

            // Set the velocity based on the rocket's angle and maintain a fixed speed
            speed.setAngleDeg(newAngle).nor().scl(rocketSpeed)

            // Update rotation and position
            rotation = newAngle
            sprite.rotation = rotation
            position.add(speed.x * deltaTime, speed.y * deltaTime)
        } else {
            // Rocket has no target or the target is dead, kill the rocket
            //kill()
            isHoming = false
        }

        if (rocketHomingTimer >= 5.0f) {
            // If the rocket has not homed within the specified time, kill it
            //kill()
            isHoming = false
        }


        sprite.setOrigin(sprite.width / 2, 0f)

        var emitter = thruster.emitters.first()

        val bottomOffsetX : Float = getArea().x / 2 * MathUtils.cosDeg(rotation)
        val bottomOffsetY : Float = getArea().y / 2 * MathUtils.sinDeg(rotation)

        thruster.setPosition(getCenter().x - bottomOffsetX, getCenter().y - bottomOffsetY)
        emitter.angle.setHigh(rotation + 180f)
        emitter.angle.setLow(rotation -180f)
    }



    private fun projectileUpdate(deltaTime: Float)
    {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime
    }

    private fun beamUpdate(deltaTime: Float)
    {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime
    }

    public fun getDamage(): Int
    {
        return bulletDamage
    }

    public fun setType(bulletType: BulletType)
    {
        this.bulletType = bulletType
    }

    public fun getType() : BulletType
    {
        return bulletType
    }

    public fun setTarget(target: Enemy?)
    {
        this.target = target
    }

    public fun getHoming(): Boolean
    {
        return this.isHoming
    }

    public fun setThruster(thruster: PooledEffect)
    {
        this.thruster = thruster
        thrusterSet = true
        thruster.scaleEffect(0.5f)
    }

    override public fun kill()
    {
        this.isDead = true
        if(thrusterSet){
            thruster.setPosition(-1000.0f, -1000.0f)
        }
    }
}
