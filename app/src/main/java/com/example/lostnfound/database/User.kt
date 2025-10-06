package com.example.lostnfound.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val email: String,
    val nama: String,
    val password: String // Akan di-hash
)