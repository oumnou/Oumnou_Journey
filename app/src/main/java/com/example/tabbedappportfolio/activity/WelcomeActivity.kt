package com.example.tabbedappportfolio.activity

import android.content.Intent
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import androidx.appcompat.app.AppCompatActivity
import com.example.tabbedappportfolio.R


class WelcomeActivity : AppCompatActivity() {
    private val delayMillis: Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_layout)

        var textureView:TextureView = findViewById(R.id.textureView)
        var mediaPlayer = MediaPlayer.create(this, R.raw.bg_video5)


        textureView.surfaceTextureListener = object : SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                val surface = Surface(surface)
                mediaPlayer.setSurface(surface)
                mediaPlayer.isLooping = true
                mediaPlayer.start()
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                // Cleanup resources if needed
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                // Update if needed
            }
        }

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, delayMillis)


}
}