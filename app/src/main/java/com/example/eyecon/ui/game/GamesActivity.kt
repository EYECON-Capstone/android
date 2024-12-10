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

        binding = ActivityGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val webView = binding.webView
        webView.settings.javaScriptEnabled = true

        webView.loadUrl("https://www.crazygames.com/game/what-s-the-difference-online")
    }
}