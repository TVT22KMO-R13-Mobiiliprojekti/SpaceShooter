package com.spaceshooter.game

import com.badlogic.gdx.math.Vector2

open class GameObject(var position: Vector2 = Vector2(0.0f, 0.0f), var size: Vector2 = Vector2(0.0f, 0.0f)) {


    open fun update() {}
    open fun render() {}
}
