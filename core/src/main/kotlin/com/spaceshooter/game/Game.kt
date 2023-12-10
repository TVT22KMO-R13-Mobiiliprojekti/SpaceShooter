package com.spaceshooter.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import java.util.*

class Game : ApplicationAdapter() {

    var camera: OrthographicCamera? = null

    private val RESOLUTIONX: Int = 1920
    private val RESOLUTIONY: Int = 1080

    private val batch by lazy { SpriteBatch() }
    private val image by lazy { Texture("libgdx.png") }

    //private val background1 by lazy { Texture("bkgd_0.png") }
    private val background2 by lazy { Texture("bkgd_1.png") }

    private var bg1XPos = 0f
    private var bg2XPos = 1920f // assuming the width of the image is 1920
    private var bg3XPos = 1920f

    private val playerImage by lazy { Texture("player.png") }

    private val bulletImage by lazy { Texture("bullet.png") }

    private val enemyImage by lazy { Texture("enemy.png") }

    private var bulletList: Vector<Bullet> = Vector<Bullet>()
    private var enemyList: Vector<Enemy> = Vector<Enemy>()
    //private var enemyBulletList : ArrayList<Bullet> = TODO()

    private var enemySpawnInterval : Float = 3.0f
    private var enemyTimer: Float = 0.0f

    private var bulletTimer: Float = 0.0f

    private var touchStarted: Boolean = false

    private var touchStartPos: Vector3 = Vector3(0.0f, 0.0f, 0.0f)

    //private var content: Content = Content()

    private val testSprite by lazy { Sprite() }

    private val hud by lazy { Hud(batch) }

    private val player: Player = Player()

    public fun initialize() {
        Content.initialize()
        // create the camera and the SpriteBatch
        camera = OrthographicCamera()
        camera!!.setToOrtho(false, 1920f, 1080f)

        player.setPos(Vector2(100.0f, 480.0f))
        player.setArea(Vector2(64.0f, 88.0f))
        player.setHitBoxSize(player.getArea().x, player.getArea().y)

        player.setTexture(Content.getTexture("player.png"))
        player.initialize()

        player.setHud(hud)

        testSprite.texture = playerImage
    }


    public fun update(dt: Float) {

        Content.getAssetManager().update()
        var deltaTime = Gdx.graphics.deltaTime
        //Game update logic goes here
        player.update(deltaTime)

        if (player.getPlayerHealth() <= 0) {
            // Perform game-over logic here
            // For now, let's log a message and stop the game
            Gdx.app.log("Game Over", "Player health reached 0. Game Over!")
            //Gdx.app.exit() // Close the application
            // For example, you can introduce a boolean flag to indicate the game is over.
        }

        // process user input
        if (Gdx.input.isTouched) {
            if (!touchStarted) {
                touchStartPos = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0.0f)
                camera!!.unproject(touchStartPos)
                touchStarted = true
            }
            val touchPos = Vector3()
            touchPos[Gdx.input.x.toFloat(), Gdx.input.y.toFloat()] = 0f
            camera!!.unproject(touchPos)
            //(touchPos.x - 128 / 2).also { this.rectangle.x = it }
            //(touchPos.y - 32 / 2).also { this.rectangle.y = it }

            var distance = touchStartPos.dst2(touchPos)
            var deltaY = touchPos.y - touchStartPos.y
            var deltaX = touchPos.x - touchStartPos.x
            var angle = atan2(deltaY, deltaX)

            if (distance >= 45000.0f) {
                distance = 45000.0f
            }

            var speedX = cos(angle)
            var speedY = sin(angle)

            var speedMultiplier = 0.01f

            //Gdx.app.log("ANGLE IN RAD", "Angle : $angle")
            //Gdx.app.log("DISTANCE : ", "DISTANCE : $distance")

            player.setSpeed(
                Vector2(
                    distance * speedX * speedMultiplier,
                    distance * speedY * 1.2f * speedMultiplier
                )
            )
        } else {
            touchStarted = false
            player.setSpeed(Vector2(0.0f, 0.0f))
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            //rectangle.x -= 200 * Gdx.graphics.deltaTime
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            //rectangle.x += 200 * Gdx.graphics.deltaTime
        }

        bulletTimer += deltaTime


        for (b in bulletList) {
            b.update(deltaTime)
        }
        /*
        for (b in bulletList.indices) {
            for (x in enemyList.indices) {
                if (enemyList[x].checkCollision(bulletList[b])) {
                    //Gdx.app.log("Collision detected", "Bullet is colliding with enemy")
                    enemyList[x].kill()
                    bulletList[b].kill()
                    //hud.score += 10
                    hud.addScore(10)
                }
            }
        }
        */
        for (b in bulletList.indices) {
            for (x in enemyList.indices) {
                if (enemyList[x].checkCollision(bulletList[b])) {
                    //Gdx.app.log("Collision detected", "Bullet is colliding with enemy")
                    enemyList[x].kill()
                    bulletList[b].kill()
                    //hud.score += 10
                    hud.addScore(10)
                }
            }
        }
        for(e in enemyList.indices) {
            var playerBullets : Vector<Bullet> = player.getBullets()
            for(b in playerBullets.indices)
            {
                if(playerBullets[b].checkCollision(enemyList[e]))
                {
                    playerBullets[b].kill()
                    enemyList[e].damage(playerBullets[b].getDamage())
                    hud.addScore(10)
                }
            }
        }

        enemyTimer += deltaTime

        enemyUpdates(deltaTime)

        // spawn points
        val spawnPoints = arrayOf(
            Vector2(1700f, 500f),
            Vector2(1700f, 800f),
            Vector2(1700f, 200f)
            // Add more spawn points if needed
        )

        for (e in enemyList) {
            e.update(deltaTime)
            //Gdx.app.log("Enemy hitbox info", "Pos X: ${e.getHitBox().x}, Pos Y: ${e.getHitBox().y}, Hitbox height: ${e.getHitBox().height}, Hitbox width: ${e.getHitBox().width}")
            if(e.checkCollision(player))
            {
                player.damage(10)
                hud.updateHealthBar(player.getPlayerHealth())
            }

            if (e.getPos().x <= 0) {
                Gdx.app.log("Game Over", "An enemy has crossed the left side of the screen")
                Gdx.app.exit()
            }
        }

        // Move the backgrounds
        val backgroundSpeed = 50f // Adjust the speed as needed
        val backgroundSpeed2 = 100f // Adjust the speed as needed
        bg1XPos -= backgroundSpeed * deltaTime
        bg2XPos -= backgroundSpeed * deltaTime
        bg3XPos -= backgroundSpeed2 * deltaTime

        // Reset the backgrounds to create a looping effect
        val backgroundWidth = 1920f
        if (bg1XPos + backgroundWidth <= 0) {
            bg1XPos = bg2XPos + backgroundWidth // Set it after the second image
        }
        if (bg2XPos + backgroundWidth <= 0) {
            bg2XPos = bg1XPos + backgroundWidth // Set it after the first image
        }
        if (bg3XPos + backgroundWidth <= 0) {
            bg3XPos = backgroundWidth
        }

        //Removal of dead GameObjects:
        for (e in enemyList.indices.reversed()) {
            if (enemyList[e].isDead()) {
                enemyList[e] = null
                enemyList.removeAt(e)
                hud.addScore(100)
            }
        }
        //Removal of player's projectiles marked for deletion
        player.cleanUp()
    }

    public fun draw(dt: Float) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f);
        camera?.update();

        if (this.camera != null) {
            batch.setProjectionMatrix(this.camera?.combined);
        }
        batch.begin()

        // Draw the backgrounds with their updated positions
        batch.draw(background2, bg1XPos, 0f, 1920f, 1080f)
        batch.draw(background2, bg2XPos, 0f, 1920f, 1080f)
        //batch.draw(background1, bg3XPos, 0f, 1920f, 1080f)
        player.render(batch)
        // Render HUD
        hud.draw()


        for (e in enemyList) {
            //batch.draw(e.getTexture(), e.getPos().x, e.getPos().y, e.getArea().x, e.getArea().y)
            e.render(batch)
        }

        //Debug draw for rendering HitBox of player to see where it is
        //batch.draw(bulletImage, player.getHitBox().x, player.getHitBox().y, player.getHitBox().width, player.getHitBox().height)

        //Debug draw for rendering enemies' and bullets' hitboxes.
        /*
        for(e in enemyList)
        {
            batch.draw(bulletImage, e.getHitBox().x, e.getHitBox().y, e.getHitBox().width, e.getHitBox().height)
        }
        for(b in bulletList)
        {
            batch.draw(enemyImage, b.getHitBox().x, b.getHitBox().y, b.getHitBox().width, b.getHitBox().height)
        }
        */

        //player.render(batch)
        batch.end()

    }

    fun enemyUpdates(deltaTime: Float)
    {
        enemyTimer += deltaTime

        if(enemyTimer >= enemySpawnInterval)
        {
            val spawnY : Int = random(RESOLUTIONY - 384)
            val eType : EnemyType = EnemyType.values().random()

            if(eType == EnemyType.FAST)
            {
                var num = 0
                for (i in 0 until 4) {
                    num += 1
                    spawnEnemy(Vector2(RESOLUTIONX.toFloat() + 76*num, spawnY.toFloat()), eType)
                }
            }
            else {
                spawnEnemy(Vector2(RESOLUTIONX.toFloat(), spawnY.toFloat()), eType)
            }
            enemyTimer = 0.0f
        }

    }

    fun spawnEnemy(spawnPosition: Vector2, type: EnemyType)
    {
        //val randomIndex = (Math.random() * spawnPoints.size).toInt()
        //val spawnPoint = spawnPoints[randomIndex]

        var enemy: Enemy = Enemy()
        enemy.setPos(spawnPosition)
        enemy.setTexture(Content.getTexture("ships_void.png"))
        enemy.setType(type)

        //Change to initialize this in Enemy class when type is known
        //enemy.setArea(Vector2(64f, 160f))
        enemy.setHitBoxSize(enemy.getArea().x, enemy.getArea().y)

        val directionLeft = Vector2(-1f, 0f) // Left direction

        val enemySpeed = 50f // Adjust the speed
        enemy.setSpeed(Vector2(directionLeft.x * enemySpeed, directionLeft.y * enemySpeed))

        Gdx.app.log(
            "Enemy spawned to location: ",
            "X:" + enemy.getPos().x + " Y: " + enemy.getPos().y
        )
        enemyList.add(enemy)
    }
}
