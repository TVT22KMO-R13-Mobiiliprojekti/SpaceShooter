package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

class Player(): GameObject() {

    override fun update(deltaTime: Float)
    {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime

        sprite.setPosition(position.x, position.y)

        hitBox.setPosition(position.x - hitBox.width/2, position.y - hitBox.height / 2)

    }
    override fun render(spriteBatch: SpriteBatch)
    {
        sprite.draw(spriteBatch)
        //Gdx.app.log("SPRITE INFO: ", "Position x: " + sprite.x + " Position y: " + sprite.y + "Sprite size : " + sprite.width)
        //Gdx.app.log("SPRITE INFO: ", "Texture height: " + sprite.texture.height)
        //Gdx.app.log("Texture Info", "Width: ${sprite.texture.depth}, Height: ${sprite.texture.height}")
    }


}
