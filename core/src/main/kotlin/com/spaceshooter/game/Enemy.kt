package com.spaceshooter.game

import com.badlogic.gdx.math.Vector2

enum class EnemyType
{
    FAST, TANK, SHOOTER
}
class Enemy() : GameObject() {

    private var type : EnemyType = EnemyType.FAST

    constructor(type: EnemyType, spawnPosition: Vector2) : this() {
        this.position = spawnPosition
        this.type = type
    }

    override fun update(deltaTime: Float) {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime

        sprite.setCenter(position.x, position.y)
        hitBox.setPosition(position.x, position.y)

        //println("Sprite Position: (${sprite.x}, ${sprite.y})")
        //println("Rectangle Position: (${hitBox.x}, ${hitBox.y})")
    }

    public fun setType(type: EnemyType)
    {
        this.type = type
    }

}

