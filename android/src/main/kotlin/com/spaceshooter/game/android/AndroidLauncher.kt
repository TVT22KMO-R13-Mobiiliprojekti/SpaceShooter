package com.spaceshooter.game.android

import MediaManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.spaceshooter.game.Main
import com.spaceshooter.game.MainActivity

/** Launches the Android application. */
class AndroidLauncher : AndroidApplication() {

    private lateinit var mediaPlayer: MediaManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(Main(), AndroidApplicationConfiguration().apply {
            // Configure your application here.
            useImmersiveMode = true // Recommended, but not required.
        })
        val intent = intent
        val musicResource = intent.getIntExtra("musicResource", 0)
        mediaPlayer = MediaManager
        mediaPlayer.initializeMediaPlayer(this, musicResource)
        mediaPlayer.startMediaPlayer()
    }
    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog(){
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want exit game")
            .setPositiveButton("Yes") { _, _ ->
                // User confirmed, so allow the default behavior
                //val intent = Intent(this, MainActivity::class.java)
                mediaPlayer.stopMediaPlayer()
                finish()
                //startActivity(intent)

            }
            .setNegativeButton("No") { dialog, _ ->
                // User canceled, so dismiss the dialog
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }


}
