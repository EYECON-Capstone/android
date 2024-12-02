package com.example.eyecon.ui.diagnosa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.eyecon.databinding.FragmentDiagnosaBinding

class DiagnosaFragment : Fragment() {

    private var _binding: FragmentDiagnosaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val diagnosaViewModel =
            ViewModelProvider(this).get(DiagnosaViewModel::class.java)

        _binding = FragmentDiagnosaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDiagnosa
        diagnosaViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}