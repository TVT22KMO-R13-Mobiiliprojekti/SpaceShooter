package com.spaceshooter.game

import com.badlogic.gdx.math.Vector2

class Enemy() : GameObject() {
    override fun update(deltaTime: Float) {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime
    }
}

