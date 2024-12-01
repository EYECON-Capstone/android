package com.example.eyecon.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eyecon.databinding.FragmentNewsBinding

// NewsFragment.kt
class NewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewModel()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        viewModel.fetchNews()
    }

    private fun observeViewModel() {
        viewModel.newsState.observe(viewLifecycleOwner) { articles ->
            newsAdapter.updateArticles(articles)
        }

        viewModel.errorState.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}