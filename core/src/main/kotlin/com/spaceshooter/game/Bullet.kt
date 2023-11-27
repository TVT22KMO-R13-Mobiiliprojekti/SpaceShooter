package com.spaceshooter.game

class Bullet() : GameObject() {


    override fun update(deltaTime : Float)
    {
        position.x += deltaTime * speed
    }
}
