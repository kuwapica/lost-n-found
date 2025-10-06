package com.example.lostnfound

// SplashActivity.kt
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Gunakan Handler untuk menunda navigasi agar splash screen terlihat sesaat
        Handler(Looper.getMainLooper()).postDelayed({
            // Cek status login
            val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
            val isFirstTime = sharedPreferences.getBoolean("isFirstTime", true)

            if (isLoggedIn) {
                // Jika sudah login, langsung ke MainActivity
                goToActivity(MainActivity::class.java)
            } else if (isFirstTime) {
                // Jika ini pertama kalinya, ke SignUpActivity
                // Tandai bahwa ini bukan lagi pertama kali
                sharedPreferences.edit().putBoolean("isFirstTime", false).apply()
                goToActivity(SignUpActivity::class.java)
            } else {
                // Jika bukan pertama kali dan belum login, ke SignInActivity
                goToActivity(SignInActivity::class.java)
            }
        }, 1500) // Tunda 1.5 detik
    }

    private fun goToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish() // Tutup SplashActivity agar tidak bisa kembali ke sini
    }
}
