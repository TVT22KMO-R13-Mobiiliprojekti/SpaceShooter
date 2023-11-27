package com.spaceshooter.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class Main : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    private val image by lazy { Texture("libgdx.png") }

    private var game: Game = Game()

    private fun initialize()
    {
        game = Game()

        game.initialize()
    }

    var deltaTime : Float = 0.0f

    private var started : Boolean = false

    override fun render() {
        if(!started)
        {
            initialize()
            started = true
        }

        deltaTime = Gdx.graphics.getDeltaTime()
        /*
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        batch.draw(image, 140f, 210f)
        batch.end()
        */
        game.update(Gdx.graphics.getDeltaTime())
        game.draw(Gdx.graphics.getDeltaTime())
    }

    override fun dispose() {
        batch.dispose()
        image.dispose()
    }
}
