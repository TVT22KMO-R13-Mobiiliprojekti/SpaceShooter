package com.spaceshooter.game

import com.badlogic.gdx.math.Vector2

class Player(): GameObject() {

    override fun update()
    {

    }
    override fun render()
    {

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
