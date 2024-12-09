package com.example.eyecon.ui.game

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eyecon.R
import com.example.eyecon.databinding.ActivityGamesBinding

class GamesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamesBinding

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