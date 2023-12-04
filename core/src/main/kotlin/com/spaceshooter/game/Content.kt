package com.spaceshooter.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture


class Content {
    private var manager = AssetManager()

    fun initialize()
    {
        loadTextures()
        loadSounds()
        loadMusic()
    }

    private fun loadTextures()
    {
        manager.load("player.png", Texture::class.java)
        manager.load("enemy.png", Texture::class.java)
        manager.load("bullet.png", Texture::class.java)
        manager.load("bkgd_0.png", Texture::class.java)
        manager.load("bkgd_1.png", Texture::class.java)
        manager.load("ships_void.png", Texture::class.java)
        manager.load("arcade_space_shooter.png", Texture::class.java)
        manager.load("beams.png", Texture::class.java)
        manager.load("M484BulletCollection1.png", Texture::class.java)
        manager.load("ships_biomech.png", Texture::class.java)
        manager.load("ships_saucer.png", Texture::class.java)
        manager.finishLoading()
    }

    fun loadSounds()
    {

    }

    fun loadMusic()
    {

    }

    fun getAssetManager() : AssetManager
    {
        return this.manager
    }

    fun getTexture(texturePath: String): Texture {
        if (manager.isLoaded(texturePath)) {
            // texture is available, get it with the assetmanager and return it
            val tex: Texture = manager.get(texturePath, Texture::class.java)

            return tex
        }
        else
        {
            //If texture hasn't loaded, try to load it and return it
            val load = manager.load(texturePath, Texture::class.java)

            manager.finishLoading()

            val tex = manager.get(texturePath, Texture::class.java)

            return tex
        }
    }


}
