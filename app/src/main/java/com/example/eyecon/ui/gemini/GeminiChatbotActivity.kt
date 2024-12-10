package com.example.eyecon.ui.gemini

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eyecon.data.news.dataclass.ChatMessage
import com.example.eyecon.databinding.ActivityGeminiChatbotBinding
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeminiChatbotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGeminiChatbotBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var generativeModel: GenerativeModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeminiChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupGeminiAPI()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@GeminiChatbotActivity)
        }
    }

    private fun setupGeminiAPI() {
        val apiKey = "AIzaSyCf1JXoE-EvKGO6KxDi-vfbsCSKb7SnUSE"
        generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey
        )
    }

    private fun setupClickListeners() {
        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                binding.messageInput.text.clear()
                binding.messageInput.isEnabled = false
                binding.sendButton.isEnabled = false
            }
        }
    }

    private fun showLoading() {
        binding.loadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingProgressBar.visibility = View.GONE
        binding.messageInput.isEnabled = true
        binding.sendButton.isEnabled = true
    }

    private fun sendMessage(message: String) {
        chatAdapter.addMessage(ChatMessage(message))
        showLoading()

        lifecycleScope.launch {
            try {
                val chat = generativeModel.startChat()
                val response = withContext(Dispatchers.IO) {
                    chat.sendMessage(message)
                }

                val cleanResponse = response.text?.replace("\\*+".toRegex(), "") ?: "Maaf, saya tidak bisa menghasilkan respons"
                chatAdapter.addMessage(ChatMessage(cleanResponse, isBot = true))

                binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
            } catch (e: Exception) {
                Toast.makeText(
                    this@GeminiChatbotActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                hideLoading()
            }
        }
    }
}