package com.example.lostnfound

import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lostnfound.databinding.FragmentEditPasswordBinding
import com.google.android.material.snackbar.Snackbar

class EditPasswordFragment : Fragment() {
    private var _binding: FragmentEditPasswordBinding? = null
    private val binding get() = _binding!!
    private var isSaved = false
    private var passwordVisible = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nunito = ResourcesCompat.getFont(requireContext(), R.font.nunito_sans)
        binding.etPasswordEdit.typeface = nunito
        binding.etPasswordConfirm.typeface = nunito
        binding.btnSimpan.typeface = nunito

        binding.btnToggleCurrentPasswordEye.setOnClickListener {
            passwordVisible = !passwordVisible
            if (passwordVisible) {
                binding.etPasswordEdit.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnToggleCurrentPasswordEye.setImageResource(R.drawable.ic_eye_open)
                binding.btnToggleCurrentPasswordEye.setColorFilter(ContextCompat.getColor(requireContext(), R.color.yellow))
            } else {
                binding.etPasswordEdit.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnToggleCurrentPasswordEye.setImageResource(R.drawable.ic_eye_closed)
                binding.btnToggleCurrentPasswordEye.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
            }
            binding.etPasswordEdit.setSelection(binding.etPasswordEdit.text.length)
        }

        binding.btnSimpan.setOnClickListener {
            val newPass = binding.etPasswordEdit.text.toString().trim()
            val confirmPass = binding.etPasswordConfirm.text.toString().trim()

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                showCustomSnackbar("Password tidak boleh kosong", R.color.yellow)
                isSaved = false
            } else if (newPass != confirmPass) {
                showCustomSnackbar("Password tidak cocok", R.color.yellow)
                isSaved = false
            } else if (!isValidPassword(newPass)) {
                showCustomSnackbar("Password tidak sesuai format", R.color.yellow)
                isSaved = false
            } else {
                showCustomSnackbar("Perubahan berhasil disimpan", R.color.orange)
                isSaved = true
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_editPassword_to_editProfile)
                }, 500)
            }
        }

        binding.btnBack.setOnClickListener {
            if (!isSaved) {
                showCustomSnackbar("Perubahan belum disimpan", R.color.yellow)
            }
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_editPassword_to_editProfile)
            }, 500)
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[./@])[A-Za-z\\d./@]{8,}$".toRegex()
        return regex.matches(password)
    }

    private fun showCustomSnackbar(message: String, colorRes: Int) {
        val parent = requireActivity().findViewById(android.R.id.content) as FrameLayout
        val snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        snackbarView.backgroundTintList=null

        snackbarView.setBackgroundColor(ContextCompat.getColor(requireContext(), colorRes))
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