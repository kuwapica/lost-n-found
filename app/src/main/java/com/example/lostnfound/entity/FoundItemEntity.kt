package com.example.lostnfound.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "found_items_table")
data class FoundItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val namaBarang: String,
    val lokasiFound: String,
    val waktu: String,
    val deskripsi: String,
    val imagePath: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "lost"
)
