package com.example.eyecon.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.eyecon.R
import com.example.eyecon.auth.LoginActivity
import com.example.eyecon.databinding.FragmentHomeBinding
import com.example.eyecon.ui.profile.ProfileActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.showusername()
        // Display the username
        homeViewModel.username.observe(viewLifecycleOwner) { username ->
            binding.textUsername.text = username
        }
        homeViewModel.showphoto()
        homeViewModel.profilePhotoUrl.observe(viewLifecycleOwner) { photoUrl ->
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .circleCrop() // Circular image
                    .into(binding.imageView)
            } else {
                Glide.with(this)
                    .load(R.drawable.baseline_person_24) // Default image if no profile photo
                    .circleCrop()
                    .into(binding.imageView)
            }
        }
        binding.textUsername.setOnClickListener {
            homeViewModel.signOut() // Sign out using the ViewModel
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.imageView.setOnClickListener{
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }
}
