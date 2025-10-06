package com.example.lostnfound

import android.content.Context // <-- TAMBAHKAN import ini
import android.content.Intent  // <-- TAMBAHKAN import ini
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
// Hapus import NavController jika hanya digunakan untuk Edit Profil
import com.example.lostnfound.database.AppDatabase
import com.example.lostnfound.databinding.FragmentProfileBinding
import com.example.lostnfound.utils.UserPreferences
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Hapus properti yang tidak digunakan jika ada
    // private lateinit var database: AppDatabase
    // private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Muat data user (kode Anda sudah benar)
        loadUserData()

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_editProfile)
        }

        // ================================================================
        // TAMBAHKAN BLOK KODE INI UNTUK FUNGSI LOGOUT
        // ================================================================
        binding.btnKeluar.setOnClickListener {
            // 1. Dapatkan SharedPreferences yang kita gunakan di SplashActivity
            val sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

            // 2. Ubah status login menjadi false
            with(sharedPreferences.edit()) {
                putBoolean("isLoggedIn", false)
                // Anda juga bisa menghapus isFirstTime jika perlu, tapi isLoggedIn sudah cukup
                apply()
            }

            // 3. Buat Intent untuk pindah ke SignInActivity
            val intent = Intent(requireActivity(), SignInActivity::class.java)

            // 4. Set flag untuk membersihkan semua activity sebelumnya (termasuk MainActivity)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // 5. Mulai activity baru
            startActivity(intent)

            // 6. Tutup activity saat ini (MainActivity) agar tidak bisa diakses lagi
            requireActivity().finish()
        }
        // ================================================================
    }

    private fun loadUserData() {
        val userPreferences = UserPreferences(requireContext())
        val database = AppDatabase.getDatabase(requireContext())
        val email = userPreferences.getLoggedInEmail()

        if (email != null) {
            lifecycleScope.launch {
                val user = database.userDao().getUserByEmail(email)
                // Pindahkan ke main thread untuk update UI
                user?.let {
                    // Update UI di main thread
                    binding.textName.setText(it.nama)
                    binding.textEmail.setText(it.email)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
