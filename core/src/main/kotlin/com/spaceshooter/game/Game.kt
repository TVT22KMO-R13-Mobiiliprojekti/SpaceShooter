package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils


class Game {

    var camera: OrthographicCamera? = null

    private val batch by lazy { SpriteBatch() }
    private val image by lazy { Texture("libgdx.png") }

    private val playerImage by lazy { Texture("player.png") }

    private val bulletImage by lazy { Texture("bullet.png")}

    //private val player: Player = TODO()
    private var bulletList : ArrayList<Bullet> = arrayListOf()

    //private var enemyList : ArrayList<Enemy> = TODO()
    //private var enemyBulletList : ArrayList<Bullet> = TODO()

    private var rectangle: Rectangle = Rectangle()

    private var bulletTimer : Float = 0.0f

    public fun initialize()
    {
        // create the camera and the SpriteBatch
        camera = OrthographicCamera()
        camera!!.setToOrtho(false, 1920f, 1080f)
        // create a Rectangle to logically represent the bucket
        rectangle = Rectangle()
        rectangle.x = (800.0f / 2.0f - 64.0f / 2.0f) // center the bucket horizontally

        rectangle.y = 20.0f // bottom left corner of the bucket is 20 pixels above

        rectangle.width = 128.0f
        rectangle.height = 32.0f
    }

    public fun update(dt: Float)
    {
        //Game update logic goes here

        // process user input
        if (Gdx.input.isTouched) {
            val touchPos = Vector3()
            touchPos[Gdx.input.x.toFloat(), Gdx.input.y.toFloat()] = 0f
            camera!!.unproject(touchPos)
            (touchPos.x - 128 / 2).also { this.rectangle.x = it }
            (touchPos.y - 32 / 2).also { this.rectangle.y = it }
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            rectangle.x -= 200 * Gdx.graphics.deltaTime
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            rectangle.x += 200 * Gdx.graphics.deltaTime
        }

        bulletTimer += Gdx.graphics.deltaTime

        if(bulletTimer >= 1.0f)
        {

            var bullet: Bullet = Bullet()
            bullet.setPos(Vector2(rectangle.x, rectangle.y))
            bullet.setTexture(bulletImage)

            bulletList.add(bullet)
            bulletTimer = 0.0f
        }

        for (b in bulletList)
        {
            b.update(Gdx.graphics.deltaTime)
        }
    }

    public fun draw(dt: Float)
    {
        ScreenUtils.clear(0.0f, 0.0f, 0.2f, 1.0f);
        camera?.update();

        if(this.camera != null)
        batch.setProjectionMatrix(this.camera?.combined);

        batch.begin()

        batch.draw(playerImage, rectangle.x, rectangle.y, rectangle.width, rectangle.height)

        for (b in bulletList)
        {
            batch.draw(bulletImage, b.getPos().x, b.getPos().y, 32.0f, 32.0f)
        }

        batch.end()

    }
}
