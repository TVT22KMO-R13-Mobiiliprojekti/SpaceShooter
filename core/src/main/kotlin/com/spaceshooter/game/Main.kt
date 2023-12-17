package com.spaceshooter.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

interface HighScoreInterface {
    fun sendScore(value: Int)

    companion object {
        // Default implementation
        fun createDefault(): HighScoreInterface {
            return object : HighScoreInterface {
                override fun sendScore(value: Int) {
                    // Default implementation logic
                }
            }
        }
    }
}

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class Main(private val highScoreInterface: HighScoreInterface) : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    private val image by lazy { Texture("libgdx.png") }

    private var game: Game = Game(highScoreInterface)

    private fun initialize()
    {
        game = Game(highScoreInterface)

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
        game.update(deltaTime)
        game.draw(deltaTime)
    }

    override fun dispose() {
        batch.dispose()
        image.dispose()
        game.dispose()
    }
}
