package com.spaceshooter.game

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Switch
import MediaManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaManager
    private lateinit var btnReturn: Button
    private lateinit var switchMusic: Switch
    private lateinit var switchSfx: Switch
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

        switchMusic.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) mediaPlayer.startMediaPlayer() else mediaPlayer.stopMediaPlayer()
        }
    }
}