package com.example.eyecon.ui.diagnosa

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eyecon.data.dataclass.Diagnosa
import com.example.eyecon.databinding.FragmentDiagnosaBinding

class DiagnosaFragment : Fragment() {
    private var _binding: FragmentDiagnosaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DiagnosaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiagnosaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.loadDiagnosa(requireContext())
        observeDiagnosa()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = DiagnosaAdapter { diagnosa ->
                navigateToDetail(diagnosa)
            }
        }
    }

    private fun observeDiagnosa() {
        viewModel.diagnosaList.observe(viewLifecycleOwner) { diagnosaList ->
            (binding.recyclerView.adapter as DiagnosaAdapter).submitList(diagnosaList)
        }
    }

    private fun navigateToDetail(diagnosa: Diagnosa) {
        val intent = Intent(requireContext(), DetailDiagnosaActivity::class.java).apply {
            putExtra(EXTRA_DIAGNOSA_ID, diagnosa.id)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_DIAGNOSA_ID = "extra_diagnosa_id"
    }
}