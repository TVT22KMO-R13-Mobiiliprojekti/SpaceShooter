package com.spaceshooter.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import java.util.*


class Game(private val AndroidInterface: AndroidInterface) : ApplicationAdapter() {

    var camera: OrthographicCamera? = null

    private val RESOLUTIONX: Int = 1920
    private val RESOLUTIONY: Int = 1080

    private val batch by lazy { SpriteBatch() }

    //private val background1 by lazy { Texture("bkgd_0.png") }
    private val background2 by lazy { Texture("bkgd_1.png") }

    private var bg1XPos = 0f
    private var bg2XPos = 1920f // assuming the width of the image is 1920
    private var bg3XPos = 1920f

    private var bulletList: Vector<Bullet> = Vector<Bullet>()
    private var enemyList: Vector<Enemy> = Vector<Enemy>()
    private var enemyBulletList: Vector<Bullet> = Vector<Bullet>()

    private var enemySpawnInterval : Float = 3.0f
    private var enemyTimer: Float = 0.0f
    private var spawnCount : Int = 0
    private var flipMove: Boolean = false

    private var bulletTimer: Float = 0.0f

    private var touchStarted: Boolean = false

    private var touchStartPos: Vector3 = Vector3(0.0f, 0.0f, 0.0f)

    //private var content: Content = Content()

    private val testSprite by lazy { Sprite() }

    private val stage: Stage by lazy { Stage(ScreenViewport()) }
    private val hud by lazy { Hud(batch, stage) }

    private val player: Player = Player()
    private var powerUp: GameObject = GameObject()
    private var powerUpCreated: Boolean = false
    private var powerUpTimer: Float = 0.0f
    private var powerUpType: Int = 1

    lateinit var explosionEffectPool: ParticleEffectPool
    lateinit var thrusterEffectPool: ParticleEffectPool
    var effects: Vector<PooledEffect> = Vector<PooledEffect>()

    var explosionEffect : ParticleEffect = ParticleEffect()
    var thrusterEffect: ParticleEffect = ParticleEffect()

    private var gamePaused: Boolean = false
    private var musicPlaing: Boolean = true


    fun initialize() {
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

        hud.setGame(this)

        explosionEffect.load(Gdx.files.internal("particles/smallexplosion.p"), Gdx.files.internal(""));
        explosionEffect.setPosition(300f, 300f)
        explosionEffect.start()

        thrusterEffect.load(Gdx.files.internal("particles/thruster.p"), Gdx.files.internal(""))

        explosionEffectPool = ParticleEffectPool(explosionEffect, 1, 32)
        thrusterEffectPool = ParticleEffectPool(thrusterEffect, 1, 64)

        player.setThrusterParticlePool(thrusterEffectPool, thrusterEffect)
        player.setPooledEffects(effects)
    }

    override fun create() {
        Content.initialize()
    }


    fun update(deltaTime: Float) {

        Content.getAssetManager().update()

        //Game update logic goes here
        player.update(deltaTime)

        if (player.getPlayerHealth() <= 0) {
            // Perform game-over logic here
            //Gdx.app.log("Game Over", "Player health reached 0. Game Over!")
            AndroidInterface.sendScore(hud.getScore())
            //Gdx.app.exit() // Close the application
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

        bulletTimer += deltaTime


        for (b in bulletList) {
            b.update(deltaTime)
        }

        for(e in enemyList.indices) {
            var playerBullets : Vector<Bullet> = player.getBullets()
            for(b in playerBullets.indices)
            {
                if(playerBullets[b].checkCollision(enemyList[e]))
                {
                    spawnParticleEffect(playerBullets[b].getPos().x + playerBullets[b].getArea().x/2, playerBullets[b].getPos().y + playerBullets[b].getArea().y/2, 0.5f)
                    playerBullets[b].kill()
                    enemyList[e].damage(playerBullets[b].getDamage())
                    hud.addScore(10)
                }
            }
        }

        enemyTimer += deltaTime

        enemyUpdates(deltaTime)
        findClosestEnemy()
        powerUpUpdate(deltaTime)

        // spawn points
        val spawnPoints = arrayOf(
            Vector2(1700f, 500f),
            Vector2(1700f, 800f),
            Vector2(1700f, 200f)
            // Add more spawn points if needed
        )

        for (e in enemyList.indices) {
            enemyList[e].update(deltaTime)
            //Gdx.app.log("Enemy hitbox info", "Pos X: ${e.getHitBox().x}, Pos Y: ${e.getHitBox().y}, Hitbox height: ${e.getHitBox().height}, Hitbox width: ${e.getHitBox().width}")
            if(enemyList[e].checkCollision(player))
            {
                player.damage(10)
                hud.updateHealthBar(player.getPlayerHealth())
            }
            var enemyBullets: Vector<Bullet> = enemyList[e].getBulletList()

            if (enemyList[e].getPos().x <= 0) {
                AndroidInterface.sendScore(hud.getScore())
                //Gdx.app.log("Game Over", "An enemy has crossed the left side of the screen")
                //Gdx.app.exit()
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

                var particleScale = 1f
                if(enemyList[e].getType() == EnemyType.TANK){
                    particleScale = 5f
                }
                else if(enemyList[e].getType() == EnemyType.SHOOTER){
                    particleScale = 2f
                }
                spawnParticleEffect(enemyList[e].getPos().x + enemyList[e].getArea().x/2, enemyList[e].getPos().y + enemyList[e].getArea().y/2, particleScale)
                enemyList[e] = null
                enemyList.removeAt(e)
                hud.addScore(100)
            }
        }
        updateEnemyBullets(deltaTime)
        //Removal of player's projectiles marked for deletion
        player.cleanUp()
        //Removal of enemybullets and possibly other gameobjects
        cleanUp()
    }

    public fun draw(deltaTime: Float) {
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

        if(powerUpCreated) {
            powerUp.render(batch)
        }

        for (e in enemyList) {
            //batch.draw(e.getTexture(), e.getPos().x, e.getPos().y, e.getArea().x, e.getArea().y)
            e.render(batch)
        }

        //Draw particle effects
        for (i in effects.size - 1 downTo 0) {
            val effect = effects[i]
            effect.draw(batch, deltaTime)
            if (effect.isComplete()) {
                effect.free()
                effects.removeAt(i)
            }
        }

        //explosionEffect.draw(batch)

        // Render HUD
        hud.draw()
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
                flipMove = !flipMove
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
        enemy.setBulletList(enemyBulletList)

        if(type == EnemyType.FAST)
        {
            enemy.setOffsetX(spawnCount * 76f)
            enemy.moveFlip(flipMove)
        }


        if(type == EnemyType.FAST && spawnCount < 3) {
            spawnCount += 1
        }
        else if(spawnCount >= 3){
            spawnCount = 0
        }

        //Change to initialize this in Enemy class when type is known
        //enemy.setArea(Vector2(64f, 160f))
        enemy.setHitBoxSize(enemy.getArea().x, enemy.getArea().y)

        //val directionLeft = Vector2(-1f, 0f) // Left direction

        //val enemySpeed = 150f // Adjust the speed
        //enemy.setSpeed(Vector2(directionLeft.x * enemySpeed, directionLeft.y * enemySpeed))

        //Gdx.app.log(
        //    "Enemy spawned to location: ",
        //    "X:" + enemy.getPos().x + " Y: " + enemy.getPos().y
        //)
        enemyList.add(enemy)
    }

    fun spawnParticleEffect(posX: Float, posY: Float, scale: Float)
    {
        var pooledEffect : PooledEffect = explosionEffectPool.obtain()
        pooledEffect.setPosition(posX, posY)
        pooledEffect.scaleEffect(scale)
        effects.add(pooledEffect)
    }

    private fun findClosestEnemy() {
        var closestDistanceSquared = Float.MAX_VALUE
        var closestEnemy: Enemy? = null

        for (enemy in enemyList) {
            val distanceSquared = player.getPos().dst2(enemy.getPos())

            // Update closest enemy if the current enemy is closer
            if (distanceSquared < closestDistanceSquared) {
                closestDistanceSquared = distanceSquared
                closestEnemy = enemy
            }
        }
        player.setTargetEnemy(closestEnemy)
    }

    public fun cleanUp()
    {
        for (b in enemyBulletList.indices.reversed()) {
            if (enemyBulletList[b].isDead()) {
                enemyBulletList[b] = null
                enemyBulletList.removeAt(b)
            }
        }
    }

    public fun updateEnemyBullets(deltaTime: Float)
    {
        for (b in enemyBulletList) {
            b.update(deltaTime)
            if(b.checkCollision(player))
            {
                player.damage(10)
                hud.updateHealthBar(player.getPlayerHealth())
                b.kill()
            }
        }
    }

    fun createPowerUp(type: Int)
    {
        powerUp.setPos(Vector2(1984.0f, 508.0f))
        powerUp.setArea(Vector2(64.0f, 32.0f))
        powerUp.setHitBoxSize(powerUp.getArea().x, powerUp.getArea().y)
        powerUp.setSpeed(Vector2(-200.0f, 0.0f))
        when(type)
        {
            1 ->  {
                powerUp.setTexture(Content.getTexture("rocketpowerup.png"))
                powerUpType = 1
            }
            2 -> {
                powerUp.setTexture(Content.getTexture("gunpowerup.png"))
                powerUpType = 2
            }
            3 -> {
                powerUp.setTexture(Content.getTexture("health.png"))
                powerUpType = 3
            }
        }
        powerUpCreated = true
    }

    fun powerUpUpdate(deltaTime: Float)
    {
        if(powerUpTimer >= 5.0f && !powerUpCreated) {
            val rnd = random(1, 3)
            Gdx.app.log("RANDOM NUMBER WAS", rnd.toString())
            createPowerUp(rnd)
            powerUpTimer = 0.0f
        }
        else
        {
            powerUpTimer += deltaTime
        }


        if(powerUpCreated)
        {
            if(powerUp.checkCollision(player))
            {
                when(powerUpType)
                {
                    1-> player.rocketUpgrade()
                    2-> player.scatterUpgrade()
                    3-> {
                        player.heal(30)
                        hud.updateHealthBar(player.getPlayerHealth())
                    }
                }
                powerUpCreated = false
                powerUpTimer = 0.0f
            }
            powerUp.update(deltaTime)
            //Gdx.app.log("POWERUP POS: ", powerUp.getPos().x.toString())
            //Gdx.app.log("POWERUP POS Y: ", powerUp.sprite.height.toString())
            if(powerUp.position.x < 64.0f)
            {
                powerUpTimer = 0.0f
                powerUpCreated = false
            }
        }
    }

    fun powerUpReset()
    {
        powerUpTimer = 0.0f
        powerUpCreated = false
    }

    public fun pauseGame(paused: Boolean)
    {
        this.gamePaused = paused
    }

    public fun getGamePaused() : Boolean
    {
        return gamePaused
    }

    override fun pause()
    {
        Content.dispose()
        Gdx.app.log("Content", "Disposing assets")
    }

    override fun resume()
    {
        Gdx.app.log("Content", "Reloading assets")
        Content.reloadAssets()
    }

    public fun stopOrPlayMusic(value: Boolean){
        AndroidInterface.stopMusic(value)
    }


    override fun dispose() {
        Content.dispose()
        batch.dispose()
        explosionEffectPool.clear()
    }
}
