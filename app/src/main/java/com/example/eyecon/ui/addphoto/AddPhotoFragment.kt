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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.eyecon.R
import com.example.eyecon.data.photo.remote.PhotoRepository
import com.example.eyecon.databinding.FragmentAddPhotoBinding
import com.example.eyecon.ui.CameraActivity
import com.example.eyecon.ui.detail.ResultActivity
import com.example.eyecon.utils.reduceFileImage
import com.example.eyecon.utils.uriToFile
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class AddPhotoFragment : Fragment() {
    private var _binding: FragmentAddPhotoBinding? = null
    private val binding get() = _binding!!
    private val addPhotoViewModel by viewModels<AddPhotoViewModel>{
        AddPhotoViewModelFactory.getInstance(requireActivity())
    }
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddPhotoBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
        addPhotoViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
//
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