package com.spaceshooter.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import java.util.*
import kotlin.math.abs

enum class EnemyType
{
    FAST, TANK, SHOOTER
}
class Enemy() : GameObject() {

    private var type : EnemyType = EnemyType.FAST

    private var health : Int = 10
    private var shootingInterval: Float = 2.5f
    private var shootTimer: Float = 0.0f
    private var movementAmount: Float = 0.0f
    private var offsetX: Float = 0.0f
    private var flipMovement: Boolean = false

    private var enemyBullets: Vector<Bullet> = Vector<Bullet>()

    constructor(type: EnemyType, spawnPosition: Vector2) : this() {
        this.position = spawnPosition
        this.type = type
    }

    override fun update(deltaTime: Float) {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime

        sprite.setPosition(position.x, position.y)
        updateHitBox()
        updateAnimation(deltaTime)

        when(type)
        {
            EnemyType.FAST ->  fastUpdate(deltaTime)
            EnemyType.SHOOTER ->  shooterUpdate(deltaTime)
            EnemyType.TANK ->  tankUpdate(deltaTime)
        }
    }

    override fun render(spriteBatch: SpriteBatch) {
        sprite.draw(spriteBatch)

        if(enemyBullets.isNotEmpty())
        {
            for(e in enemyBullets)
            {
                e.render(spriteBatch)
            }
        }
    }

    public fun setType(type: EnemyType)
    {
        this.type = type
        textureRegions.clear()
        var posX : Int; var posY : Int; var sizeX : Int; var sizeY : Int;
        animationInterval = 0.1f

        if(type == EnemyType.TANK)
        {
            health = 100
            posX = 128
            posY = 0
            sizeX = 48
            sizeY = 80
            frameCount = 4
            setArea(Vector2(192f, 320f))
            for (i in 0 until frameCount) {
                addAnimationFrame(posX + sizeX * i, posY, sizeX, sizeY)
            }
            val newRegion = textureRegions[currentFrame]
            sprite.setRegion(newRegion)
            rotation = 90.0f
            setSpeed(Vector2(-100.0f, 0.0f))
        }
        else if(type == EnemyType.FAST)
        {
            health = 10
            posX = 0
            posY = 440
            sizeX = 16
            sizeY = 38
            frameCount = 4
            setArea(Vector2(32f, 76f))
            for (i in 0 until frameCount) {
                addAnimationFrame(posX + sizeX * i, posY, sizeX, sizeY)
            }
            val newRegion = textureRegions[currentFrame]
            sprite.setRegion(newRegion)

            rotation = 90.0f
            setSpeed(Vector2(-200.0f, 0.0f))
        }
        else if(type == EnemyType.SHOOTER)
        {
            health = 30
            posX = 0
            posY = 568
            sizeX = 32
            sizeY = 54
            frameCount = 4
            setArea(Vector2(96f, 160f))
            for (i in 0 until frameCount) {
                addAnimationFrame(posX + sizeX * i, posY, sizeX, sizeY)
            }
            val newRegion = textureRegions[currentFrame]
            sprite.setRegion(newRegion)
            rotation = 90.0f
            setSpeed(Vector2(-150.0f, 0.0f))
        }
        sprite.rotate(rotation)
        updateHitBox()
    }

    fun damage(damage: Int)
    {
        health -= damage
        if(health <= 0.0f)
        {
            kill()
        }
    }

    fun fastUpdate(deltaTime: Float)
    {
        movementAmount += abs(speed.x) * deltaTime
        movementAmount += abs(speed.y) * deltaTime
        if(movementAmount >= 250.0f + offsetX)
        {
            if(abs(speed.x) > 0.0f)
            {
                if(flipMovement) {
                    setSpeed(Vector2(0.0f, 200f))
                    rotation = 0f
                }
                else {
                    setSpeed(Vector2(0.0f, -200f))
                    rotation = 180f
                }
            }
            else if(abs(speed.y) > 0.0f)
            {
                setSpeed(Vector2(-300.0f, 0.0f))
                rotation = 90f
            }

            movementAmount = 0.0f
            fastUpdateBounds()

            sprite.rotation = rotation
            offsetX = 0.0f
        }
    }

    private fun fastUpdateBounds()
    {
        val centerY = getCenter().y
        val screenCenterY = 1080.0f / 2 // Assuming 1080p screen height

        val spacing = 100.0f

        if (centerY > screenCenterY - spacing) {
            // Too much towards the top, set flipMovement to false
            flipMovement = false
        } else if (centerY < screenCenterY + spacing) {
            // Too much towards the bottom, set flipMovement to true
            flipMovement = true
        }
    }

    fun shooterUpdate(deltaTime: Float)
    {
        shootTimer += deltaTime

        if(shootTimer >= shootingInterval)
        {
            var bullet: Bullet = Bullet()

            bullet.setTexture(Content.getTexture("animbullet.png"))
            bullet.initialize()
            bullet.setArea(Vector2(8.0f, 8.0f))
            bullet.setPos(
                Vector2(
                    this.getPos().x - this.getArea().x / 2,
                    this.getPos().y + this.getArea().y / 2 - bullet.getArea().y/2)
            )
            bullet.setHitBoxSize(bullet.getArea().x, bullet.getArea().y)

            bullet.setSpeed(Vector2(-600.0f, 0.0f))
            //bullet.setSpeed(Vector2(400.0f, 100.0f - random(200.0f)))
            enemyBullets.add(bullet)
            shootTimer = 0.0f
        }

        /*
        if(enemyBullets.isNotEmpty())
        {
            for(e in enemyBullets)
            {
                e.update(deltaTime)
            }
        }
        */
    }

    fun tankUpdate(deltaTime: Float)
    {

    }

    public fun getBulletList(): Vector<Bullet>
    {
        return enemyBullets
    }

    public fun getType(): EnemyType
    {
        return type
    }

    public fun setOffsetX(offset: Float)
    {
        this.offsetX = offset
    }

    public fun moveFlip(flip: Boolean)
    {
        this.flipMovement = flip
    }

    public fun setBulletList(bulletList: Vector<Bullet>)
    {
        this.enemyBullets = bulletList
    }

}

