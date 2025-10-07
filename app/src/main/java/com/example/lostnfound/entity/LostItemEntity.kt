package com.example.lostnfound.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lost_items_table")
data class LostItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val namaBarang: String,
    val lokasiLost: String,
    val waktu: String,
    val deskripsi: String,
    val imagePath: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "lost",
    val userEmail: String
)
