package com.spaceshooter.game

import com.badlogic.gdx.math.Vector2

enum class EnemyType
{
    FAST, TANK, SHOOTER
}
class Enemy() : GameObject() {

    private var type : EnemyType = EnemyType.FAST
    var animationTimer : Float = 0.0f

    constructor(type: EnemyType, spawnPosition: Vector2) : this() {
        this.position = spawnPosition
        this.type = type
    }

    override fun update(deltaTime: Float) {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime
        hitBox.setPosition(position.x, position.y)
        sprite.setPosition(position.x, position.y)
    }

    public fun setType(type: EnemyType)
    {
        this.type = type
    }

}

