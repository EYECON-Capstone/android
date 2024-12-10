package com.example.eyecon.ui.detail

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.eyecon.R
import com.example.eyecon.data.photo.local.entity.HistoryEntity
import com.example.eyecon.databinding.ActivityResultBinding
import com.example.eyecon.ui.addphoto.AddPhotoViewModel
import com.example.eyecon.ui.addphoto.AddPhotoViewModelFactory
import com.example.eyecon.utils.reduceFileImage
import com.example.eyecon.utils.uriToFile
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val addPhotoViewModel by viewModels<AddPhotoViewModel>{
        AddPhotoViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Results"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.statusBarColor = ContextCompat.getColor(this, R.color.dark_green)
        }

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))

        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
            classifyImage(it)
        }

        addPhotoViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                // Show loading indicator
                binding.progressBar.visibility = View.VISIBLE
            } else {
                // Hide loading indicator
                binding.progressBar.visibility = View.GONE
            }
        })
        addPhotoViewModel.resultData.observe(this) { data ->
            data?.let {
                // Display the result, diagnosa, and createdAt in the UI
                binding.resultText.text = "Result: ${it.result}"
                binding.diagnosisText.text=it.diagnosa

                val currentUser = FirebaseAuth.getInstance().currentUser
                val historyEntity = HistoryEntity(
                    idUser = currentUser!!.uid,
                    id= it.id,
                    result = it.result,
                    createdAt = it.createdAt,
                    diagnosa = it.diagnosa,
                    imgUrl = it.imgUrl,
                )
                addPhotoViewModel.saveImageDetectionHistory(historyEntity)
            }
        }
        // Get photo URI from the Intent
    }
    private fun classifyImage(uri: Uri) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val userId = it.uid
            val imageFile = uriToFile(uri, applicationContext).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )
            val idUser = userId.toRequestBody("text/plain".toMediaType())

            // Calling the ViewModel to predict using the image and userId
            addPhotoViewModel.predictphoto(multipartBody, idUser)
        }
    }
    private fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_ID = "extra_id"
    }
}
