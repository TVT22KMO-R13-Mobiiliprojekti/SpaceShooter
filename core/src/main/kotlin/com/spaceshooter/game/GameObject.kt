package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import java.lang.Math.*
import java.util.Vector

open class GameObject() {

    internal var sprite: Sprite = Sprite()
    private lateinit var texture : Texture
    internal var textureRegions : Vector<TextureRegion> = Vector<TextureRegion>()
    internal var animationTimer: Float = 0.0f
    internal var animationInterval: Float = 0.5f
    internal var currentFrame: Int = 0
    internal var frameCount : Int = 1


    internal var position: Vector2 = Vector2(0.0f, 0.0f)
    internal var size: Vector2 = Vector2(0.0f, 0.0f)
    internal var speed: Vector2 = Vector2(400.0f, 0.0f)
    internal var rotation: Float = 0.0f
    internal var hitBox : Rectangle = Rectangle()
    internal var isDead : Boolean = false

    open fun update(deltaTime: Float)
    {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime

        sprite.setPosition(position.x, position.y)
        updateHitBox()
    }
    open fun render(spriteBatch: SpriteBatch)
    {
        sprite.draw(spriteBatch)
    }

    open fun updateAnimation(deltaTime: Float)
    {
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
        }
    }

    open fun setTexture(texture: Texture)
    {
        this.texture = texture
        this.sprite.texture = this.texture
        initializeSprite(texture, 0, 0, this.texture.width, this.texture.height)
        //sprite.setPosition(position.x, position.y)
        //sprite.setSize(size.x, size.y)
    }

    open fun getTexture(): Texture
    {
        return this.texture
    }

    fun addAnimationFrame(posX: Int, posY: Int, width: Int, height: Int)
    {
        var texRegion: TextureRegion = TextureRegion(this.texture, posX, posY, width, height)

        textureRegions.add(texRegion)
    }

    fun setPos(position : Vector2)
    {
        this.position = position
        sprite.setCenter(this.position.x, this.position.y)
    }

    fun getPos(): Vector2
    {
        return this.position
    }

    //setArea is setting the 'size' variable of the GameObject, the compiler does not like it being called setSize and getSize for some reason.
    fun setArea(size: Vector2)
    {
        this.size = size
        sprite.setSize(this.size.x, this.size.y)
        sprite.setOrigin(size.x / 2, size.y / 2);
    }

    fun getArea() : Vector2
    {
        return this.size
    }

    fun setSpeed(speed: Vector2)
    {
        this.speed = speed
    }

    fun getSpeed() : Vector2
    {
        return speed
    }

    fun setHitBoxSize(width: Float, height: Float)
    {
        this.hitBox.width = width
        this.hitBox.height = height
    }

    fun getHitBox() : Rectangle
    {
        return this.hitBox
    }

    fun checkCollision(gameObject: GameObject): Boolean
    {
        return hitBox.overlaps(gameObject.getHitBox())
    }

    private fun initializeSprite(texture : Texture, srcX : Int, srcY : Int, srcWidth : Int, srcHeight : Int) {
        if (texture == null) throw IllegalArgumentException ("texture cannot be null.");
        this.texture = texture;
        sprite.setRegion(srcX, srcY, srcWidth, srcHeight);
        sprite.setColor(1f, 1f, 1f, 1f);
        sprite.setSize(abs(srcWidth.toFloat()), abs(srcHeight.toFloat()));
        sprite.setOrigin(sprite.width / 2, sprite.height / 2);
    }

    open fun kill()
    {
        this.isDead = true
    }

    fun isDead(): Boolean
    {
        return this.isDead
    }

    internal fun updateHitBox() {
        // Update Rectangle position and size to match the rotated sprite
        val spriteCenter = Vector2(sprite.x + sprite.originX, sprite.y + sprite.originY)
        hitBox.setCenter(spriteCenter.x, spriteCenter.y)

        val rotatedWidth = kotlin.math.abs(sprite.width * cos(toRadians(sprite.rotation.toDouble()))) +
            kotlin.math.abs(sprite.height * sin(toRadians(sprite.rotation.toDouble())))

        val rotatedHeight = kotlin.math.abs(sprite.width * kotlin.math.sin(toRadians(sprite.rotation.toDouble()))) +
            kotlin.math.abs(sprite.height * kotlin.math.cos(toRadians(sprite.rotation.toDouble())))

        // Update Rectangle size to match the rotated sprite
        hitBox.setSize(rotatedWidth.toFloat(), rotatedHeight.toFloat())
    }

    public fun getCenter(): Vector2
    {
        return Vector2(this.position.x + this.size.x/2, this.position.y + this.size.y/2)
    }
}
