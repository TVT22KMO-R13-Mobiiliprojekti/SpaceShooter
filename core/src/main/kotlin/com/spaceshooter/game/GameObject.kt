package com.spaceshooter.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2

open class GameObject(var position: Vector2 = Vector2(0.0f, 0.0f), var size: Vector2 = Vector2(0.0f, 0.0f)) {


    private lateinit var texture : Texture

    internal var speed: Float = 400.0f

    open fun update(deltaTime: Float) {}
    open fun render() {}

    open fun setTexture(texture: Texture)
    {
        this.texture = texture
    }

    fun setPos(position : Vector2)
    {
        this.position = position
    }

    fun getPos(): Vector2
    {
        return this.position
    }
}
