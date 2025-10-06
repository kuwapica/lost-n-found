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

class SignUpActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnDaftar: Button
    private lateinit var tvMasuk: TextView
    private lateinit var tvPasswordError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Init views
        etNama = findViewById(R.id.etNamaSignUp)
        etEmail = findViewById(R.id.etEmailSignUp)
        etPassword = findViewById(R.id.etPasswordSignUp)
        etConfirmPassword = findViewById(R.id.etConfirmPasswordSignUp)
        btnDaftar = findViewById(R.id.btnDaftar)
        tvMasuk = findViewById(R.id.tvMasuk)
        tvPasswordError = findViewById(R.id.tvPasswordError)

        // Set font Nunito Sans
        val nunitoSans = ResourcesCompat.getFont(this, R.font.nunito_sans)
        etNama.typeface = nunitoSans
        etEmail.typeface = nunitoSans
        etPassword.typeface = nunitoSans
        etConfirmPassword.typeface = nunitoSans
        btnDaftar.typeface = nunitoSans
        tvMasuk.typeface = nunitoSans
        tvPasswordError.typeface = nunitoSans

        // Button Daftar
        btnDaftar.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putBoolean("isLoggedIn", true)
                apply() // Simpan perubahan
            }

            // 2. Tampilkan Snackbar bahwa pendaftaran berhasil.
            showSuccessSnackbar("Registrasi berhasil!")

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, SignInActivity::class.java)

                // 4. Gunakan flags untuk membersihkan riwayat activity sebelumnya.
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish() // Tutup SignUpActivity secara permanen
            }, 1000) // Tunda 1 detik agar Snackbar terlihat
        }

        // TextView Masuk - navigate to SignInActivity
        tvMasuk.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
//            finish()
        }
    }

    private fun validateSignUp(nama: String, email: String, password: String, confirmPassword: String): Boolean {
        if (nama.isEmpty()) {
            etNama.error = "Nama tidak boleh kosong"
            return false
        }

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

        if (!isValidPassword(password)) {
            tvPasswordError.text = "password harus mengandung minimal satu huruf kapital, angka, dan tanda baca ./@"
            tvPasswordError.setTextColor(ContextCompat.getColor(this, R.color.red))
            tvPasswordError.visibility = TextView.VISIBLE
            return false
        } else {
            tvPasswordError.visibility = TextView.GONE
        }

        if (confirmPassword != password) {
            etConfirmPassword.error = "Konfirmasi password tidak sama"
            return false
        }

        return true
    }

    private fun isValidUnsoedEmail(email: String): Boolean {
        val regex = "^[a-zA-Z0-9._%+-]+@(unsoed\\.ac\\.id|mhs\\.unsoed\\.ac\\.id)$".toRegex()
        return regex.matches(email)
    }

    private fun isValidPassword(password: String): Boolean {
        // Minimal 8 karakter, ada huruf kapital, ada angka, hanya boleh ./@
        val regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[./@])[A-Za-z\\d./@]{8,}$".toRegex()
        return regex.matches(password)
    }

    private fun showSuccessSnackbar(message: String) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        snackbarView.backgroundTintList=null

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