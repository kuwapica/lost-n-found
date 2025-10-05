package com.example.lostnfound.ui.addpost

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lostnfound.R
import com.example.lostnfound.data.model.FormStatus
import com.example.lostnfound.databinding.FragmentFormLostBinding
import com.example.lostnfound.ui.viewmodel.LostItemViewModel
import java.util.Locale

class FormLostFragment : Fragment() {
    private var _binding: FragmentFormLostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LostItemViewModel by viewModels()
    private var selectedDate: Calendar = Calendar.getInstance()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openGallery()
        } else {
            Toast.makeText(
                requireContext(),
                "Permission ditolak. Tidak dapat memilih foto.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.setSelectedImage(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormLostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCLickListeners()
        observeViewModel()
    }

    private fun setupCLickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.imgUploadContainer.setOnClickListener {
            checkPermissionAndOpenGallery()
        }

        binding.etWaktu.setOnClickListener {
            showDatePicker()
        }

        binding.btnSubmit.setOnClickListener {
            submitForm()
        }

        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeViewModel() {
        viewModel.selectedImage.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                binding.ivImgUpload.setImageURI(uri)
                binding.tvUploadPlaceholder.visibility = View.GONE
            } else {
                binding.ivImgUpload.setImageDrawable(null)
                binding.tvUploadPlaceholder.visibility = View.VISIBLE
            }
        }

        viewModel.formStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is FormStatus.Loading -> {
                    showLoading(true)
                }
                is FormStatus.Success -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetFormStatus()
                    findNavController().navigateUp()
                }
                is FormStatus.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetFormStatus()
                }
                is FormStatus.Idle -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun checkPermissionAndOpenGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(
                    requireContext(),
                    "Permission diperlukan untuk memilih foto dari galeri.",
                    Toast.LENGTH_LONG
                ).show()
                requestPermissionLauncher.launch(permission)
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dafOfMonth ->
                selectedDate.set(year, month, dafOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.etWaktu.setText(dateFormat.format(selectedDate.time))
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun submitForm() {
        val namaBarang = binding.etNamaBarang.text.toString().trim()
        val lokasiLost = binding.etLokasiLost.text.toString().trim()
        val waktu = binding.etWaktu.text.toString().trim()
        val deskripsi = binding.etDeskripsi.text.toString().trim()
        val imageUri = viewModel.selectedImage.value

        binding.etNamaBarang.error = null
        binding.etLokasiLost.error = null
        binding.etDeskripsi.error = null

        viewModel.submitLostItem(namaBarang, lokasiLost, waktu, deskripsi, imageUri)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnSubmit.isEnabled = !isLoading
        binding.btnCancel.isEnabled = !isLoading
        binding.imgUploadContainer.isEnabled = !isLoading

        if (isLoading) {
            binding.btnSubmit.text = "Loading..."
        } else {
            binding.btnSubmit.text = "Submit"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}