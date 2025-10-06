package com.example.lostnfound.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.lostnfound.dao.LostItemDao
import com.example.lostnfound.data.model.FormStatus
import com.example.lostnfound.entity.LostItemEntity

class LostItemRepository(private val lostItemDao: LostItemDao) {
    val allItems: LiveData<List<LostItemEntity>> = lostItemDao.getAllItems()

    suspend fun insert(item: LostItemEntity): Long {
        return lostItemDao.insert(item)
    }

    suspend fun update(item: LostItemEntity) {
        lostItemDao.update(item)
    }

    suspend fun delete(item: LostItemEntity) {
        lostItemDao.delete(item)
    }

    fun getItemByStatus(status: String): LiveData<List<LostItemEntity>> {
        return lostItemDao.getItemByStatus(status)
    }

    suspend fun getItemById(id: Int): LostItemEntity? {
        return lostItemDao.getItemById(id)
    }

    fun searchItems(query: String): LiveData<List<LostItemEntity>> {
        return lostItemDao.searchITEMS(query)
    }

    suspend fun deleteAll() {
        lostItemDao.deleteAll()
    }
}