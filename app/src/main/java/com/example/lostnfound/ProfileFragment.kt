package com.example.lostnfound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lostnfound.database.LostFoundDatabase
import com.example.lostnfound.databinding.FragmentProfileBinding
import com.example.lostnfound.utils.UserPreferences
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: LostFoundDatabase
    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Initialize
        database = LostFoundDatabase.getDatabase(requireContext())
        userPreferences = UserPreferences(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set font
        val nunito = ResourcesCompat.getFont(requireContext(), R.font.nunito_sans)
        binding.textName.typeface = nunito
        binding.textEmail.typeface = nunito
        binding.btnEditProfile.typeface = nunito
        binding.btnKeluar.typeface = nunito

        // Load data user
        loadUserData()

        // Button Edit Profile
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_editProfile)
        }

        // Button Keluar (Logout)
        binding.btnKeluar.setOnClickListener {
            // Clear session
            userPreferences.clearSession()

            // Pindah ke SignInActivity
            val intent = Intent(requireActivity(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun loadUserData() {
        val email = userPreferences.getLoggedInEmail()

        if (email != null) {
            lifecycleScope.launch {
                val user = database.userDao().getUserByEmail(email)

                // Update UI di main thread
                activity?.runOnUiThread {
                    if (user != null) {
                        binding.textName.text = user.nama
                        binding.textEmail.text = user.email
                    } else {
                        binding.textName.text = "Data tidak ditemukan"
                        binding.textEmail.text = "Data tidak ditemukan"
                    }
                }
            }
        } else {
            binding.textName.text = "Tidak ada user login"
            binding.textEmail.text = "-"
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload data setiap kali fragment ditampilkan (termasuk setelah edit nama)
        loadUserData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}