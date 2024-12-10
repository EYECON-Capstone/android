package com.example.eyecon.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eyecon.data.photo.local.entity.HistoryEntity
import com.example.eyecon.databinding.FragmentHistoryBinding
import com.example.eyecon.ui.addphoto.AddPhotoViewModel
import com.example.eyecon.ui.addphoto.AddPhotoViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
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
        val adapter = HistoryAdapter()
        adapter.submitList(data)

        binding.rvHistory.adapter = adapter
    }
    private fun handleDeleteAction() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            // Show confirmation dialog before proceeding with deletion
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Apakah anda yakin ingin menghapus semua history?")
                .setCancelable(false)
                .setPositiveButton("Ya") { dialog, _ ->
                    // If user confirms, call the deleteHistory method in ViewModel
                    historyViewModel.deleteHistory(userId)
                    dialog.dismiss()
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    // If user cancels, dismiss the dialog
                    dialog.dismiss()
                }

            val alert = dialogBuilder.create()
            alert.show()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        historyViewModel.isDeleted.observe(viewLifecycleOwner) { isDeleted ->
            if (isDeleted) {
                // Successfully deleted, update UI or show a message

                historyViewModel.getHistoryByUserId(FirebaseAuth.getInstance().currentUser!!.uid)
            } else {
                // Failed to delete, show error message
                Toast.makeText(requireContext(), "Failed to delete history", Toast.LENGTH_SHORT).show()
            }
        }

        binding.hapus.setOnClickListener {
            val historyList = historyViewModel.listHistory.value
            if (historyList.isNullOrEmpty()) {
                // Show a toast if the history list is empty
                Toast.makeText(requireContext(), "History is empty", Toast.LENGTH_SHORT).show()
            } else {
                // Trigger the delete action when "hapus" is clicked
                handleDeleteAction()
            }
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHistory.layoutManager = layoutManager

        historyViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        val currentUser = FirebaseAuth.getInstance().currentUser
        historyViewModel.getHistoryByUserId(currentUser!!.uid)
        historyViewModel.listHistory.observe(viewLifecycleOwner) {
            setEventData(it)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}