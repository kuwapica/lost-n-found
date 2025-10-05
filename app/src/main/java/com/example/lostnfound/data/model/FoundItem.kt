package com.example.lostnfound.data.model

import android.net.Uri

data class FoundItem(
    val id: String = "",
    val namaBarang: String = "",
    val lokasiFound: String = "",
    val waktu: String = "",
    val deskripsi: String = "",
    val imageUri: Uri? = null,
    val timestamp: Long = System.currentTimeMillis()
)
