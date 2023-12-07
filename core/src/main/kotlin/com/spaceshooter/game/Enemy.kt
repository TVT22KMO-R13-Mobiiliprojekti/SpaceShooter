package com.spaceshooter.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import kotlin.math.abs
import kotlin.math.cos

enum class EnemyType
{
    FAST, TANK, SHOOTER
}
class Enemy() : GameObject() {

    private var type : EnemyType = EnemyType.FAST

    constructor(type: EnemyType, spawnPosition: Vector2) : this() {
        this.position = spawnPosition
        this.type = type
    }

    override fun update(deltaTime: Float) {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime

        sprite.setPosition(position.x, position.y)
        //hitBox.setPosition(position.x, position.y)

        updateRectanglePosition()

        animationTimer += deltaTime
        if(animationTimer >= animationInterval)
        {
            if (currentFrame < frameCount - 1) {
                currentFrame += 1
            } else {
                currentFrame = 0
            }
            animationTimer = 0.0f

            // Update the Sprite's texture region
            val newRegion = textureRegions[currentFrame]
            sprite.setRegion(newRegion)


            //sprite.setRegion(textureRegions[currentFrame])
        }

        println("Sprite Position: (${sprite.x}, ${sprite.y})")
        println("Rectangle Position: (${hitBox.x}, ${hitBox.y})")
    }

    override fun render(spriteBatch: SpriteBatch) {

        sprite.draw(spriteBatch)
    }

    public fun setType(type: EnemyType)
    {
        this.type = type

        textureRegions.clear()

        var posX : Int; var posY : Int; var sizeX : Int; var sizeY : Int;

        if(type == EnemyType.FAST)
        {
            posX = 0
            posY = 0
            sizeX = 32
            sizeY = 80
            frameCount = 4
            for (i in 0 until frameCount) {
                addAnimationFrame(posX + sizeX * i, posY, sizeX, sizeY)
            }

            val newRegion = textureRegions[currentFrame]
            sprite.setRegion(newRegion)

            rotation = 90.0f

            sprite.rotate(rotation)

            updateRectanglePosition()
        }
    }

    private fun updateRectanglePosition() {
        // Update Rectangle position to match the rotated sprite
        val spriteCenter = Vector2(sprite.x + sprite.originX, sprite.y + sprite.originY)
        hitBox.setCenter(spriteCenter.x, spriteCenter.y)

        val rotatedWidth = abs(sprite.width * Math.cos(Math.toRadians(sprite.rotation.toDouble()))) +
            abs(sprite.height * Math.sin(Math.toRadians(sprite.rotation.toDouble())))

        val rotatedHeight = abs(sprite.width * Math.sin(Math.toRadians(sprite.rotation.toDouble()))) +
            abs(sprite.height * cos(Math.toRadians(sprite.rotation.toDouble())))

        // Update Rectangle size to match the rotated sprite
        hitBox.setSize(rotatedWidth.toFloat(), rotatedHeight.toFloat())
    }

}

