package com.example.lostnfound

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lostnfound.database.AppDatabase
import com.example.lostnfound.databinding.FragmentProfileBinding
import com.example.lostnfound.utils.UserPreferences
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: AppDatabase
    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Initialize database & preferences
        database = AppDatabase.getDatabase(requireContext())
        userPreferences = UserPreferences(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserData()

        binding.btnEditProfile.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_profile_to_editProfile)
            }, 500)
        }
    }

    private fun loadUserData() {
        val email = userPreferences.getLoggedInEmail()

        if (email != null) {
            lifecycleScope.launch {
                val user = database.userDao().getUserByEmail(email)

                activity?.runOnUiThread {
                    if (user != null) {
                        binding.textName.setText(user.nama)
                        binding.textEmail.setText(user.email)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload data ketika kembali dari edit
        loadUserData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}