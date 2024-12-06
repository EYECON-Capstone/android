package com.example.eyecon.ui.diagnosa

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.eyecon.data.dataclass.Rekomendasi
import com.example.eyecon.R
import com.example.eyecon.databinding.ActivityDetailDiagnosaBinding

class DetailDiagnosaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDiagnosaBinding
    private val viewModel: DiagnosaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiagnosaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val diagnosaId = intent.getIntExtra(DiagnosaFragment.EXTRA_DIAGNOSA_ID, -1)
        setupUI(diagnosaId)
    }

    private fun setupUI(diagnosaId: Int) {
        viewModel.diagnosaList.observe(this) { diagnosaList ->
            val diagnosa = diagnosaList.find { it.id == diagnosaId } ?: return@observe

            binding.apply {
                diagnosaImage.setImageResource(diagnosa.imageResId)
                titleText.text = diagnosa.title
                descriptionText.text = diagnosa.description
                setupRekomendasi(diagnosa.recommendations)
            }
        }
    }

    private fun setupRekomendasi(rekomendasi: List<Rekomendasi>) {
        binding.rekomendasiLayout.apply {
            removeAllViews()
            rekomendasi.forEach { item ->
                addView(createRekomendasiView(item))
            }
        }
    }

    private fun createRekomendasiView(rekomendasi: Rekomendasi): View {
        val view = layoutInflater.inflate(R.layout.item_rekomendasi, null)

        // Set icon
        view.findViewById<ImageView>(R.id.iconView).setImageResource(rekomendasi.icon)

        // Set title
        view.findViewById<TextView>(R.id.titleText).text = rekomendasi.title

        // Set description (tambahan)
        //view.findViewById<TextView>(R.id.descriptionText).text = rekomendasi.description

        return view
    }
}