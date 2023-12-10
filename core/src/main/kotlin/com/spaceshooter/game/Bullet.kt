package com.spaceshooter.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
enum class BulletType
{
    PROJECTILE, BEAM, ROCKET
}
class Bullet() : GameObject() {



    private var bulletType : BulletType = BulletType.PROJECTILE
    private var bulletDamage: Int = 10


    override fun update(deltaTime : Float)
    {
        when(bulletType)
        {
            BulletType.PROJECTILE ->  projectileUpdate()
            BulletType.BEAM ->  beamUpdate()
            BulletType.ROCKET ->  rocketUpdate()
        }
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime

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

    private fun rocketUpdate()
    {

    }

    private fun projectileUpdate()
    {

    }

    private fun beamUpdate()
    {

    }

    public fun getDamage(): Int
    {
        return bulletDamage
    }
}
