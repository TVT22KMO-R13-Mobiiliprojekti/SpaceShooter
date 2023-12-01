import android.content.Context
import android.media.MediaPlayer
import android.util.Log

object MediaManager {

    private var mediaPlayer: MediaPlayer? = null
    private var currentResourceId: Int? = null

    fun initializeMediaPlayer(context: Context, resourceId: Int) {
        releaseMediaPlayer()
        mediaPlayer = MediaPlayer.create(context, resourceId)
        mediaPlayer?.isLooping = true
        currentResourceId = resourceId
    }

    fun startMediaPlayer() {
        mediaPlayer?.start()
    }

    fun pauseMediaPlayer() {
        mediaPlayer?.pause()
    }

    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun isMediaPlayerPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun stopMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.prepareAsync()
    }

    fun playBackgroundSound(context: Context, resourceId: Int) {
        Log.d("MediaManager", "Playing background sound")
        releaseMediaPlayer()
        mediaPlayer = MediaPlayer.create(context, resourceId)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
        currentResourceId = resourceId
    }

    fun stopBackgroundSound() {
        Log.d("MediaManager", "Stopping background sound")
        mediaPlayer?.stop()
        mediaPlayer?.prepareAsync()
        currentResourceId = null
    }

    fun switchMusic(context: Context, newResourceId: Int) {
        if (!isMediaPlayerPlaying()){
            return
        }
        if (currentResourceId != newResourceId) {
            stopBackgroundSound()
            playBackgroundSound(context, newResourceId)
        }
    }
}


