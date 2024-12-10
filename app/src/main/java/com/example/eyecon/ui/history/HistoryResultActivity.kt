package com.example.eyecon.ui.history

import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.eyecon.R
import com.example.eyecon.databinding.ActivityHistoryResultBinding
import com.example.eyecon.ui.addphoto.AddPhotoViewModel
import com.example.eyecon.ui.addphoto.AddPhotoViewModelFactory
import kotlinx.coroutines.launch

class HistoryResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryResultBinding
    private val detailViewModel by viewModels<AddPhotoViewModel> {
        AddPhotoViewModelFactory.getInstance(application)
    }
    companion object {
        const val EXTRA_ID = "extra_id"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Results"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.statusBarColor = ContextCompat.getColor(this, R.color.dark_green)
        }

        val id = intent.getStringExtra(EXTRA_ID) ?: ""
        detailViewModel.getDetailPhoto(id)

        lifecycleScope.launch {
            detailViewModel.detailphoto.observeForever { result ->
                result?.onSuccess {detail->
                    Glide.with(this@HistoryResultActivity)
                        .load(detail.imgUrl)
                        .into(binding.resultImage)

                    binding.resultText.text = detail.result
                    binding.diagnosisText.text = detail.diagnosa

                }?.onFailure {
                    // Handle the error
                }
            }
        }

    }
}