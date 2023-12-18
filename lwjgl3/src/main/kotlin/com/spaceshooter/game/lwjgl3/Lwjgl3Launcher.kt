@file:JvmName("Lwjgl3Launcher")

package com.spaceshooter.game.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.spaceshooter.game.AndroidInterface
import com.spaceshooter.game.Main

/** Launches the desktop (LWJGL3) application. */
fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired())
        return

    val highScoreInterface = AndroidInterface.createDefault() // Create an instance or use another implementation

    val main = Main(highScoreInterface)
    Lwjgl3Application(main, Lwjgl3ApplicationConfiguration().apply {
        setTitle("SpaceShooter")
        setWindowedMode(1920, 1080)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
