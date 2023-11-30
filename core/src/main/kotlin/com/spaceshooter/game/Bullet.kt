package com.spaceshooter.game

class Bullet() : GameObject() {


    override fun update(deltaTime : Float)
    {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime

        hitBox.setPosition(position.x - hitBox.width/2, position.y - hitBox.height / 2)
    }
}
