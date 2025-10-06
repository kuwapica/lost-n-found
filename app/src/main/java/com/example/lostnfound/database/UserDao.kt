package com.example.lostnfound.database

import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE users SET nama = :nama WHERE email = :email")
    suspend fun updateNama(email: String, nama: String)

    @Query("UPDATE users SET password = :password WHERE email = :email")
    suspend fun updatePassword(email: String, password: String)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun isEmailExists(email: String): Int
}