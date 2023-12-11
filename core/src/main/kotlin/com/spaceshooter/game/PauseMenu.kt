package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton


class PauseMenu(private val skin: Skin, private val stage: Stage) {


    private val dialog: Dialog = createMenuDialog()

    private fun createMenuDialog(): Dialog {
        val dialog = Dialog("Menu", skin)

        val quitButton = TextButton("Quit", skin)
        quitButton.addListener {
            // Stop game
            //Gdx.app.exit()
            true
        }
        val continueButton = TextButton("Continue", skin)
        quitButton.addListener {
            // Add functionality
            true
        }
        val musicButton = TextButton("Music On/Off", skin)
        musicButton.addListener {
            // Add functionality
            true
        }

        val sfxButton = TextButton("SFX On/Off", skin)
        sfxButton.addListener {
            // Add functionality
            true
        }

        dialog.contentTable.add(quitButton).row()
        dialog.contentTable.add(continueButton).row()
        dialog.contentTable.add(musicButton).row()
        dialog.contentTable.add(sfxButton).row()

        dialog.isMovable = false
        dialog.setSize(1000f, 600f)
        dialog.setPosition(550f,220f)

        return dialog
    }

    fun show() {
        stage.addActor(dialog)
    }
}


