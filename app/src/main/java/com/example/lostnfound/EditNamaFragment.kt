package com.example.lostnfound

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lostnfound.database.AppDatabase
import com.example.lostnfound.database.LostFoundDatabase
import com.example.lostnfound.databinding.FragmentEditNamaBinding
import com.example.lostnfound.utils.UserPreferences
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class EditNamaFragment : Fragment() {

    private var _binding: FragmentEditNamaBinding? = null
    private val binding get() = _binding!!
    private var isSaved = false

    private lateinit var database: LostFoundDatabase
    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNamaBinding.inflate(inflater, container, false)

        // Initialize database & preferences
        database = LostFoundDatabase.getDatabase(requireContext())
        userPreferences = UserPreferences(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nunito = ResourcesCompat.getFont(requireContext(), R.font.nunito_sans)
        binding.etNamaEdit.typeface = nunito
        binding.btnSimpan.typeface = nunito

        // Load nama user saat ini
        loadCurrentUserData()

        binding.btnSimpan.setOnClickListener {
            val newName = binding.etNamaEdit.text.toString().trim()

            if (newName.isEmpty()) {
                showCustomSnackbar("Nama tidak boleh kosong", R.color.yellow)
                isSaved = false
            } else {
                val email = userPreferences.getLoggedInEmail()
                if (email != null) {
                    lifecycleScope.launch {
                        database.userDao().updateNama(email, newName)

                        activity?.runOnUiThread {
                            showCustomSnackbar("Perubahan berhasil disimpan", R.color.orange)
                            isSaved = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                findNavController().navigate(R.id.action_editNama_to_editProfile)
                            }, 700)
                        }
                    }
                } else {
                    showCustomSnackbar("Terjadi kesalahan", R.color.red)
                    isSaved = false
                }
            }
        }

        binding.btnBack.setOnClickListener {
            if (!isSaved) {
                showCustomSnackbar("Perubahan belum disimpan", R.color.yellow)
            }
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_editNama_to_editProfile)
            }, 500)
        }
    }

    private fun loadCurrentUserData() {
        val email = userPreferences.getLoggedInEmail()

        if (email != null) {
            lifecycleScope.launch {
                val user = database.userDao().getUserByEmail(email)
                activity?.runOnUiThread {
                    if (user != null) {
                        binding.etNamaEdit.setText(user.nama)
                    }
                }
            }
        }
    }

    private fun showCustomSnackbar(message: String, colorResId: Int) {
        val parent = requireActivity().findViewById(android.R.id.content) as FrameLayout
        val snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        snackbarView.backgroundTintList = null

        snackbarView.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResId))
        snackbarView.setPadding(32, 48, 32, 48)
        snackbarView.minimumHeight = 120

        val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        textView.typeface = ResourcesCompat.getFont(requireContext(), R.font.nunito_sans)
        textView.textSize = 18f
        textView.gravity = Gravity.CENTER
        textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        params.topMargin = 100
        params.width = FrameLayout.LayoutParams.MATCH_PARENT
        snackbarView.layoutParams = params

        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}