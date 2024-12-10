package com.example.eyecon.ui.game

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eyecon.databinding.ActivityGamesBinding

class GamesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamesBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding object
        binding = ActivityGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Use the binding object to access the WebView
        val webView = binding.webView
        webView.settings.javaScriptEnabled = true

        // Load the URL in the WebView
        webView.loadUrl("https://www.crazygames.com/game/what-s-the-difference-online")
    }
}