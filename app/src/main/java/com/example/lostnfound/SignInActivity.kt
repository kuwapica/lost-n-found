package com.example.lostnfound

import android.content.Context // <-- Tambahkan import ini
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import androidx.lifecycle.lifecycleScope
import com.example.lostnfound.database.AppDatabase
import com.example.lostnfound.database.LostFoundDatabase
import com.example.lostnfound.utils.UserPreferences
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {

    private lateinit var database: LostFoundDatabase
    private lateinit var userPreferences: UserPreferences
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnMasuk: Button
    private lateinit var tvDaftar: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Initialize database & preferences
        database = LostFoundDatabase.getDatabase(this)
        userPreferences = UserPreferences(this)

        // Initialize views
        etEmail = findViewById(R.id.etEmailSignIn)
        etPassword = findViewById(R.id.etPasswordSignIn)
        btnMasuk = findViewById(R.id.btnMasuk)
        tvDaftar = findViewById(R.id.tvDaftar)

        // Set font
        val nunitoSans = ResourcesCompat.getFont(this, R.font.nunito_sans)
        etEmail.typeface = nunitoSans
        etPassword.typeface = nunitoSans
        btnMasuk.typeface = nunitoSans
        tvDaftar.typeface = nunitoSans

        // TextView Daftar click - langsung ke SignUpActivity
        tvDaftar.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // Button Masuk click
        btnMasuk.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateSignIn(email, password)) {
                // Cek login ke database
                lifecycleScope.launch {
                    val user = database.userDao().login(email, password)

                    runOnUiThread {
                        if (user != null) {
                            // Login berhasil - simpan session
                            userPreferences.saveLoginSession(user.email)

                            // Show success snackbar
                            showCustomSnackbar("Login berhasil!", R.color.yellow)

                            // Navigate to MainActivity after 1 second
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }, 1000)
                        } else {
                            // Login gagal - email atau password salah
                            showCustomSnackbar("Email atau password salah", R.color.red)
                        }
                    }
                }
            }
        }
    }

    private fun validateSignIn(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            etEmail.error = "Email tidak boleh kosong"
            return false
        }

        if (!isValidUnsoedEmail(email)) {
            etEmail.error = "Gunakan email Unsoed (@unsoed.ac.id)"
            return false
        }

        if (password.isEmpty()) {
            etPassword.error = "Password tidak boleh kosong"
            return false
        }
        return true
    }

    private fun isValidUnsoedEmail(email: String): Boolean {
        val regex = "^[a-zA-Z0-9._%+-]+@(unsoed\\.ac\\.id|mhs\\.unsoed\\.ac\\.id)$".toRegex()
        return regex.matches(email)
    }

    private fun showCustomSnackbar(message: String, colorRes: Int) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        snackbarView.backgroundTintList = null

        snackbarView.setBackgroundColor(ContextCompat.getColor(this, colorRes))
        snackbarView.setPadding(32, 48, 32, 48)
        snackbarView.minimumHeight = 120

        val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.black))
        textView.typeface = ResourcesCompat.getFont(this, R.font.nunito_sans)
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
}