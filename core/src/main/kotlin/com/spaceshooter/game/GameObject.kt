package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import java.lang.Math.*

open class GameObject(var position: Vector2 = Vector2(0.0f, 0.0f), var size: Vector2 = Vector2(0.0f, 0.0f)) {

    internal var sprite: Sprite = Sprite()

    private lateinit var texture : Texture

    internal var speed: Vector2 = Vector2(400.0f, 0.0f)

    internal var hitBox : Rectangle = Rectangle()

    open fun update(deltaTime: Float) {}
    open fun render(spriteBatch: SpriteBatch)
    {
        sprite.draw(spriteBatch)
    }

    open fun setTexture(texture: Texture)
    {
        this.texture = texture
        this.sprite.texture = this.texture
        initializeSprite(texture, 0, 0, texture.width, texture.height)
        //sprite.setPosition(position.x, position.y)
        //sprite.setSize(size.x, size.y)
    }

    open fun getTexture(): Texture
    {
        return this.texture
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
        return hitBox.contains(gameObject.getHitBox())
    }

    private fun initializeSprite(texture : Texture, srcX : Int, srcY : Int, srcWidth : Int, srcHeight : Int) {
        if (texture == null) throw IllegalArgumentException ("texture cannot be null.");
        this.texture = texture;
        sprite.setRegion(srcX, srcY, srcWidth, srcHeight);
        sprite.setColor(1f, 1f, 1f, 1f);
        sprite.setSize(abs(srcWidth.toFloat()), abs(srcHeight.toFloat()));
        sprite.setOrigin(sprite.width / 2, sprite.height / 2);
    }
}
