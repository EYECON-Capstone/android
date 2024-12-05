package com.example.eyecon.ui.addphoto

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.eyecon.R
import com.example.eyecon.databinding.FragmentAddPhotoBinding
import com.example.eyecon.ui.CameraActivity
import com.example.eyecon.ui.detail.ResultActivity


class AddPhotoFragment : Fragment() {
    private var _binding: FragmentAddPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var addPhotoViewModel: AddPhotoViewModel
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddPhotoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        addPhotoViewModel = ViewModelProvider(this)[AddPhotoViewModel::class.java]

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.buttonGallery.setOnClickListener { startGallery() }
        binding.buttonCamera.setOnClickListener { startCamera() }
        binding.fabAction.setOnClickListener {
            addPhotoViewModel.currentImageUri.value?.let {
                analyzeImage(it)
            } ?: run {
                Toast.makeText(requireContext(), getString(R.string.empty_image_warning), Toast.LENGTH_SHORT).show()

            }
        }
        addPhotoViewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                binding.rvImage.setImageURI(it)
            }
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.dark_green))
        )

        return root
    }
    private fun startGallery() {
        binding.rvImage.setImageURI(null)
        binding.rvImage.setImageResource(R.drawable.baseline_image_24)
        addPhotoViewModel.setCurrentImageUri(null)
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            addPhotoViewModel.setCurrentImageUri(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        binding.rvImage.setImageURI(null)
        binding.rvImage.setImageResource(R.drawable.baseline_image_24)
        addPhotoViewModel.setCurrentImageUri(null)
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraActivity.CAMERAX_RESULT) {
            val uri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            if (uri != null) {
                addPhotoViewModel.setCurrentImageUri(uri)
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }
    }

    private fun analyzeImage(uri: Uri) {
        val intent = Intent(requireContext(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
        startActivity(intent)
    }
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}