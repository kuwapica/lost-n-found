package com.example.lostnfound.data.model

import android.net.Uri

data class LostItem(
    val id: String = "",
    val namaBarang: String = "",
    val lokasiLost: String = "",
    val waktu: String = "",
    val deskripsi: String = "",
    val imageUri: Uri? = null,
    val timestamp: Long = System.currentTimeMillis()

)