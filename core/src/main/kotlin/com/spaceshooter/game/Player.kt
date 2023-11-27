package com.spaceshooter.game

import com.badlogic.gdx.math.Vector2

class Player(): GameObject() {

    override fun update()
    {

    }
    override fun render()
    {

    }

    fun setPosition(position : Vector2)
    {
        this.position = position
    }

    fun getPosition(): Vector2
    {
        return this.position
    }
}
