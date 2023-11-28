package com.spaceshooter.game

import com.badlogic.gdx.InputProcessor

public abstract class PlayerInputProcessor() : InputProcessor {

    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // your touch down code here
        return true // return true to indicate the event was handled
    }

    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // your touch up code here
        return true // return true to indicate the event was handled
    }

}
