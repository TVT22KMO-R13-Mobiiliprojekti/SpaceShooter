package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

class Player(): GameObject() {

    override fun update(deltaTime: Float)
    {
        position.x += speed.x * deltaTime
        position.y += speed.y * deltaTime

        sprite.setCenter(position.x, position.y)
        hitBox.setCenter(position.x, position.y)

        limitMovement()
    }
    override fun render(spriteBatch: SpriteBatch)
    {
        sprite.draw(spriteBatch)
        //Gdx.app.log("SPRITE INFO: ", "Position x: " + sprite.x + " Position y: " + sprite.y + "Sprite size : " + sprite.width)
        //Gdx.app.log("SPRITE INFO: ", "Texture height: " + sprite.texture.height)
        //Gdx.app.log("Texture Info", "Width: ${sprite.texture.depth}, Height: ${sprite.texture.height}")
    }

    private fun limitMovement()
    {
        if(position.x < 0.0f + size.x / 2)
            position.x = 0.0f + size.x / 2
        if(position.x > 1920.0f - size.x / 2)
            position.x = 1920.0f - size.x / 2
        if(position.y < 0.0f + size.y / 2)
            position.y = 0.0f + size.y / 2
        if(position.y > 1080.0f - size.y / 2)
            position.y = 1080.0f - size.y / 2
    }
}
