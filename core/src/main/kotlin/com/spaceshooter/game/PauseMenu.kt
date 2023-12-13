package com.spaceshooter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton

class PauseMenu(private val skin: Skin, private val stage: Stage) {
    private val fontScale = 3.0f
    private val dialog: Dialog = createMenuDialog()

    private fun createMenuDialog(): Dialog {
        val titleLabel = Label("Pause menu", skin).apply {
            setFontScale(fontScale)
        }
        val dialog = Dialog("", skin).apply {
            contentTable.add(titleLabel).padTop(-200f).padBottom(50f).row()
        }

        val buttonStyle = skin.get(TextButton.TextButtonStyle::class.java)
        val scaledFont = BitmapFont().apply {
            data.setScale(fontScale) // Set font scale
        }

        buttonStyle.font = scaledFont // Set scaled font to style

        val quitButton = TextButton("Quit", skin)
        quitButton.style = buttonStyle
        quitButton.addListener {
            // Stop game
            // Gdx.app.exit()
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
            true
        }

        dialog.contentTable.add(quitButton).row()
        dialog.contentTable.add(continueButton).row()
        dialog.contentTable.add(musicButton).row()
        dialog.contentTable.add(sfxButton).row()

        dialog.isMovable = false
        dialog.setSize(900f, 600f)
        dialog.setPosition(700f,220f)

        return dialog
    }

    fun show() {
        stage.addActor(dialog)
    }
}


