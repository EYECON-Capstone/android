package com.example.eyecon.ui.history

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.eyecon.R
import com.example.eyecon.data.news.dataclass.Rekomendasi
import com.example.eyecon.databinding.ActivityHistoryResultBinding
import com.example.eyecon.ui.addphoto.AddPhotoViewModel
import com.example.eyecon.ui.addphoto.AddPhotoViewModelFactory
import com.example.eyecon.ui.diagnosa.RekomendasiAdapter
import kotlinx.coroutines.launch

class HistoryResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryResultBinding
    private lateinit var rekomendasiAdapter: RekomendasiAdapter
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

        rekomendasiAdapter = RekomendasiAdapter()
        binding.rekomendasiRecyclerView.adapter = rekomendasiAdapter

        supportActionBar?.title = "Results"
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_green)

        val id = intent.getStringExtra(EXTRA_ID) ?: ""
        detailViewModel.getDetailPhoto(id)

        detailViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        lifecycleScope.launch {
            detailViewModel.detailphoto.observeForever { result ->
                result?.onSuccess { detail ->
                    Glide.with(this@HistoryResultActivity)
                        .load(detail.imgUrl)
                        .into(binding.resultImage)

                    binding.resultText.text = "Result: ${detail.result}"
                    binding.diagnosisText.text = detail.diagnosa

                    val recommendations = getRecommendations(detail.result)
                    rekomendasiAdapter.submitList(recommendations)
                }?.onFailure {
                }
            }
        }
    }

    private fun getRecommendations(result: String): List<Rekomendasi> {
        Log.d("Recommendations", "Processing result: $result")

        return when (result) {
            "Mata Merah", "Result: Mata Merah" -> listOf(
                Rekomendasi(R.drawable.ic_medicine, getString(R.string.red_eye_rec1)),
                Rekomendasi(R.drawable.ic_medicine, getString(R.string.red_eye_rec2)),
            )
            "Mata Juling", "Result: Mata Juling" -> listOf(
                Rekomendasi(R.drawable.ic_medicine, getString(R.string.strabismus_rec1)),
                Rekomendasi(R.drawable.ic_medicine, getString(R.string.strabismus_rec2)),
            )
            "Mata Normal", "Result: Mata Normal" -> listOf(
                Rekomendasi(R.drawable.ic_medicine, getString(R.string.conjunctivitis_rec1)),
                Rekomendasi(R.drawable.ic_medicine, getString(R.string.conjunctivitis_rec2)),
            )
            "Kantung Mata", "Result: Kantung Mata" -> listOf(
                Rekomendasi(R.drawable.ic_medicine, getString(R.string.eye_bags_rec1)),
                Rekomendasi(R.drawable.ic_medicine, getString(R.string.eye_bags_rec2)),
            )
            else -> {
                Log.e("Recommendations", "Unmatched result: $result")
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, getString(R.string.conjunctivitis_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, getString(R.string.conjunctivitis_rec2)),
                )
            }
        }
    }
}