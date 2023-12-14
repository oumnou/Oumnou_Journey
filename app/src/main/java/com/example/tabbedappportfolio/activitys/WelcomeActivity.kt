package com.example.tabbedappportfolio.activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tabbedappportfolio.R


class WelcomeActivity : AppCompatActivity() {
    private val delayMillis: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hello)

        // Use a Handler to delay the start of the next activity
        Handler().postDelayed({
            // Create an Intent to start the next activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Finish the current activity to prevent it from coming back when the user presses the back button
            finish()
        }, delayMillis)
    }

}