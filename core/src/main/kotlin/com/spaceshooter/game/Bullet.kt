package com.spaceshooter.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Bullet() : GameObject() {


    override fun update(deltaTime : Float)
    {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime

        sprite.setPosition(position.x, position.y)
        hitBox.setPosition(position.x, position.y)


    }
}
