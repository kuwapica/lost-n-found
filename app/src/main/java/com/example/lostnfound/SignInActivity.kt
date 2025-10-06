package com.example.lostnfound

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

class SignInActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnMasuk: Button
    private lateinit var tvDaftar: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

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
                // Show success snackbar
                showSuccessSnackbar("Login berhasil!")

                // Navigate to BerandaLost after 1 second
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, SignUpActivity::class.java))
                    finish()
                }, 1000)
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

        // TODO: Implement actual authentication logic here
        return true
    }

    private fun isValidUnsoedEmail(email: String): Boolean {
        val regex = "^[a-zA-Z0-9._]+@unsoed\\.ac\\.id$".toRegex()
        return regex.matches(email)
    }

    private fun showSuccessSnackbar(message: String) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view

        // Set background color kuning
        snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))

        // Set padding untuk membuat snackbar lebih besar
        snackbarView.setPadding(32, 48, 32, 48)

        // Set minimum height untuk snackbar
        snackbarView.minimumHeight = 120

        // Ambil TextView dari Snackbar dan customize
        val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.black))
        textView.typeface = ResourcesCompat.getFont(this, R.font.nunito_sans)
        textView.textSize = 18f // Ukuran text lebih besar
        textView.gravity = Gravity.CENTER // Text di tengah
        textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

        // Set layout params untuk membuat text lebih center
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        params.topMargin = 100 // Margin dari atas
        params.width = FrameLayout.LayoutParams.MATCH_PARENT
        snackbarView.layoutParams = params

        snackbar.show()
    }
}