package com.example.eyecon.ui.diagnosa

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eyecon.R
import com.example.eyecon.databinding.ActivityDetailDiagnosaBinding

class DetailDiagnosaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDiagnosaBinding
    private val viewModel: DiagnosaViewModel by viewModels()
    private lateinit var rekomendasiAdapter: RekomendasiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiagnosaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.keySet()?.forEach { key ->
            Log.d("DetailDiagnosa", "Intent extra: $key = ${intent.extras?.get(key)}")
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.detail_diagnosa)
        }

        setupRecyclerView()

        intent.extras?.keySet()?.forEach { key ->
            Log.d("DetailDiagnosa", "Intent extra: $key = ${intent.extras?.get(key)}")
        }

        val diagnosaId = when {
            intent.hasExtra(EXTRA_DIAGNOSA_ID) ->
                intent.getIntExtra(EXTRA_DIAGNOSA_ID, -1)
            intent.hasExtra("DIAGNOSA_ID") ->
                intent.getIntExtra("DIAGNOSA_ID", -1)
            else -> -1
        }

        Log.d("DetailDiagnosa", "Received diagnosaId: $diagnosaId")

        if (diagnosaId != -1) {
            setupUI(diagnosaId)
        } else {
            showError()
        }

        binding.swipeRefresh.setOnRefreshListener {
            setupUI(diagnosaId)
        }
    }

    private fun setupRecyclerView() {
        rekomendasiAdapter = RekomendasiAdapter()

        binding.rekomendasiRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DetailDiagnosaActivity)
            adapter = rekomendasiAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupUI(diagnosaId: Int) {
        Log.d("DetailDiagnosa", "Setting up UI for diagnosaId: $diagnosaId")
        showLoading(true)

        viewModel.loadDiagnosa(this)
        viewModel.diagnosaList.observe(this) { diagnosaList ->
            Log.d("DetailDiagnosa", "Loaded list size: ${diagnosaList.size}")

            diagnosaList.forEach {
                Log.d("DetailDiagnosa", "Available diagnosa: id=${it.id}, title=${it.title}")
            }

            val diagnosa = diagnosaList.find { it.id == diagnosaId }
            Log.d("DetailDiagnosa", "Found diagnosa: ${diagnosa?.title} for id: $diagnosaId")

            if (diagnosa != null) {
                binding.apply {
                    diagnosaImage.setImageResource(diagnosa.imageResId)
                    titleText.text = diagnosa.title
                    subtitleText.text = diagnosa.subtitle
                    descriptionText.text = diagnosa.description
                    rekomendasiAdapter.submitList(diagnosa.recommendations)

                    contentGroup.isVisible = true
                    errorGroup.isVisible = false
                }
            } else {
                Log.e("DetailDiagnosa", "Failed to find diagnosa with id: $diagnosaId")
                showError()
            }

            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            swipeRefresh.isRefreshing = isLoading
            progressBar.isVisible = isLoading && !swipeRefresh.isRefreshing
            contentGroup.isVisible = !isLoading
        }
    }

    private fun showError() {
        binding.apply {
            contentGroup.isVisible = false
            errorGroup.isVisible = true
            errorText.text = getString(R.string.error_loading_diagnosa)
            retryButton.setOnClickListener {
                val diagnosaId = when {
                    intent.hasExtra(EXTRA_DIAGNOSA_ID) ->
                        intent.getIntExtra(EXTRA_DIAGNOSA_ID, -1)
                    intent.hasExtra("DIAGNOSA_ID") ->
                        intent.getIntExtra("DIAGNOSA_ID", -1)
                    else -> -1
                }
                setupUI(diagnosaId)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_DIAGNOSA_ID = "extra_diagnosa_id"
    }
}