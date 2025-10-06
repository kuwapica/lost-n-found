package com.example.lostnfound.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lostnfound.entity.FoundItemEntity

@Dao
interface FoundItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FoundItemEntity): Long

    @Update
    suspend fun update(item: FoundItemEntity)

    @Delete
    suspend fun delete(item: FoundItemEntity)

    @Query("SELECT * FROM found_items_table ORDER BY timestamp DESC")
    fun getAllItems(): LiveData<List<FoundItemEntity>>

    @Query("SELECT * FROM found_items_table WHERE status = :status ORDER BY timestamp DESC")
    fun getItemByStatus(status: String): LiveData<List<FoundItemEntity>>

    @Query("SELECT * FROM found_items_table WHERE id = :id")
    suspend fun getItemById(id: Int): FoundItemEntity?

    @Query("DELETE FROM found_items_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM found_items_table WHERE namaBarang LIKE '%' || :query || '%' OR deskripsi LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchITEMS(query: String): LiveData<List<FoundItemEntity>>
}