package com.spaceshooter.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

open class GameObject(var position: Vector2 = Vector2(0.0f, 0.0f), var size: Vector2 = Vector2(0.0f, 0.0f)) {


    private lateinit var texture : Texture

    internal var speed: Vector2 = Vector2(400.0f, 0.0f)

    private var hitBox : Rectangle = Rectangle()

    open fun update(deltaTime: Float) {}
    open fun render() {}

    open fun setTexture(texture: Texture)
    {
        this.texture = texture
    }

    open fun getTexture(): Texture
    {
        return this.texture
    }

    fun setPos(position : Vector2)
    {
        this.position = position
    }

    fun getPos(): Vector2
    {
        return this.position
    }

    //setArea is setting the 'size' variable of the GameObject, the compiler does not like it being called setSize and getSize for some reason.
    fun setArea(size: Vector2)
    {
        this.size = size
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
}
