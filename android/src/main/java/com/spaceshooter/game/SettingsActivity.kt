package com.spaceshooter.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import MediaManager
import android.widget.Switch

class SettingsActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaManager
    private lateinit var btnReturn: Button
    private lateinit var switchMusic: Switch
    private lateinit var switchSfx: Switch
    private val musicMenu = R.raw.menu_music
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btnReturn = findViewById(R.id.btn_back)
        switchMusic = findViewById(R.id.switch_music)
        switchSfx = findViewById(R.id.switch_sfx)

        mediaPlayer = MediaManager

        //val resourceId = resources.getIdentifier("test_music", "raw", packageName)
        //mediaPlayer.initializeMediaPlayer(this, resourceId)
        //mediaPlayer.setStateCallback { isPlaying ->
        //    switchMusic.isChecked = isPlaying
        //}

        btnReturn.setOnClickListener {
            finish()
        }
        switchMusic.isChecked = mediaPlayer.isMediaPlayerPlaying()


        switchMusic.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) mediaPlayer.playBackgroundSound(this, musicMenu) else mediaPlayer.stopBackgroundSound()
        }
    }
}
