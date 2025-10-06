package com.example.lostnfound

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lostnfound.ui.addpost.AddFragment
import com.example.lostnfound.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi BottomNavigationView dari layout
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Atur listener untuk item yang dipilih pada navigasi bawah
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                // Jika item 'home' dipilih
                R.id.nav_home -> {
                    replaceFragment(HomeFragment()) // Ganti fragment dengan HomeFragment
                    true // Kembalikan true untuk menandakan event sudah ditangani
                }
                R.id.nav_submit -> {
                    replaceFragment(AddFragment()) // Ganti fragment dengan ProfileFragment
                    true // Kembalikan true
                }
                // Jika item 'profile' dipilih
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment()) // Ganti fragment dengan ProfileFragment
                    true // Kembalikan true
                }
                // Untuk kasus lain, jangan lakukan apa-apa
                else -> false
            }
        }

        // Saat activity pertama kali dibuat, tampilkan HomeFragment sebagai default
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_home
        }
    }

    /**
     * Fungsi helper untuk mengganti fragment yang sedang ditampilkan di dalam FragmentContainerView.
     * @param fragment Fragment baru yang akan ditampilkan.
     */
    private fun replaceFragment(fragment: Fragment) {
        // Mulai transaksi FragmentManager
        supportFragmentManager.beginTransaction()
            // Ganti isi dari 'R.id.fragment_container' dengan fragment yang baru
            .replace(R.id.fragment_container, fragment)
            // Terapkan perubahan
            .commit()
    }
}
