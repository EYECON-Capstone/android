package com.example.eyecon.ui.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.eyecon.R
import com.example.eyecon.databinding.ActivityMainBinding
import com.example.eyecon.databinding.ActivityProfileBinding


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Observe ViewModel LiveData
        profileViewModel.username.observe(this) { username ->
            binding.editusername.setText(username)
        }

        profileViewModel.email.observe(this) { email ->
            binding.email.text = email
        }

        profileViewModel.profilePhotoUrl.observe(this) { photoUrl ->
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .circleCrop() // Circular image
                    .into(binding.imageProfile)
            } else {
                Glide.with(this)
                    .load(R.drawable.baseline_person_24) // Default image if no profile photo
                    .circleCrop()
                    .into(binding.imageProfile)
            }
        }

        // Load the data from ViewModel
        profileViewModel.showusername()
        profileViewModel.showphoto()
        profileViewModel.showemail()
    }
}
