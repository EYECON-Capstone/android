package com.example.eyecon.ui.home

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.eyecon.R
import com.example.eyecon.auth.LoginActivity
import com.example.eyecon.data.photo.local.entity.HistoryEntity
import com.example.eyecon.databinding.FragmentHomeBinding
import com.example.eyecon.ui.addphoto.AddPhotoViewModel
import com.example.eyecon.ui.addphoto.AddPhotoViewModelFactory
import com.example.eyecon.ui.game.GamesActivity
import com.example.eyecon.ui.gemini.GeminiChatbotActivity
import com.example.eyecon.ui.profile.ProfileActivity
import com.example.eyecon.ui.news.NewsHomeAdapter
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var newsAdapter: NewsHomeAdapter
    private val auth = FirebaseAuth.getInstance()
    private val historyViewModel by viewModels<AddPhotoViewModel>{
        AddPhotoViewModelFactory.getInstance(requireActivity())
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setEventData(data: List<HistoryEntity>) {
        val adapter = HistoryHorizontalAdapter()
        adapter.submitList(data.take(5))

        binding.rvHistoryHorizontal.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.games.setOnClickListener {
            val intent = Intent(requireActivity(), GamesActivity::class.java)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHistoryHorizontal.layoutManager = layoutManager



        historyViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        val currentUser = FirebaseAuth.getInstance().currentUser
        historyViewModel.getHistoryByUserId(currentUser!!.uid)
        historyViewModel.listHistory.observe(viewLifecycleOwner) {
            setEventData(it)
        }

        binding.geminibot.setOnClickListener {
            val intent = Intent(requireActivity(), GeminiChatbotActivity::class.java)
            startActivity(intent)
        }



        setupNewsRecyclerView()
        setupProfileSection()
        setupLogoutButton()

        return root
    }

    private fun setupNewsRecyclerView() {
        newsAdapter = NewsHomeAdapter { article ->
            article.url?.let { url ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Tidak dapat membuka berita: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } ?: run {
                Toast.makeText(
                    context,
                    "URL berita tidak tersedia",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.recyclerViewNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }

        homeViewModel.newsArticles.observe(viewLifecycleOwner) { articles ->
            newsAdapter.submitList(articles)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        homeViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        homeViewModel.loadNews()
    }

    private fun setupProfileSection() {
        homeViewModel.showusername()
        homeViewModel.username.observe(viewLifecycleOwner) { username ->
            if (!username.isNullOrEmpty()) {
                binding.textUsername.text = username
            } else {
                binding.textUsername.text = getString(R.string.default_username)
            }
        }

        homeViewModel.showphoto()
        homeViewModel.profilePhotoUrl.observe(viewLifecycleOwner) { photoUrl ->
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .circleCrop()
                    .into(binding.imageView)
            } else {
                Glide.with(this)
                    .load(R.drawable.baseline_person_24)
                    .circleCrop()
                    .into(binding.imageView)
            }
        }

        binding.imageView.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLogoutButton() {
        binding.keluar.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya") { _, _ ->
                    val sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.clear()  // Menghapus data login
                    editor.apply()

                    // Logout dari Firebase
                    auth.signOut()
                    Toast.makeText(requireContext(), "Berhasil keluar", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }
}