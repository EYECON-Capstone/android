package com.example.eyecon.ui.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.eyecon.MainActivity
import com.example.eyecon.R
import com.example.eyecon.auth.LoginActivity
import com.example.eyecon.databinding.ActivityMainBinding
import com.example.eyecon.databinding.ActivityProfileBinding
import com.example.eyecon.ui.CameraActivity
import com.example.eyecon.ui.CameraActivity.Companion.CAMERAX_RESULT
import com.example.eyecon.ui.home.HomeFragment


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.linear.setOnClickListener {
            val options = arrayOf("Open Camera", "Open Gallery")
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Choose an option")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> startCamera() // Option for Camera
                    1 -> startGallery() // Option for Gallery
                }
            }
            builder.show()
        }


        profileViewModel.showemail()
        profileViewModel.showusername()
        profileViewModel.showphoto()


        profileViewModel.email.observe(this) { email ->
            binding.email.text = email
        }

        profileViewModel.username.observe(this) { username ->
            binding.editusername.text = username
        }

        profileViewModel.profilePhotoUrl.observe(this) { photoUrl ->
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .circleCrop()
                    .into(binding.imageProfile)
            } else {
                Glide.with(this)
                    .load(R.drawable.baseline_person_24) // Default image
                    .circleCrop()
                    .into(binding.imageProfile)
            }
        }

        binding.editusername.setOnClickListener {
            showEditUsernameDialog()
        }

        binding.saveButton.setOnClickListener {
            // Perbarui foto profil (jika ada)

            profileViewModel.isLoading.observe(this) {
                showLoading(it)
            }

            profileViewModel.profilePhotoUrl.observe(this) { uri ->
                if (uri != null) {
                    profileViewModel.updateProfilePhoto(uri)
                }
            }


            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            profileViewModel.updateProfilePhoto(uri) // Perbarui nilai dengan URI baru
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }


    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            val uri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            if (uri != null) {
                profileViewModel.updateProfilePhoto(uri) // Perbarui nilai dengan URI baru
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }
    }

    private fun showEditUsernameDialog() {
        val currentUsername = binding.editusername.text.toString()
        val editText = EditText(this)
        editText.setText(currentUsername)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Username")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val newUsername = editText.text.toString().trim()
                if (newUsername.isNotEmpty()) {
                    profileViewModel.updateUsername(newUsername)
                } else {
                    Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        // Simulate clicking the "Save" button when the back button is pressed
        binding.saveButton.performClick()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}
