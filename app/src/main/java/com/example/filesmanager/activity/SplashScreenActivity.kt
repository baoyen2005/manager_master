package com.example.filesmanager.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.filesmanager.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.isIndeterminate = true
        val handler = Handler()

        handler.postDelayed(Runnable() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },4000)

    }
}