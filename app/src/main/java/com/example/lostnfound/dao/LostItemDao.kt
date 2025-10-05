package com.example.lostnfound.dao

import android.R
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lostnfound.entity.LostItemEntity

@Dao
interface LostItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LostItemEntity): Long

    @Update
    suspend fun update(item: LostItemEntity)

    @Delete
    suspend fun delete(item: LostItemEntity)

    @Query("SELECT * FROM lost_items_table ORDER BY timestamp DESC")
    fun getAllItems(): LiveData<List<LostItemEntity>>

    @Query("SELECT * FROM lost_items_table WHERE status = :status ORDER BY timestamp DESC")
    fun getItemByStatus(status: String): LiveData<List<LostItemEntity>>

    @Query("SELECT * FROM lost_items_table WHERE id = :id")
    suspend fun getItemById(id: Int): LostItemEntity?

    @Query("DELETE FROM lost_items_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM lost_items_table WHERE namaBarang LIKE '%' || :query || '%' OR deskripsi LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchITEMS(query: String): LiveData<List<LostItemEntity>>
}